package com.ledi.pdftools.entities;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PdfListDetailEntity {

    private String pdfDetailId;
    private String pdfId;
    private String prodNo;
    private BigDecimal declareAmountUsd;
    private BigDecimal tariffRate;
    private BigDecimal freightPct;
    private BigDecimal declareAmountJpy;
    private BigDecimal tariffBase;
    private BigDecimal tariff;
    private BigDecimal tariffRounding;
    private BigDecimal countryExciseTax;
    private BigDecimal countryExciseTaxBase;
    private BigDecimal countryExciseTaxAmount;
    private BigDecimal localExciseTaxBase;
    private BigDecimal localExciseTaxAmount;
}
