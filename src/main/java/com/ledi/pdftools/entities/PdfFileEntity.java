package com.ledi.pdftools.entities;

import lombok.Data;

@Data
public class PdfFileEntity {

    private String pdfFileId;
    private String pdfId;
    private String fileName;
    private String filePath;
    private String fileUrl;
}
