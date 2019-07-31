package com.ledi.pdftools.services.impl;

import com.ledi.pdftools.beans.*;
import com.ledi.pdftools.beans.ank.ShipModel;
import com.ledi.pdftools.constants.CodeInfo;
import com.ledi.pdftools.entities.PdfFileEntity;
import com.ledi.pdftools.entities.PdfListDetailEntity;
import com.ledi.pdftools.entities.PdfListEntity;
import com.ledi.pdftools.exceptions.ServiceException;
import com.ledi.pdftools.mappers.PdfFileMapper;
import com.ledi.pdftools.mappers.PdfListMapper;
import com.ledi.pdftools.services.PdfDataCoordinateService;
import com.ledi.pdftools.services.PdfFileService;
import com.ledi.pdftools.services.PdfListDetailService;
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
    @Resource
    PdfListDetailService pdfListDetailService;

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
        // 如果是从ank下载的，可以直接知道转单号，直接用，不用从文件中读出来的转单号。这样避免格式不一样的pdf导致转单号错误。
        if (pdfListEntity != null && shipModel != null) {
            String awb= DataUtil.formatAwb(shipModel.getInvoiceNo());
            if (StringUtils.isNotBlank(awb)) {
                pdfListEntity.setAwb(awb);
            }
        }

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

            // 重新生成detail数据
            this.pdfListDetailService.deletePdfDetailByPdfId(pdfListEntity.getPdfId());
            if (pdfListEntity.getPdfListDetailMap() != null && !pdfListEntity.getPdfListDetailMap().isEmpty()) {
                for (PdfListDetailEntity detailEntity : pdfListEntity.getPdfListDetailMap().values()) {
                    detailEntity.setPdfId(pdfListEntity.getPdfId());
                    detailEntity.setPdfDetailId(IDUtil.uuid());
                    this.pdfListDetailService.save(detailEntity);
                }
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

                        this.pdfListDetailService.deletePdfDetailByPdfId(entity.getPdfId());
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

            this.pdfFileService.replacePdfFile(updatedPdf, originalPdf, isDelete, isReplace);

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

            // 初始化原始数据的details信息
            originalPdf.initPdfListDetailMap(this.pdfListDetailService.getPdfListDetail(originalPdf.getPdfId()));
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

            this.pdfListDetailService.deletePdfDetailByPdfId(pdfListEntity.getPdfId());
            if (pdfListEntity.getPdfListDetailMap() != null && !pdfListEntity.getPdfListDetailMap().isEmpty()) {
                for (PdfListDetailEntity detailEntity : pdfListEntity.getPdfListDetailMap().values()) {
                    detailEntity.setPdfId(pdfListEntity.getPdfId());
                    detailEntity.setPdfDetailId(IDUtil.uuid());
                    this.pdfListDetailService.save(detailEntity);
                }
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

        result.setDetailList(this.createPdfListDetailModelList(originalEntity, updatedEntity));

        return result;
    }

    public List<PdfListDetailModel> createPdfListDetailModelList(PdfListEntity originalEntity, PdfListEntity updatedEntity) {
        List<PdfListDetailEntity> originalPdfDetailEntityList = null;
        if (originalEntity != null && StringUtils.isNotBlank(originalEntity.getPdfId())) {
            originalPdfDetailEntityList = this.pdfListDetailService.getPdfListDetail(originalEntity.getPdfId());
        }
        if (originalPdfDetailEntityList == null || originalPdfDetailEntityList.isEmpty()) {
            return null;
        }

        List<PdfListDetailEntity> updatedPdfDetailEntityList = null;
        if (updatedEntity != null && StringUtils.isNotBlank(updatedEntity.getPdfId())) {
            updatedPdfDetailEntityList = this.pdfListDetailService.getPdfListDetail(updatedEntity.getPdfId());
        }
        Map<String, PdfListDetailEntity> updatedPdfDetailEntityMap = new HashMap<String, PdfListDetailEntity>();
        if (updatedPdfDetailEntityList != null && !updatedPdfDetailEntityList.isEmpty()) {
            updatedPdfDetailEntityList.forEach(entity -> {updatedPdfDetailEntityMap.put(entity.getProdNo(), entity);});
        }

        List<PdfListDetailModel> result = new ArrayList<PdfListDetailModel>();
        PdfListDetailModel model = null;
        for (PdfListDetailEntity entity : originalPdfDetailEntityList) {
            model = new PdfListDetailModel();
            model.setProdNo(entity.getProdNo());
            model.setOriginalPdfDetail(this.convertEntity2PdfDetailModel(entity));
            if (updatedPdfDetailEntityMap.containsKey(model.getProdNo())) {
                model.setUpdatedPdfDetail(this.convertEntity2PdfDetailModel(updatedPdfDetailEntityMap.get(model.getProdNo())));
            }

            result.add(model);
        }

        return result;
    }

    public PdfDetailModel convertEntity2PdfDetailModel(PdfListDetailEntity entity) {
        if (entity == null) {
            return null;
        }

        PdfDetailModel model = new PdfDetailModel();
        model.setDeclareAmountUsd(entity.getDeclareAmountUsd());
        model.setTariffRate(entity.getTariffRate());
        model.setFreightPct(entity.getFreightPct());
        model.setDeclareAmountJpy(entity.getDeclareAmountJpy());
        model.setTariffBase(entity.getTariffBase());
        model.setTariff(entity.getTariff());
        model.setTariffRounding(entity.getTariffRounding());
        model.setCountryExciseTax(entity.getCountryExciseTax());
        model.setCountryExciseTaxBase(entity.getCountryExciseTaxBase());
        model.setCountryExciseTaxAmount(entity.getCountryExciseTaxAmount());
        model.setLocalExciseTaxBase(entity.getLocalExciseTaxBase());
        model.setLocalExciseTaxAmount(entity.getLocalExciseTaxAmount());

        return model;
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
        BigDecimal usdJpyExchangeRate = updatedEntity.getUsdJpyExchangeRate()!=null?updatedEntity.getUsdJpyExchangeRate():BigDecimal.ONE;

        int index = 1;
        int max = 10;

        while (index <= max) {
            String fieldCategory = "prod" + index;

            BigDecimal declareAmountUsd = DataUtil.valueOfBigDecimal(updatedEntity.getDetail(fieldCategory, "declareAmountUsd"));
            if (declareAmountUsd != null) {
                // 品名X关税率用原始数据
                BigDecimal tariffRate = DataUtil.valueOfBigDecimal(originalEntity.getDetail(fieldCategory, "tariffRate"), BigDecimal.ZERO);
                updatedEntity.setDetail(fieldCategory, "tariffRate", tariffRate);

                // 品名X运费比重=品名1申报价值/总申报价值（当品名1申报价值=空，则为100%）
                BigDecimal freightPct = BigDecimal.ONE;
                if (updatedEntity.getDeclareTotalAmountUsd().compareTo(BigDecimal.ZERO) != 0) {
                    freightPct = declareAmountUsd.divide(updatedEntity.getDeclareTotalAmountUsd(), 4, BigDecimal.ROUND_HALF_UP);

                }
                updatedEntity.setDetail(fieldCategory, "freightPct", freightPct);

                // 品名X日元申报价值=品名X申报价值*美元日元汇率+申报运费USD*美元日元汇率*品名X运费比重
                BigDecimal declareAmountJpy = (declareAmountUsd.multiply(usdJpyExchangeRate)).add(updatedEntity.getDeclareFreightAmountUsd().multiply(usdJpyExchangeRate).multiply(freightPct));
                declareAmountJpy = DataUtil.round(declareAmountJpy, 1);
                updatedEntity.setDetail(fieldCategory, "declareAmountJpy", declareAmountJpy);

                /**
                 * 如果原始数据中的税金合计=0，则更新数据保持和原始数据一致即可，不需要进行计算
                 */
                // 品名X关税计算基数
                BigDecimal tariffBase = DataUtil.valueOfBigDecimal(originalEntity.getDetail(fieldCategory, "tariffBase"));
                // 品名X关税额
                BigDecimal tariff = DataUtil.valueOfBigDecimal(originalEntity.getDetail(fieldCategory, "tariff"));
                // 品名X关税取整
                BigDecimal tariffRounding = DataUtil.valueOfBigDecimal(originalEntity.getDetail(fieldCategory, "tariffRounding"));
                // 品名X国内消费税
                BigDecimal countryExciseTax = DataUtil.valueOfBigDecimal(originalEntity.getDetail(fieldCategory, "countryExciseTax"));
                // 品名X国内消费税额基数
                BigDecimal countryExciseTaxBase = DataUtil.valueOfBigDecimal(originalEntity.getDetail(fieldCategory, "countryExciseTaxBase"));
                // 品名X国内消费税金额
                BigDecimal countryExciseTaxAmount = DataUtil.valueOfBigDecimal(originalEntity.getDetail(fieldCategory, "countryExciseTaxAmount"));
                // 品名X地方消费税基数
                BigDecimal localExciseTaxBase = DataUtil.valueOfBigDecimal(originalEntity.getDetail(fieldCategory, "localExciseTaxBase"));
                // 品名X地方消费税金额
                BigDecimal localExciseTaxAmount = DataUtil.valueOfBigDecimal(originalEntity.getDetail(fieldCategory, "localExciseTaxAmount"));
                if (originalEntity.getTaxTotalAmount() != null && originalEntity.getTaxTotalAmount().compareTo(BigDecimal.ZERO) != 0) {
                    // 品名X关税计算基数=品名X日元申报价值，按千位向下取整
                    tariffBase = DataUtil.round(declareAmountJpy, 1000);
                    // 品名X关税额=品名X关税计算基数*品名1关税率
                    tariff = tariffBase.multiply(tariffRate);
                    // 品名X关税取整=品名X关税额，按百位向下取整
                    tariffRounding = DataUtil.round(tariff, 100);
                    // 品名X国内消费税=品名X日元申报价值+品名X关税取整
                    countryExciseTax = declareAmountJpy.add(tariffRounding);
                    // 品名X国内消费税额基数=品名X国内消费税，按千位向下取整
                    countryExciseTaxBase = DataUtil.round(countryExciseTax, 1000);
                    // 品名X国内消费税金额=品名X国内消费税额基数*6.3%
                    countryExciseTaxAmount = countryExciseTaxBase.multiply(new BigDecimal(0.063));
                    // 品名X地方消费税基数=品名X国内消费税金额，按百位向下取整
                    localExciseTaxBase = DataUtil.round(countryExciseTaxAmount, 100);
                    // 品名X地方消费税金额=品名X地方消费税基数*17/63
                    localExciseTaxAmount = (localExciseTaxBase.multiply(new BigDecimal(17))).divide(new BigDecimal(63), 0, BigDecimal.ROUND_HALF_UP);
                }
                updatedEntity.setDetail(fieldCategory, "tariffBase", tariffBase);
                updatedEntity.setDetail(fieldCategory, "tariff", tariff);
                updatedEntity.setDetail(fieldCategory, "tariffRounding", tariffRounding);
                updatedEntity.setDetail(fieldCategory, "countryExciseTax", countryExciseTax);
                updatedEntity.setDetail(fieldCategory, "countryExciseTaxBase", countryExciseTaxBase);
                updatedEntity.setDetail(fieldCategory, "countryExciseTaxAmount", countryExciseTaxAmount);
                updatedEntity.setDetail(fieldCategory, "localExciseTaxBase", localExciseTaxBase);
                updatedEntity.setDetail(fieldCategory, "localExciseTaxAmount", localExciseTaxAmount);
            }

            index ++;
        }

        // 关税合计
        updatedEntity.setTariffTotalAmount(BigDecimal.ZERO);
        // 国内消费税合计
        updatedEntity.setCountryExciseTaxTotalAmount(BigDecimal.ZERO);
        // 地方消费税合计
        updatedEntity.setLocalExciseTaxTotalAmount(BigDecimal.ZERO);
        if (updatedEntity.getPdfListDetailMap() != null && !updatedEntity.getPdfListDetailMap().isEmpty()) {
            for (PdfListDetailEntity detailEntity : updatedEntity.getPdfListDetailMap().values()) {
                if (detailEntity.getTariff() != null) {
                    updatedEntity.setTariffTotalAmount(updatedEntity.getTariffTotalAmount().add(detailEntity.getTariff()));
                }
                if (detailEntity.getCountryExciseTaxAmount() != null) {
                    updatedEntity.setCountryExciseTaxTotalAmount(updatedEntity.getCountryExciseTaxTotalAmount().add(detailEntity.getCountryExciseTaxAmount()));
                }
                if (detailEntity.getLocalExciseTaxAmount() != null) {
                    updatedEntity.setLocalExciseTaxTotalAmount(updatedEntity.getLocalExciseTaxTotalAmount().add(detailEntity.getLocalExciseTaxAmount()));
                }
            }
        }

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
}
