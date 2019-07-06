package com.ledi.pdftools.services;

import com.ledi.pdftools.entities.PdfDataCoordinateEntity;

import java.util.List;

public interface PdfDataCoordinateService {

    public List<PdfDataCoordinateEntity> getDeletePageDataCoordinateList(int pageNo);

    public List<PdfDataCoordinateEntity> getReplacePageDataCoordinateList(int pageNo);

    public List<PdfDataCoordinateEntity> getPageDataCoordinateList(int pageNo, int actionType);
}
