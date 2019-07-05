package com.ledi.pdftools.services;

import com.ledi.pdftools.entities.PdfFileEntity;
import com.ledi.pdftools.entities.PdfListEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PdfFileService {

    public PdfFileEntity uploadPdfFile(MultipartFile file) throws Exception;

    public PdfListEntity readDataFromPdfFile(PdfFileEntity pdfFileEntity);

    public List<PdfListEntity> uploadExcelFile(MultipartFile file) throws Exception;

    public void deletePdfFile(String pdfId);
}
