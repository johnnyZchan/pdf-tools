package com.ledi.pdftools.beans;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PdfListModel {

    private PdfModel originalPdf;
    private PdfModel updatedPdf;
    private PdfModel comparePdf;

    private List<PdfListDetailModel> detailList;
}
