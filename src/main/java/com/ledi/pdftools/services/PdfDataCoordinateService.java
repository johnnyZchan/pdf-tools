package com.ledi.pdftools.services;

import com.ledi.pdftools.entities.PdfDataCoordinateEntity;

import java.util.List;

public interface PdfDataCoordinateService {

    public List<PdfDataCoordinateEntity> getPageDataCoordinateList(int pageNo);
}
