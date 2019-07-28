package com.ledi.pdftools.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PdfListDetailModel {

    private PdfDetailModel originalPdfDetail;
    private PdfDetailModel updatedPdfDetail;
    private PdfDetailModel comparePdfDetail;
}
