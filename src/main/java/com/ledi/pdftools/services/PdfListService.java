package com.ledi.pdftools.services;

import com.ledi.pdftools.beans.PdfListModel;
import com.ledi.pdftools.beans.SkipModel;
import com.ledi.pdftools.beans.ank.ShipModel;
import com.ledi.pdftools.entities.PdfListEntity;

import java.util.List;

public interface PdfListService {

    public int getPdfListCount(String awb, Integer makeStatus, String makeStartTime, String makeEndTime, String permissionStartTime, String permissionEndTime, String importer);
    public List<PdfListEntity> getPdfList(String awb, Integer makeStatus, String makeStartTime, String makeEndTime, String permissionStartTime, String permissionEndTime, String importer, Integer start, Integer length);
    public List<PdfListEntity> getPdfList(List<String> awbList, int type);
    public List<PdfListEntity> getPdfList(List<String> awbList, int type, Integer makeStatus);

    public List<PdfListModel> getPdfModelList(String awb, Integer makeStatus, String makeStartTime, String makeEndTime, String permissionStartTime, String permissionEndTime, String importer, Integer start, Integer length);

    public void addPdf(String pdfFileId, boolean coverFlg);
    public List<SkipModel> addPdf(List<String> pdfFileIdList, boolean coverFlg);
    public void addPdf(String pdfFileId, ShipModel shipModel, boolean coverFlg);
    public boolean isAwbExist(String awb);

    public void delPdf(List<String> awbList);

    public List<String> makePdf(List<String> awbList, boolean isDelete, boolean isReplace);

    public void addUpdatedData(List<PdfListEntity> pdfList, boolean coverFlg);
}
