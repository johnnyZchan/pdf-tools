package com.ledi.pdftools.entities;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class PdfListEntity {

    private String pdfId;
    private Integer type;
    private String awb;
    private String awbReplace;
    private Integer num;
    private BigDecimal weight;
    private BigDecimal declareFreightPrice;
    private BigDecimal declareTotalAmountUsd;
    private BigDecimal declareFreightAmountUsd;
    private BigDecimal clearanceAmount;
    private BigDecimal bprAmount;
    private BigDecimal tariff;
    private BigDecimal exciseTax;
    private BigDecimal localExciseTax;
    private BigDecimal taxTotalAmount;
    private BigDecimal usdJpyExchangeRate;
    private BigDecimal prod1DeclareAmountUsd;
    private BigDecimal prod2DeclareAmountUsd;
    private BigDecimal prod3DeclareAmountUsd;
    private BigDecimal prod1TariffRate;
    private BigDecimal prod2TariffRate;
    private BigDecimal prod3TariffRate;
    private BigDecimal prod1FreightPct;
    private BigDecimal prod2FreightPct;
    private BigDecimal prod3FreightPct;
    private BigDecimal prod1DeclareAmountJpy;
    private BigDecimal prod2DeclareAmountJpy;
    private BigDecimal prod3DeclareAmountJpy;
    private BigDecimal prod1TariffBase;
    private BigDecimal prod2TariffBase;
    private BigDecimal prod3TariffBase;
    private BigDecimal prod1Tariff;
    private BigDecimal prod2Tariff;
    private BigDecimal prod3Tariff;
    private BigDecimal prod1TariffRounding;
    private BigDecimal prod2TariffRounding;
    private BigDecimal prod3TariffRounding;
    private BigDecimal prod1CountryExciseTax;
    private BigDecimal prod2CountryExciseTax;
    private BigDecimal prod3CountryExciseTax;
    private BigDecimal prod1CountryExciseTaxBase;
    private BigDecimal prod2CountryExciseTaxBase;
    private BigDecimal prod3CountryExciseTaxBase;
    private BigDecimal prod1CountryExciseTaxAmount;
    private BigDecimal prod2CountryExciseTaxAmount;
    private BigDecimal prod3CountryExciseTaxAmount;
    private BigDecimal prod1LocalExciseTaxBase;
    private BigDecimal prod2LocalExciseTaxBase;
    private BigDecimal prod3LocalExciseTaxBase;
    private BigDecimal prod1LocalExciseTaxAmount;
    private BigDecimal prod2LocalExciseTaxAmount;
    private BigDecimal prod3LocalExciseTaxAmount;
    private BigDecimal tariffTotalAmount;
    private BigDecimal countryExciseTaxTotalAmount;
    private BigDecimal localExciseTaxTotalAmount;
    private Integer makeStatus;
    private Timestamp makeTime;
    private Integer delStatus;
    private Timestamp createTime;


}
