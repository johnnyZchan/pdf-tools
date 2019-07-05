package com.ledi.pdftools.services.impl;

import com.ledi.pdftools.beans.PdfListModel;
import com.ledi.pdftools.beans.PdfModel;
import com.ledi.pdftools.constants.CodeInfo;
import com.ledi.pdftools.entities.PdfFileEntity;
import com.ledi.pdftools.entities.PdfListEntity;
import com.ledi.pdftools.exceptions.ServiceException;
import com.ledi.pdftools.mappers.PdfFileMapper;
import com.ledi.pdftools.mappers.PdfListMapper;
import com.ledi.pdftools.services.PdfFileService;
import com.ledi.pdftools.services.PdfListService;
import com.ledi.pdftools.utils.DataUtil;
import com.ledi.pdftools.utils.IDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("pdfListService")
@Slf4j
public class PdfListServiceImpl implements PdfListService {

    @Resource
    PdfListMapper pdfListMapper;
    @Resource
    PdfFileMapper pdfFileMapper;
    @Resource
    PdfFileService pdfFileService;

    public int getPdfListCount() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", PdfListEntity.TYPE_ORIGINAL);
        return pdfListMapper.countByCondition(params);
    }

    public List<PdfListEntity> getPdfList(Integer start, Integer length) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", PdfListEntity.TYPE_ORIGINAL);

        String orderSql = "create_time desc";
        return pdfListMapper.findByCondition(params, orderSql, start, length);
    }

    public List<PdfListModel> getPdfModelList(Integer start, Integer length) {
        List<PdfListModel> result = null;
        List<PdfListEntity> dataList = this.getPdfList(start, length);
        if (dataList != null) {
            result = new ArrayList<PdfListModel>();
            for (PdfListEntity entity : dataList) {
                result.add(this.convertEntity2PdfListModel(entity));
            }
        }

        return result;
    }

    public PdfListEntity getOriginalPdf(String awb) {
        return this.pdfListMapper.findPdfByAwb(PdfListEntity.TYPE_ORIGINAL, awb);
    }

    public PdfListEntity getUpdatedPdf(String awb) {
        return this.pdfListMapper.findPdfByAwb(PdfListEntity.TYPE_UPDATED, awb);
    }

    @Transactional
    public void addPdf(String pdfFileId, boolean coverFlg) {
        PdfFileEntity pdfFileEntity = this.pdfFileMapper.findById(pdfFileId);
        if (pdfFileEntity == null) {
            throw new ServiceException(CodeInfo.CODE_PDF_FILE_NOT_EXIST);
        }

        PdfListEntity pdfListEntity = this.pdfFileService.readDataFromPdfFile(pdfFileEntity);
        if (pdfListEntity != null && StringUtils.isNotBlank(pdfListEntity.getAwb())) {
            PdfListEntity oldPdfListEntity = this.getOriginalPdf(pdfListEntity.getAwb());
            // 单号已存在，是否覆盖：false=不覆盖，需要确认
            if (oldPdfListEntity != null && !coverFlg) {
                throw new ServiceException(CodeInfo.CODE_AWB_ALREADY_EXIST);
            }

            pdfListEntity.setType(PdfListEntity.TYPE_ORIGINAL);
            pdfListEntity.setMakeStatus(PdfListEntity.MAKE_STATUS_NO);
            pdfListEntity.setMakeTime(new Timestamp(System.currentTimeMillis()));
            pdfListEntity.setDelStatus(PdfListEntity.DEL_STATUS_NO);
            pdfListEntity.setCreateTime(pdfListEntity.getMakeTime());
            // 如果单号已经存在，则覆盖数据；并且如果已经制作了更新数据，需要删除更新文件并把更新数据的状态改成未制作
            if (oldPdfListEntity != null) {
                pdfListEntity.setPdfId(oldPdfListEntity.getPdfId());

                this.pdfListMapper.update(pdfListEntity);

                PdfListEntity updatedPdf = this.getUpdatedPdf(pdfListEntity.getAwb());
                if (updatedPdf != null && updatedPdf.getMakeStatus() == PdfListEntity.MAKE_STATUS_YES) {
                    this.pdfFileService.deletePdfFile(updatedPdf.getPdfId());

                    updatedPdf.setMakeTime(null);
                    updatedPdf.setMakeStatus(PdfListEntity.MAKE_STATUS_NO);
                    this.pdfListMapper.update(updatedPdf);
                }
            } else {
                String pdfId = IDUtil.uuid();
                pdfListEntity.setPdfId(pdfId);

                this.pdfListMapper.save(pdfListEntity);
            }

            pdfFileEntity.setPdfId(pdfListEntity.getPdfId());
            this.pdfFileMapper.update(pdfFileEntity);
        } else {
            throw new ServiceException("awb.can.not.read.error");
        }
    }

    public int getUpdatedAndMadeDuplicatePdfCount(List<String> awbList) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", PdfListEntity.TYPE_UPDATED);
        params.put("makeStatus", PdfListEntity.MAKE_STATUS_YES);
        params.put("awbList", awbList);
        return this.pdfListMapper.countByCondition(params);
    }

    @Transactional
    public void addUpdatedData(List<PdfListEntity> pdfList, boolean coverFlg) {
        if (pdfList != null || pdfList.size() == 0) {
            throw new ServiceException("file.not.data.error");
        }

        // 检查是否有重复且已制作的更新数据
        List<String> awbList = new ArrayList<String>();
        pdfList.stream().map(pdf -> awbList.add(pdf.getAwb()));
        int duplicateCount = this.getUpdatedAndMadeDuplicatePdfCount(awbList);
        if (duplicateCount > 0 && !coverFlg) {
            throw new ServiceException(CodeInfo.CODE_AWB_ALREADY_EXIST);
        }

        for (PdfListEntity pdfListEntity : pdfList) {
            // 查看单号是否存在，不存在则忽略
            if (StringUtils.isBlank(pdfListEntity.getAwb())) {
                continue;
            }
            PdfListEntity originalPdf = this.getOriginalPdf(pdfListEntity.getAwb());
            if (originalPdf == null) {
                continue;
            }

            this.createUpdatedEntity(pdfListEntity, originalPdf);
            PdfListEntity oldUpdatedPdf = this.getUpdatedPdf(pdfListEntity.getAwb());
            // 如果单号已经存在，则覆盖数据；如果是制作状态，需要删除更新文件
            if (oldUpdatedPdf != null) {
                pdfListEntity.setPdfId(oldUpdatedPdf.getPdfId());
                this.pdfListMapper.update(pdfListEntity);

                this.pdfFileService.deletePdfFile(pdfListEntity.getPdfId());
            } else {
                pdfListEntity.setPdfId(IDUtil.uuid());
                this.pdfListMapper.save(pdfListEntity);
            }

        }
    }

    public PdfListModel convertEntity2PdfListModel(PdfListEntity originalEntity) {
        if (originalEntity == null) {
            return null;
        }

        PdfModel originalModel = this.convertEntity2PdfModel(originalEntity);
        PdfListEntity updatedEntity = this.getUpdatedPdf(originalEntity.getAwb());
        PdfModel updatedModel = null;
        if (updatedEntity != null) {
            updatedModel = this.convertEntity2PdfModel(updatedEntity);
        }

        PdfListModel result = new PdfListModel();
        result.setOriginalPdf(originalModel);
        result.setUpdatedPdf(updatedModel);
        result.setComparePdf(this.createComparePdfModel(originalModel, updatedModel));

        return result;
    }

    public PdfModel convertEntity2PdfModel(PdfListEntity entity) {
        if (entity == null) {
            return null;
        }

        PdfModel model = new PdfModel();
        model.setType(entity.getType());
        model.setAwb(entity.getAwb());
        model.setAwbReplace(entity.getAwbReplace());
        model.setNum(entity.getNum());
        model.setWeight(entity.getWeight());
        model.setDeclareTotalAmountUsd(entity.getDeclareTotalAmountUsd());
        model.setDeclareFreightAmountUsd(entity.getDeclareFreightAmountUsd());
        model.setClearanceAmount(entity.getClearanceAmount());
        model.setBprAmount(entity.getBprAmount());
        model.setTariff(entity.getTariff());
        model.setExciseTax(entity.getExciseTax());
        model.setLocalExciseTax(entity.getLocalExciseTax());
        model.setTaxTotalAmount(entity.getTaxTotalAmount());
        model.setUsdJpyExchangeRate(entity.getUsdJpyExchangeRate());
        model.setProd1DeclareAmountUsd(entity.getProd1DeclareAmountUsd());
        model.setProd2DeclareAmountUsd(entity.getProd2DeclareAmountUsd());
        model.setProd3DeclareAmountUsd(entity.getProd3DeclareAmountUsd());
        model.setProd1TariffRate(entity.getProd1TariffRate());
        model.setProd2TariffRate(entity.getProd2TariffRate());
        model.setProd3TariffRate(entity.getProd3TariffRate());
        model.setProd1FreightPct(entity.getProd1FreightPct());
        model.setProd2FreightPct(entity.getProd2FreightPct());
        model.setProd3FreightPct(entity.getProd3FreightPct());
        model.setProd1DeclareAmountJpy(entity.getProd1DeclareAmountJpy());
        model.setProd2DeclareAmountJpy(entity.getProd2DeclareAmountJpy());
        model.setProd3DeclareAmountJpy(entity.getProd3DeclareAmountJpy());
        model.setProd1TariffBase(entity.getProd1TariffBase());
        model.setProd2TariffBase(entity.getProd2TariffBase());
        model.setProd3TariffBase(entity.getProd3TariffBase());
        model.setProd1Tariff(entity.getProd1Tariff());
        model.setProd2Tariff(entity.getProd2Tariff());
        model.setProd3Tariff(entity.getProd3Tariff());
        model.setProd1TariffRounding(entity.getProd1TariffRounding());
        model.setProd2TariffRounding(entity.getProd2TariffRounding());
        model.setProd3TariffRounding(entity.getProd3TariffRounding());
        model.setProd1CountryExciseTax(entity.getProd1CountryExciseTax());
        model.setProd2CountryExciseTax(entity.getProd2CountryExciseTax());
        model.setProd3CountryExciseTax(entity.getProd3CountryExciseTax());
        model.setProd1CountryExciseTaxBase(entity.getProd1CountryExciseTaxBase());
        model.setProd2CountryExciseTaxBase(entity.getProd2CountryExciseTaxBase());
        model.setProd3CountryExciseTaxBase(entity.getProd3CountryExciseTaxBase());
        model.setProd1CountryExciseTaxAmount(entity.getProd1CountryExciseTaxAmount());
        model.setProd2CountryExciseTaxAmount(entity.getProd2CountryExciseTaxAmount());
        model.setProd3CountryExciseTaxAmount(entity.getProd3CountryExciseTaxAmount());
        model.setProd1LocalExciseTaxBase(entity.getProd1LocalExciseTaxBase());
        model.setProd2LocalExciseTaxBase(entity.getProd2LocalExciseTaxBase());
        model.setProd3LocalExciseTaxBase(entity.getProd3LocalExciseTaxBase());
        model.setProd1LocalExciseTaxAmount(entity.getProd1LocalExciseTaxAmount());
        model.setProd2LocalExciseTaxAmount(entity.getProd2LocalExciseTaxAmount());
        model.setProd3LocalExciseTaxAmount(entity.getProd3LocalExciseTaxAmount());
        model.setTariffTotalAmount(entity.getTariffTotalAmount());
        model.setCountryExciseTaxTotalAmount(entity.getCountryExciseTaxTotalAmount());
        model.setLocalExciseTaxTotalAmount(entity.getLocalExciseTaxTotalAmount());
        model.setMakeStatus(entity.getMakeStatus());
        model.setMakeTime(entity.getMakeTime());

        return model;
    }

    public PdfModel createComparePdfModel(PdfModel originalPdf, PdfModel updatedPdf) {
        if (originalPdf == null || updatedPdf == null) {
            return null;
        }

        PdfModel result = new PdfModel();
        if (updatedPdf.getWeight() != null && originalPdf.getWeight() != null) {
            result.setWeight(updatedPdf.getWeight().subtract(originalPdf.getWeight()));
        }
        if (updatedPdf.getDeclareTotalAmountUsd() != null && originalPdf.getDeclareTotalAmountUsd() != null) {
            result.setDeclareTotalAmountUsd(updatedPdf.getDeclareTotalAmountUsd().subtract(originalPdf.getDeclareFreightAmountUsd()));
        }
        if (updatedPdf.getDeclareFreightAmountUsd() != null && originalPdf.getDeclareFreightAmountUsd() != null) {
            result.setDeclareFreightAmountUsd(updatedPdf.getDeclareFreightAmountUsd().subtract(originalPdf.getDeclareFreightAmountUsd()));
        }
        if (updatedPdf.getClearanceAmount() != null && originalPdf.getClearanceAmount() != null) {
            result.setClearanceAmount(updatedPdf.getClearanceAmount().subtract(originalPdf.getClearanceAmount()));
        }
        if (updatedPdf.getTaxTotalAmount() != null && originalPdf.getTaxTotalAmount() != null) {
            result.setTaxTotalAmount(updatedPdf.getTaxTotalAmount().subtract(originalPdf.getTaxTotalAmount()));
        }

        return result;
    }

    public void createUpdatedEntity(PdfListEntity updatedEntity, PdfListEntity originalEntity) {
        // 通关金额=申报价值合计
        updatedEntity.setClearanceAmount(originalEntity.getDeclareTotalAmountUsd());
        // BPR合计=原始有数据，新数据用通关金额；如果原始没数据，新数据继续为空
        if (originalEntity.getBprAmount() != null) {
            updatedEntity.setBprAmount(updatedEntity.getClearanceAmount());
        }
        // 关税用原始数据
        updatedEntity.setTariff(originalEntity.getTariff());
        // 消费税用原始数据
        updatedEntity.setExciseTax(originalEntity.getExciseTax());
        // 地方消费税用原始数据
        updatedEntity.setLocalExciseTax(originalEntity.getLocalExciseTax());
        // 税金总计用原始数据
        updatedEntity.setTaxTotalAmount(originalEntity.getTaxTotalAmount());
        // 美元日元汇率用原始数据
        updatedEntity.setUsdJpyExchangeRate(originalEntity.getUsdJpyExchangeRate());

        /**
         * 品名1是一定有的，2和3不一定有，2和3的判断标准就是判断美金申报价值是否为空
         * 品名美金申报价值是判断标准，如果这个字段没有数据，其他的品名数据都不需要计算了
         */
        // 品名1关税率用原始数据
        updatedEntity.setProd1TariffRate(originalEntity.getProd1TariffRate());
        // 品名1运费比重=品名1申报价值/总申报价值（当品名1申报价值=空，则为100%）
        updatedEntity.setProd1FreightPct(BigDecimal.ONE);
        if (updatedEntity.getDeclareTotalAmountUsd().compareTo(BigDecimal.ZERO) != 0) {
            updatedEntity.setProd1FreightPct(updatedEntity.getProd1DeclareAmountUsd().divide(updatedEntity.getDeclareTotalAmountUsd(), 4, BigDecimal.ROUND_HALF_UP));
        }
        // 品名1日元申报价值=品名1申报价值*美元日元汇率+申报运费USD*美元日元汇率*品名1运费比重
        BigDecimal usdJpyExchangeRate = updatedEntity.getUsdJpyExchangeRate()!=null?updatedEntity.getUsdJpyExchangeRate():BigDecimal.ONE;
        updatedEntity.setProd1DeclareAmountJpy((updatedEntity.getProd1DeclareAmountUsd().multiply(usdJpyExchangeRate)).add(updatedEntity.getDeclareFreightAmountUsd().multiply(usdJpyExchangeRate).multiply(updatedEntity.getProd1FreightPct())));
        // 品名1关税计算基数=品名1日元申报价值，按千位向下取整
        updatedEntity.setProd1TariffBase(DataUtil.round(updatedEntity.getProd1DeclareAmountJpy(), 1000));
        // 品名1关税额=品名1关税计算基数*品名1关税率
        BigDecimal prod1TariffRate = updatedEntity.getProd1TariffRate()!=null?updatedEntity.getProd1TariffRate():BigDecimal.ZERO;
        updatedEntity.setProd1Tariff(updatedEntity.getProd1TariffBase().multiply(prod1TariffRate));
        // 品名1关税取整=品名1关税额，按百位向下取整
        updatedEntity.setProd1TariffRounding(DataUtil.round(updatedEntity.getProd1Tariff(), 100));
        // 品名1国内消费税=品名1日元申报价值+品名1关税取整
        updatedEntity.setProd1CountryExciseTax(updatedEntity.getProd1DeclareAmountJpy().add(updatedEntity.getProd1TariffRounding()));
        // 品名1国内消费税额基数=品名1国内消费税，按千位向下取整
        updatedEntity.setProd1CountryExciseTaxBase(DataUtil.round(updatedEntity.getProd1CountryExciseTax(), 1000));
        // 品名1国内消费税金额=品名1国内消费税额基数*6.3%
        updatedEntity.setProd1CountryExciseTaxAmount(updatedEntity.getProd1CountryExciseTaxBase().multiply(new BigDecimal(0.063)));
        // 品名1地方消费税基数=品名1国内消费税金额，按百位向下取整
        updatedEntity.setProd1LocalExciseTaxBase(DataUtil.round(updatedEntity.getProd1CountryExciseTaxAmount(), 100));
        // 品名1地方消费税金额=品名1地方消费税基数*17/63
        updatedEntity.setProd1LocalExciseTaxAmount((updatedEntity.getProd1LocalExciseTaxBase().multiply(new BigDecimal(17))).divide(new BigDecimal(63), 2, BigDecimal.ROUND_HALF_UP));

        if (updatedEntity.getProd2DeclareAmountUsd() != null) {
            // 品名2关税率用原始数据
            updatedEntity.setProd2TariffRate(originalEntity.getProd2TariffRate());
            // 品名2运费比重=品名2申报价值/总申报价值（当品名2申报价值=空，则为100%）
            updatedEntity.setProd2FreightPct(BigDecimal.ONE);
            if (updatedEntity.getDeclareTotalAmountUsd().compareTo(BigDecimal.ZERO) != 0) {
                updatedEntity.setProd2FreightPct(updatedEntity.getProd2DeclareAmountUsd().divide(updatedEntity.getDeclareTotalAmountUsd(), 4, BigDecimal.ROUND_HALF_UP));
            }
            // 品名2日元申报价值=品名2申报价值*美元日元汇率+申报运费USD*美元日元汇率*品名2运费比重
            updatedEntity.setProd2DeclareAmountJpy((updatedEntity.getProd2DeclareAmountUsd().multiply(usdJpyExchangeRate)).add(updatedEntity.getDeclareFreightAmountUsd().multiply(usdJpyExchangeRate).multiply(updatedEntity.getProd2FreightPct())));
            // 品名2关税计算基数=品名2日元申报价值，按千位向下取整
            updatedEntity.setProd2TariffBase(DataUtil.round(updatedEntity.getProd2DeclareAmountJpy(), 1000));
            // 品名2关税额=品名2关税计算基数*品名2关税率
            BigDecimal prod2TariffRate = updatedEntity.getProd2TariffRate()!=null?updatedEntity.getProd2TariffRate():BigDecimal.ZERO;
            updatedEntity.setProd2Tariff(updatedEntity.getProd2TariffBase().multiply(prod2TariffRate));
            // 品名2关税取整=品名2关税额，按百位向下取整
            updatedEntity.setProd2TariffRounding(DataUtil.round(updatedEntity.getProd2Tariff(), 100));
            // 品名2国内消费税=品名2日元申报价值+品名2关税取整
            updatedEntity.setProd2CountryExciseTax(updatedEntity.getProd2DeclareAmountJpy().add(updatedEntity.getProd2TariffRounding()));
            // 品名2国内消费税额基数=品名2国内消费税，按千位向下取整
            updatedEntity.setProd2CountryExciseTaxBase(DataUtil.round(updatedEntity.getProd2CountryExciseTax(), 1000));
            // 品名2国内消费税金额=品名2国内消费税额基数*6.3%
            updatedEntity.setProd2CountryExciseTaxAmount(updatedEntity.getProd2CountryExciseTaxBase().multiply(new BigDecimal(0.063)));
            // 品名2地方消费税基数=品名2国内消费税金额，按百位向下取整
            updatedEntity.setProd2LocalExciseTaxBase(DataUtil.round(updatedEntity.getProd2CountryExciseTaxAmount(), 100));
            // 品名2地方消费税金额=品名2地方消费税基数*17/63
            updatedEntity.setProd2LocalExciseTaxAmount((updatedEntity.getProd2LocalExciseTaxBase().multiply(new BigDecimal(17))).divide(new BigDecimal(63), 2, BigDecimal.ROUND_HALF_UP));
        }

        if (updatedEntity.getProd3DeclareAmountUsd() != null) {
            // 品名3关税率用原始数据
            updatedEntity.setProd3TariffRate(originalEntity.getProd3TariffRate());
            // 品名3运费比重=品名3申报价值/总申报价值（当品名3申报价值=空，则为100%）
            updatedEntity.setProd3FreightPct(BigDecimal.ONE);
            if (updatedEntity.getDeclareTotalAmountUsd().compareTo(BigDecimal.ZERO) != 0) {
                updatedEntity.setProd3FreightPct(updatedEntity.getProd3DeclareAmountUsd().divide(updatedEntity.getDeclareTotalAmountUsd(), 4, BigDecimal.ROUND_HALF_UP));
            }
            // 品名3日元申报价值=品名3申报价值*美元日元汇率+申报运费USD*美元日元汇率*品名3运费比重
            updatedEntity.setProd3DeclareAmountJpy((updatedEntity.getProd3DeclareAmountUsd().multiply(usdJpyExchangeRate)).add(updatedEntity.getDeclareFreightAmountUsd().multiply(usdJpyExchangeRate).multiply(updatedEntity.getProd3FreightPct())));
            // 品名3关税计算基数=品名3日元申报价值，按千位向下取整
            updatedEntity.setProd3TariffBase(DataUtil.round(updatedEntity.getProd3DeclareAmountJpy(), 1000));
            // 品名3关税额=品名3关税计算基数*品名3关税率
            BigDecimal prod3TariffRate = updatedEntity.getProd3TariffRate()!=null?updatedEntity.getProd3TariffRate():BigDecimal.ZERO;
            updatedEntity.setProd3Tariff(updatedEntity.getProd3TariffBase().multiply(prod3TariffRate));
            // 品名3关税取整=品名3关税额，按百位向下取整
            updatedEntity.setProd3TariffRounding(DataUtil.round(updatedEntity.getProd3Tariff(), 100));
            // 品名3国内消费税=品名3日元申报价值+品名3关税取整
            updatedEntity.setProd3CountryExciseTax(updatedEntity.getProd3DeclareAmountJpy().add(updatedEntity.getProd3TariffRounding()));
            // 品名3国内消费税额基数=品名3国内消费税，按千位向下取整
            updatedEntity.setProd3CountryExciseTaxBase(DataUtil.round(updatedEntity.getProd3CountryExciseTax(), 1000));
            // 品名3国内消费税金额=品名3国内消费税额基数*6.3%
            updatedEntity.setProd3CountryExciseTaxAmount(updatedEntity.getProd3CountryExciseTaxBase().multiply(new BigDecimal(0.063)));
            // 品名3地方消费税基数=品名3国内消费税金额，按百位向下取整
            updatedEntity.setProd3LocalExciseTaxBase(DataUtil.round(updatedEntity.getProd3CountryExciseTaxAmount(), 100));
            // 品名3地方消费税金额=品名3地方消费税基数*17/63
            updatedEntity.setProd3LocalExciseTaxAmount((updatedEntity.getProd3LocalExciseTaxBase().multiply(new BigDecimal(17))).divide(new BigDecimal(63), 2, BigDecimal.ROUND_HALF_UP));
        }

        // 关税合计
        BigDecimal prod1TariffAmount = updatedEntity.getProd1Tariff()!=null?updatedEntity.getProd1Tariff():BigDecimal.ZERO;
        BigDecimal prod2TariffAmount = updatedEntity.getProd2Tariff()!=null?updatedEntity.getProd2Tariff():BigDecimal.ZERO;
        BigDecimal prod3TariffAmount = updatedEntity.getProd3Tariff()!=null?updatedEntity.getProd3Tariff():BigDecimal.ZERO;
        updatedEntity.setTariffTotalAmount(prod1TariffAmount.add(prod2TariffAmount).add(prod3TariffAmount));

        // 国内消费税合计
        BigDecimal prod1CountryExciseTaxAmount = updatedEntity.getProd1CountryExciseTaxAmount()!=null?updatedEntity.getProd1CountryExciseTaxAmount():BigDecimal.ZERO;
        BigDecimal prod2CountryExciseTaxAmount = updatedEntity.getProd2CountryExciseTaxAmount()!=null?updatedEntity.getProd2CountryExciseTaxAmount():BigDecimal.ZERO;
        BigDecimal prod3CountryExciseTaxAmount = updatedEntity.getProd3CountryExciseTaxAmount()!=null?updatedEntity.getProd3CountryExciseTaxAmount():BigDecimal.ZERO;
        updatedEntity.setCountryExciseTaxTotalAmount(prod1CountryExciseTaxAmount.add(prod2CountryExciseTaxAmount).add(prod3CountryExciseTaxAmount));

        // 地方消费税合计
        BigDecimal prod1LocalExciseTaxAmount = updatedEntity.getProd1LocalExciseTaxAmount()!=null?updatedEntity.getProd1LocalExciseTaxAmount():BigDecimal.ZERO;
        BigDecimal prod2LocalExciseTaxAmount = updatedEntity.getProd2LocalExciseTaxAmount()!=null?updatedEntity.getProd2LocalExciseTaxAmount():BigDecimal.ZERO;
        BigDecimal prod3LocalExciseTaxAmount = updatedEntity.getProd3LocalExciseTaxAmount()!=null?updatedEntity.getProd3LocalExciseTaxAmount():BigDecimal.ZERO;
        updatedEntity.setLocalExciseTaxTotalAmount(prod1LocalExciseTaxAmount.add(prod2LocalExciseTaxAmount).add(prod3LocalExciseTaxAmount));

        updatedEntity.setType(PdfListEntity.TYPE_UPDATED);
        updatedEntity.setMakeStatus(PdfListEntity.MAKE_STATUS_NO);
        updatedEntity.setDelStatus(PdfListEntity.DEL_STATUS_NO);
        updatedEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
    }
}
