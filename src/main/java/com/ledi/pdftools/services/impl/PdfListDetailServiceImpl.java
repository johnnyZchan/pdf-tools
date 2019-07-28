package com.ledi.pdftools.services.impl;

import com.ledi.pdftools.entities.PdfListDetailEntity;
import com.ledi.pdftools.mappers.PdfListDetailMapper;
import com.ledi.pdftools.services.PdfListDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("pdfListDetailService")
@Slf4j
public class PdfListDetailServiceImpl implements PdfListDetailService {

    @Resource
    PdfListDetailMapper pdfListDetailMapper;

    public List<PdfListDetailEntity> getPdfListDetail(String pdfId) {
        return this.pdfListDetailMapper.findByPdfId(pdfId);
    }


}
