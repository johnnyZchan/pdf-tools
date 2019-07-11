package com.ledi.pdftools.services.impl;

import com.ledi.pdftools.beans.PdfListModel;
import com.ledi.pdftools.beans.PdfModel;
import com.ledi.pdftools.beans.SkipModel;
import com.ledi.pdftools.beans.ank.ShipModel;
import com.ledi.pdftools.constants.CodeInfo;
import com.ledi.pdftools.entities.PdfFileEntity;
import com.ledi.pdftools.entities.PdfListEntity;
import com.ledi.pdftools.exceptions.ServiceException;
import com.ledi.pdftools.mappers.PdfFileMapper;
import com.ledi.pdftools.mappers.PdfListMapper;
import com.ledi.pdftools.services.PdfDataCoordinateService;
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
    @Resource
    PdfDataCoordinateService pdfDataCoordinateService;

    public int getPdfListCount(String awb, Integer makeStatus, String makeStartTime, String makeEndTime, String permissionStartTime, String permissionEndTime) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", PdfListEntity.TYPE_ORIGINAL);
        params.put("awb", awb);
        params.put("makeStatus", makeStatus);
        params.put("makeStartTime", DataUtil.convertString2Timestamp(makeStartTime, "yyyy-MM-dd"));
        params.put("makeEndTime", DataUtil.convertString2Timestamp(makeEndTime, "yyyy-MM-dd"));
        params.put("permissionStartTime", DataUtil.convertString2Timestamp(permissionStartTime, "yyyy-MM-dd"));
        params.put("permissionEndTime", DataUtil.convertString2Timestamp(permissionEndTime, "yyyy-MM-dd"));
        return pdfListMapper.countByCondition(params);
    }

    public List<PdfListEntity> getPdfList(String awb, Integer makeStatus, String makeStartTime, String makeEndTime, String permissionStartTime, String permissionEndTime, Integer start, Integer length) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", PdfListEntity.TYPE_ORIGINAL);
        params.put("awb", awb);
        params.put("makeStatus", makeStatus);
        params.put("makeStartTime", DataUtil.convertString2Timestamp(makeStartTime, "yyyy-MM-dd"));
        params.put("makeEndTime", DataUtil.convertString2Timestamp(makeEndTime, "yyyy-MM-dd"));
        params.put("permissionStartTime", DataUtil.convertString2Timestamp(permissionStartTime, "yyyy-MM-dd"));
        params.put("permissionEndTime", DataUtil.convertString2Timestamp(permissionEndTime, "yyyy-MM-dd"));

        String orderSql = "create_time desc";
        return pdfListMapper.findByCondition(params, orderSql, start, length);
    }

    public List<PdfListEntity> getPdfList(List<String> awbList, int type) {
        return this.getPdfList(awbList, type, PdfListEntity.MAKE_STATUS_YES);
    }

    public List<PdfListEntity> getPdfList(List<String> awbList, int type, Integer makeStatus) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", type);
        params.put("makeStatus", makeStatus);
        params.put("awbList", awbList);
        return pdfListMapper.findByCondition(params, null, null, null);
    }

    public List<PdfListModel> getPdfModelList(String awb, Integer makeStatus, String makeStartTime, String makeEndTime, String permissionStartTime, String permissionEndTime, Integer start, Integer length) {
        List<PdfListModel> result = null;
        List<PdfListEntity> dataList = this.getPdfList(awb, makeStatus, makeStartTime, makeEndTime, permissionStartTime, permissionEndTime, start, length);
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

    public boolean isAwbExist(String awb) {
        if (this.getOriginalPdf(awb) != null) {
            return true;
        }

        return false;
    }

    public List<SkipModel> addPdf(List<String> pdfFileIdList, boolean coverFlg) {
        if (pdfFileIdList == null || pdfFileIdList.isEmpty()) {
            return null;
        }

        List<SkipModel> skipList = new ArrayList<SkipModel>();
        for (String pdfFileId : pdfFileIdList) {
            try {
                this.addPdf(pdfFileId, coverFlg);
            } catch (ServiceException e) {
                if (e.getCode() == CodeInfo.CODE_AWB_ALREADY_EXIST) {
                    SkipModel model = new SkipModel();
                    model.setAwb(e.getMessage());
                    model.setPdfFileId(pdfFileId);
                    skipList.add(model);
                }
            }
        }

        return skipList;
    }

    public void addPdf(String pdfFileId, boolean coverFlg) {
        this.addPdf(pdfFileId, null, coverFlg);
    }

    @Transactional
    public void addPdf(String pdfFileId, ShipModel shipModel, boolean coverFlg) {
        PdfFileEntity pdfFileEntity = this.pdfFileMapper.findById(pdfFileId);
        if (pdfFileEntity == null) {
            throw new ServiceException(CodeInfo.CODE_PDF_FILE_NOT_EXIST);
        }

        PdfListEntity pdfListEntity = this.pdfFileService.readDataFromPdfFile(pdfFileEntity);
        if (pdfListEntity != null && StringUtils.isNotBlank(pdfListEntity.getAwb())) {
            PdfListEntity oldPdfListEntity = this.getOriginalPdf(pdfListEntity.getAwb());
            // 单号已存在，是否覆盖：false=不覆盖，需要确认
            if (oldPdfListEntity != null && !coverFlg) {
                throw new ServiceException(CodeInfo.CODE_AWB_ALREADY_EXIST, pdfListEntity.getAwb());
            }

            pdfListEntity.setType(PdfListEntity.TYPE_ORIGINAL);
            pdfListEntity.setMakeStatus(PdfListEntity.MAKE_STATUS_NO);
            pdfListEntity.setMakeTime(new Timestamp(System.currentTimeMillis()));
            pdfListEntity.setDelStatus(PdfListEntity.DEL_STATUS_NO);
            pdfListEntity.setCreateTime(pdfListEntity.getMakeTime());
            if (shipModel != null && StringUtils.isNotBlank(shipModel.getPermitTime())) {
                pdfListEntity.setPermissionTime(DataUtil.convertString2Timestamp(shipModel.getPermitTime(), "yyyy-MM-dd HH:mm"));
            }
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

    @Transactional
    public void delPdf(List<String> awbList) {
        if (awbList != null && !awbList.isEmpty()) {
            for (String awb : awbList) {
                List<PdfListEntity> pdfList = this.pdfListMapper.findByAwb(awb);
                if (pdfList != null && !pdfList.isEmpty()) {
                    for (PdfListEntity entity : pdfList) {
                        this.pdfListMapper.delete(entity.getPdfId());
                        this.pdfFileService.deletePdfFile(entity.getPdfId());
                    }
                }
            }
        }
    }

    public List<String> makePdf(List<String> awbList, boolean isDelete, boolean isReplace) {
        if (awbList == null || awbList.isEmpty()) {
            return null;
        }
        if (!isDelete && !isReplace) {
            return null;
        }

        List<String> failAwbList = new ArrayList<String>();
        for (String awb : awbList) {
            if (!makePdf(awb, isDelete, isReplace)) {
                failAwbList.add(awb);
            }
        }

        return failAwbList;
    }

    @Transactional
    public boolean makePdf(String awb, boolean isDelete, boolean isReplace) {
        try {
            PdfListEntity updatedPdf = this.getUpdatedPdf(awb);
            if (updatedPdf == null) {
                log.warn("未找到PDF[" + awb + "]的更新数据");
                return false;
            }
            PdfListEntity originalPdf = this.getOriginalPdf(awb);
            if (originalPdf == null) {
                log.warn("未找到PDF[" + awb + "]的原始数据");
                return false;
            }

            // 将原始文件拷贝一份，作为更新数据的基础版本
            this.pdfFileService.makeUpdatedFile(originalPdf, updatedPdf);

            if (isDelete) {
                this.clearPdfData(updatedPdf);
            }
            if (isReplace) {
                this.replacePdfData(updatedPdf, originalPdf);
            }

            updatedPdf.setMakeStatus(PdfListEntity.MAKE_STATUS_YES);
            updatedPdf.setMakeTime(new Timestamp(System.currentTimeMillis()));
            this.pdfListMapper.update(updatedPdf);

            originalPdf.setMakeStatus(PdfListEntity.MAKE_STATUS_YES);
            originalPdf.setMakeTime(updatedPdf.getMakeTime());
            this.pdfListMapper.update(originalPdf);
        } catch (Exception e) {
            log.warn("制作PDF[" + awb + "]失败", e);
            return false;
        }

        return true;
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
        if (pdfList == null || pdfList.isEmpty()) {
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
        model.setPermissionTime(entity.getPermissionTime());

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
            result.setDeclareTotalAmountUsd(updatedPdf.getDeclareTotalAmountUsd().subtract(originalPdf.getDeclareTotalAmountUsd()));
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
        updatedEntity.setClearanceAmount(updatedEntity.getDeclareTotalAmountUsd());
        // BPR合计=原始有数据，新数据用通关金额；如果原始没数据，新数据继续为空
        if (originalEntity.getBprAmount() != null) {
            updatedEntity.setBprAmount(updatedEntity.getClearanceAmount());
        }
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
        updatedEntity.setProd1DeclareAmountJpy(DataUtil.round(updatedEntity.getProd1DeclareAmountJpy(), 1));

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
        updatedEntity.setProd1LocalExciseTaxAmount((updatedEntity.getProd1LocalExciseTaxBase().multiply(new BigDecimal(17))).divide(new BigDecimal(63), 0, BigDecimal.ROUND_HALF_UP));

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
            updatedEntity.setProd2DeclareAmountJpy(DataUtil.round(updatedEntity.getProd2DeclareAmountJpy(), 1));

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
            updatedEntity.setProd2LocalExciseTaxAmount((updatedEntity.getProd2LocalExciseTaxBase().multiply(new BigDecimal(17))).divide(new BigDecimal(63), 0, BigDecimal.ROUND_HALF_UP));
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
            updatedEntity.setProd3DeclareAmountJpy(DataUtil.round(updatedEntity.getProd3DeclareAmountJpy(), 1));

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
            updatedEntity.setProd3LocalExciseTaxAmount((updatedEntity.getProd3LocalExciseTaxBase().multiply(new BigDecimal(17))).divide(new BigDecimal(63), 0, BigDecimal.ROUND_HALF_UP));
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

        // 关税用关税合计百位取整
        updatedEntity.setTariff(DataUtil.round(updatedEntity.getTariffTotalAmount(), 100));
        // 消费税用国内消费税合计百位取整
        updatedEntity.setExciseTax(DataUtil.round(updatedEntity.getCountryExciseTaxTotalAmount(), 100));
        // 地方消费税用地方消费税合计百位取整
        updatedEntity.setLocalExciseTax(DataUtil.round(updatedEntity.getLocalExciseTaxTotalAmount(), 100));
        // 税金总计用以上三个数据相加
        updatedEntity.setTaxTotalAmount(updatedEntity.getTariff().add(updatedEntity.getExciseTax().add(updatedEntity.getLocalExciseTax())));

        updatedEntity.setType(PdfListEntity.TYPE_UPDATED);
        updatedEntity.setMakeStatus(PdfListEntity.MAKE_STATUS_NO);
        updatedEntity.setDelStatus(PdfListEntity.DEL_STATUS_NO);
        updatedEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
    }

    public void replacePdfData(PdfListEntity updatedPdf, PdfListEntity originalPdf) {
        if (updatedPdf == null) {
            return;
        }
        if (originalPdf == null) {
            return;
        }
        PdfFileEntity updatedPdfFile = this.pdfFileService.getPdfFileByPdfId(updatedPdf.getPdfId());
        this.pdfFileService.replacePdfFile(updatedPdf, originalPdf, updatedPdfFile);
    }

    public void clearPdfData(PdfListEntity updatedPdf) {
        if (updatedPdf == null) {
            return;
        }
        PdfFileEntity updatedPdfFile = this.pdfFileService.getPdfFileByPdfId(updatedPdf.getPdfId());
        this.pdfFileService.clearPdfFile(updatedPdfFile);
    }
}
