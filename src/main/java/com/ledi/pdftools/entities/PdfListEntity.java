package com.ledi.pdftools.entities;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
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
    // 品名1 - 3美金申报价值
    private BigDecimal prod1DeclareAmountUsd;
    private BigDecimal prod2DeclareAmountUsd;
    private BigDecimal prod3DeclareAmountUsd;
    // 品名1 - 3关税率
    private BigDecimal prod1TariffRate;
    private BigDecimal prod2TariffRate;
    private BigDecimal prod3TariffRate;
    // 品名1 - 3运费比重
    private BigDecimal prod1FreightPct;
    private BigDecimal prod2FreightPct;
    private BigDecimal prod3FreightPct;
    // 品名1 - 3日元申报价值
    private BigDecimal prod1DeclareAmountJpy;
    private BigDecimal prod2DeclareAmountJpy;
    private BigDecimal prod3DeclareAmountJpy;
    // 品名1 - 3关税计算基数
    private BigDecimal prod1TariffBase;
    private BigDecimal prod2TariffBase;
    private BigDecimal prod3TariffBase;
    // 品名1 - 3关税额
    private BigDecimal prod1Tariff;
    private BigDecimal prod2Tariff;
    private BigDecimal prod3Tariff;
    // 品名1 - 3关税取整
    private BigDecimal prod1TariffRounding;
    private BigDecimal prod2TariffRounding;
    private BigDecimal prod3TariffRounding;
    // 品名1 - 3国内消费税
    private BigDecimal prod1CountryExciseTax;
    private BigDecimal prod2CountryExciseTax;
    private BigDecimal prod3CountryExciseTax;
    // 品名1 - 3国内消费税额基数
    private BigDecimal prod1CountryExciseTaxBase;
    private BigDecimal prod2CountryExciseTaxBase;
    private BigDecimal prod3CountryExciseTaxBase;
    // 品名1 - 3国内消费税金额
    private BigDecimal prod1CountryExciseTaxAmount;
    private BigDecimal prod2CountryExciseTaxAmount;
    private BigDecimal prod3CountryExciseTaxAmount;
    // 品名1 - 3地方消费税基数
    private BigDecimal prod1LocalExciseTaxBase;
    private BigDecimal prod2LocalExciseTaxBase;
    private BigDecimal prod3LocalExciseTaxBase;
    // 品名1 - 3地方消费税金额
    private BigDecimal prod1LocalExciseTaxAmount;
    private BigDecimal prod2LocalExciseTaxAmount;
    private BigDecimal prod3LocalExciseTaxAmount;
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


}
