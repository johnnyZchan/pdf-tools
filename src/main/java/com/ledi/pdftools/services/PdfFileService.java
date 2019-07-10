package com.ledi.pdftools.services;

import com.ledi.pdftools.beans.PdfFileModel;
import com.ledi.pdftools.entities.PdfFileEntity;
import com.ledi.pdftools.entities.PdfListEntity;
import com.ledi.pdftools.exceptions.ServiceException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface PdfFileService {

    public PdfFileEntity uploadPdfFile(MultipartFile file) throws Exception;

    public PdfFileEntity uploadPdfFile(File file);

    public PdfFileEntity getPdfFileByPdfId(String pdfId);

    public PdfListEntity readDataFromPdfFile(PdfFileEntity pdfFileEntity);

    public List<PdfListEntity> uploadExcelFile(MultipartFile file) throws Exception;

    public void deletePdfFile(String pdfId);

    public void deletePdfFile(PdfFileEntity pdfFileEntity);

    public void deletePdfFileById(String id);

    public void makeUpdatedFile(PdfListEntity originalPdf, PdfListEntity updatedPdf) throws ServiceException;

    public void clearPdfFile(PdfFileEntity pdfFileEntity);

    public void replacePdfFile(PdfListEntity updatedPdfEntity, PdfListEntity originalPdfEntity, PdfFileEntity pdfFileEntity);

    public String getPdfFilePath(String pdfFileId);

    public List<PdfFileModel> getPdfFileList(List<String> awbList, int type);

    public String getFileBaseDir();
}
