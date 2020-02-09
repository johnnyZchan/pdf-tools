package com.ledi.pdftools.entities;

import com.ledi.pdftools.utils.BeanUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
public class PdfListEntity {

    public static final int TYPE_ORIGINAL = 1;
    public static final int TYPE_UPDATED = 2;

    public static final int MAKE_STATUS_NO = 0;
    public static final int MAKE_STATUS_YES = 1;

    public static final int DEL_STATUS_NO = 0;
    public static final int DEL_STATUS_YES = 1;


    private String pdfId;
    // 类型（1：原始文件、2：更新文件）
    private Integer type;
    // 单号
    private String awb;
    // 换单号
    private String awbReplace;
    // 件数
    private Integer num;
    // 重量
    private BigDecimal weight;
    // 总申报价值USD
    private BigDecimal declareTotalAmountUsd;
    // 申报运费USD
    private BigDecimal declareFreightAmountUsd;
    // 申报运费单位
    private String declareFreightAmountUnit;
    // 通关金额
    private BigDecimal clearanceAmount;
    // BPR合计
    private BigDecimal bprAmount;
    // 关税
    private BigDecimal tariff;
    // 消费税
    private BigDecimal exciseTax;
    // 地方消费税
    private BigDecimal localExciseTax;
    // 税金总计
    private BigDecimal taxTotalAmount;
    // 美元日元汇率
    private BigDecimal usdJpyExchangeRate;
    // 关税合计
    private BigDecimal tariffTotalAmount;
    // 国内消费税合计
    private BigDecimal countryExciseTaxTotalAmount;
    // 地方消费税合计
    private BigDecimal localExciseTaxTotalAmount;

    private Integer makeStatus;
    private Timestamp makeTime;
    private Integer delStatus;
    private Timestamp createTime;
    private Timestamp permissionTime;

    // 进口商
    private String importer;
    // 申报贸易条款
    private String declareFreightTradeTerms;
    // 申报币种
    private String declareFreightCurrency;

    private Map<String, PdfListDetailEntity> pdfListDetailMap;

    public void setDetail(String fieldCategory, String fieldName, Object value) {
        if (StringUtils.isBlank(fieldCategory)
                || StringUtils.isBlank(fieldName)) {
            return;
        }

        if (pdfListDetailMap == null) {
            pdfListDetailMap = new HashMap<String, PdfListDetailEntity>();
        }
        if (!pdfListDetailMap.containsKey(fieldCategory) || pdfListDetailMap.get(fieldCategory) == null) {
            PdfListDetailEntity detailEntity = new PdfListDetailEntity();
            detailEntity.setProdNo(fieldCategory);
            detailEntity.setPdfId(this.getPdfId());

            pdfListDetailMap.put(fieldCategory, detailEntity);
        }

        try {
            BeanUtil.setFieldValue(pdfListDetailMap.get(fieldCategory), fieldName, value);
        } catch (Exception e) {
            log.warn("error occurred in setDetail()", e);
        }
    }

    public Object getDetail(String fieldCategory, String fieldName) {
        if (StringUtils.isBlank(fieldCategory)
                || StringUtils.isBlank(fieldName)) {
            return null;
        }

        if (pdfListDetailMap == null || pdfListDetailMap.isEmpty()) {
            return null;
        }

        if (!pdfListDetailMap.containsKey(fieldCategory) || pdfListDetailMap.get(fieldCategory) == null) {
            return null;
        }

        Object result = null;
        try {
            result = BeanUtil.getFieldValue(pdfListDetailMap.get(fieldCategory), fieldName);
        } catch (Exception e) {
            log.warn("error occurred in getDetail()", e);
            result = null;
        }
        return result;
    }

    public void initPdfListDetailMap(List<PdfListDetailEntity> pdfDetailList) {
        if (pdfDetailList == null) {
            return;
        }

        this.pdfListDetailMap = new HashMap<String, PdfListDetailEntity>();
        for (PdfListDetailEntity entity : pdfDetailList) {
            this.pdfListDetailMap.put(entity.getProdNo(), entity);
        }
    }
}
