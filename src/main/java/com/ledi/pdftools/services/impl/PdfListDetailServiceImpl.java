package com.ledi.pdftools.services.impl;

import com.ledi.pdftools.entities.PdfListDetailEntity;
import com.ledi.pdftools.mappers.PdfListDetailMapper;
import com.ledi.pdftools.services.PdfListDetailService;
import com.ledi.pdftools.utils.IDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void deletePdfDetailByPdfId(String pdfId) {
        this.pdfListDetailMapper.deleteByPdfId(pdfId);
    }

    @Transactional
    public void save(PdfListDetailEntity pdfListDetailEntity) {
        if (pdfListDetailEntity == null) {
            return;
        }

        if (this.isDetailEntityEmpty(pdfListDetailEntity)) {
            return;
        }

        if (StringUtils.isBlank(pdfListDetailEntity.getPdfDetailId())) {
            pdfListDetailEntity.setPdfDetailId(IDUtil.uuid());
        }

        this.pdfListDetailMapper.save(pdfListDetailEntity);
    }

    public boolean isDetailEntityEmpty(PdfListDetailEntity entity) {
        if (entity == null) {
            return true;
        }

        if (entity.getDeclareAmountUsd() == null
                && entity.getTariffRate() == null
                && entity.getFreightPct() == null
                && entity.getDeclareAmountJpy() == null
                && entity.getTariffBase() == null
                && entity.getTariff() == null
                && entity.getTariffRounding() == null
                && entity.getCountryExciseTax() == null
                && entity.getCountryExciseTaxBase() == null
                && entity.getCountryExciseTaxAmount() == null
                && entity.getLocalExciseTaxBase() == null
                && entity.getLocalExciseTaxAmount() == null) {
            return true;
        }

        return false;
    }
}
