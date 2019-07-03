package com.ledi.pdftools.services;

import com.ledi.pdftools.entities.PdfFileEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    public PdfFileEntity uploadPdfFile(MultipartFile file) throws Exception;
}
