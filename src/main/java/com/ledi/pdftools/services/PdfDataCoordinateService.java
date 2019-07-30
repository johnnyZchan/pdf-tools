package com.ledi.pdftools.services;

import com.itextpdf.text.Rectangle;
import com.ledi.pdftools.beans.RectModel;
import com.ledi.pdftools.entities.PdfDataCoordinateEntity;

import java.util.List;

public interface PdfDataCoordinateService {

    public List<PdfDataCoordinateEntity> getDeletePageDataCoordinateList(String templateName, int pageNo);

    public List<PdfDataCoordinateEntity> getReplacePageDataCoordinateList(String templateName, int pageNo);

    public List<PdfDataCoordinateEntity> getPageDataCoordinateList(String templateName, int pageNo, int actionType);

    public RectModel createRect(Double left, Double top, Double width, Double height);
}
