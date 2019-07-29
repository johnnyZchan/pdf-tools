package com.ledi.pdftools.services;

import com.ledi.pdftools.entities.PdfListDetailEntity;

import java.util.List;

public interface PdfListDetailService {

    public List<PdfListDetailEntity> getPdfListDetail(String pdfId);

    public void deletePdfDetailByPdfId(String pdfId);

    public void save(PdfListDetailEntity pdfListDetailEntity);
}
