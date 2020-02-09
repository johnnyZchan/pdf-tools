package com.ledi.pdftools.beans;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
public class PdfModel {

    private Integer type;
    private String awb;
    private String awbReplace;
    private Integer num;
    private BigDecimal weight;
    private BigDecimal declareTotalAmountUsd;
    private BigDecimal declareFreightAmountUsd;
    private String declareFreightAmountUnit;
    private BigDecimal clearanceAmount;
    private BigDecimal bprAmount;
    private BigDecimal tariff;
    private BigDecimal exciseTax;
    private BigDecimal localExciseTax;
    private BigDecimal taxTotalAmount;
    private BigDecimal usdJpyExchangeRate;
    private BigDecimal tariffTotalAmount;
    private BigDecimal countryExciseTaxTotalAmount;
    private BigDecimal localExciseTaxTotalAmount;
    private Integer makeStatus;
    private Timestamp makeTime;
    private Timestamp permissionTime;
    private String importer;
    private String declareFreightTradeTerms;
    private String declareFreightCurrency;
}
