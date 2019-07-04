package com.ledi.pdftools.services;

import com.ledi.pdftools.entities.PdfFileEntity;
import com.ledi.pdftools.entities.PdfListEntity;
import org.springframework.web.multipart.MultipartFile;

public interface PdfFileService {

    public PdfFileEntity uploadPdfFile(MultipartFile file) throws Exception;

    public PdfListEntity readDataFromPdfFile(PdfFileEntity pdfFileEntity);
}
