package com.ledi.pdftools.beans;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PdfDetailModel {

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
