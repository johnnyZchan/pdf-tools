package com.ledi.pdftools.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PdfListDetailModel {

    private String prodNo;

    private PdfDetailModel originalPdfDetail;
    private PdfDetailModel updatedPdfDetail;
}
