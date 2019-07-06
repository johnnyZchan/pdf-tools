package com.ledi.pdftools.services;

import com.ledi.pdftools.beans.PdfListModel;
import com.ledi.pdftools.entities.PdfListEntity;

import java.util.List;

public interface PdfListService {

    public int getPdfListCount();
    public List<PdfListEntity> getPdfList(Integer start, Integer length);

    public List<PdfListModel> getPdfModelList(Integer start, Integer length);

    public void addPdf(String pdfFileId, boolean coverFlg);

    public void delPdf(List<String> awbList);

    public List<String> makePdf(List<String> awbList);

    public void addUpdatedData(List<PdfListEntity> pdfList, boolean coverFlg);
}
