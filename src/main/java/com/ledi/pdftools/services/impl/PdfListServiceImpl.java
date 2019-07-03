package com.ledi.pdftools.services.impl;

import com.ledi.pdftools.beans.PdfListModel;
import com.ledi.pdftools.beans.PdfModel;
import com.ledi.pdftools.constants.CodeInfo;
import com.ledi.pdftools.entities.PdfFileEntity;
import com.ledi.pdftools.entities.PdfListEntity;
import com.ledi.pdftools.exceptions.ServiceException;
import com.ledi.pdftools.mappers.PdfFileMapper;
import com.ledi.pdftools.mappers.PdfListMapper;
import com.ledi.pdftools.services.PdfListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("pdfListService")
@Slf4j
public class PdfListServiceImpl implements PdfListService {

    @Resource
    PdfListMapper pdfListMapper;
    @Resource
    PdfFileMapper pdfFileMapper;

    public int getPdfListCount() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", PdfListEntity.TYPE_ORIGINAL);
        return pdfListMapper.countByCondition(params);
    }

    public List<PdfListEntity> getPdfList(Integer start, Integer length) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", PdfListEntity.TYPE_ORIGINAL);

        String orderSql = "create_time desc";
        return pdfListMapper.findByCondition(params, orderSql, start, length);
    }

    public List<PdfListModel> getPdfModelList(Integer start, Integer length) {
        List<PdfListModel> result = null;
        List<PdfListEntity> dataList = this.getPdfList(start, length);
        if (dataList != null) {
            result = new ArrayList<PdfListModel>();
            for (PdfListEntity entity : dataList) {
                result.add(this.convertEntity2PdfListModel(entity));
            }
        }

        return result;
    }

    @Transactional
    public void addPdf(String pdfFileId) {
        PdfFileEntity pdfFileEntity = this.pdfFileMapper.findById(pdfFileId);
        if (pdfFileEntity == null) {
            throw new ServiceException(CodeInfo.CODE_PDF_FILE_NOT_EXIST);
        }


    }

    public PdfListModel convertEntity2PdfListModel(PdfListEntity originalEntity) {
        if (originalEntity == null) {
            return null;
        }

        PdfModel originalModel = this.convertEntity2PdfModel(originalEntity);
        PdfListEntity updatedEntity = this.pdfListMapper.findUpdatedPdfByAwb(originalEntity.getAwb());
        PdfModel updatedModel = null;
        if (updatedEntity != null) {
            updatedModel = this.convertEntity2PdfModel(updatedEntity);
        }

        PdfListModel result = new PdfListModel();
        result.setOriginalPdf(originalModel);
        result.setUpdatedPdf(updatedModel);
        result.setComparePdf(this.createComparePdfModel(originalModel, updatedModel));

        return result;
    }

    public PdfModel convertEntity2PdfModel(PdfListEntity entity) {
        if (entity == null) {
            return null;
        }

        PdfModel model = new PdfModel();
        model.setType(entity.getType());
        model.setAwb(entity.getAwb());
        model.setAwbReplace(entity.getAwbReplace());
        model.setNum(entity.getNum());
        model.setWeight(entity.getWeight());
        model.setDeclareTotalAmountUsd(entity.getDeclareTotalAmountUsd());
        model.setDeclareFreightAmountUsd(entity.getDeclareFreightAmountUsd());
        model.setClearanceAmount(entity.getClearanceAmount());
        model.setBprAmount(entity.getBprAmount());
        model.setTariff(entity.getTariff());
        model.setExciseTax(entity.getExciseTax());
        model.setLocalExciseTax(entity.getLocalExciseTax());
        model.setTaxTotalAmount(entity.getTaxTotalAmount());
        model.setUsdJpyExchangeRate(entity.getUsdJpyExchangeRate());
        model.setProd1DeclareAmountUsd(entity.getProd1DeclareAmountUsd());
        model.setProd2DeclareAmountUsd(entity.getProd2DeclareAmountUsd());
        model.setProd3DeclareAmountUsd(entity.getProd3DeclareAmountUsd());
        model.setProd1TariffRate(entity.getProd1TariffRate());
        model.setProd2TariffRate(entity.getProd2TariffRate());
        model.setProd3TariffRate(entity.getProd3TariffRate());
        model.setProd1FreightPct(entity.getProd1FreightPct());
        model.setProd2FreightPct(entity.getProd2FreightPct());
        model.setProd3FreightPct(entity.getProd3FreightPct());
        model.setProd1DeclareAmountJpy(entity.getProd1DeclareAmountJpy());
        model.setProd2DeclareAmountJpy(entity.getProd2DeclareAmountJpy());
        model.setProd3DeclareAmountJpy(entity.getProd3DeclareAmountJpy());
        model.setProd1TariffBase(entity.getProd1TariffBase());
        model.setProd2TariffBase(entity.getProd2TariffBase());
        model.setProd3TariffBase(entity.getProd3TariffBase());
        model.setProd1Tariff(entity.getProd1Tariff());
        model.setProd2Tariff(entity.getProd2Tariff());
        model.setProd3Tariff(entity.getProd3Tariff());
        model.setProd1TariffRounding(entity.getProd1TariffRounding());
        model.setProd2TariffRounding(entity.getProd2TariffRounding());
        model.setProd3TariffRounding(entity.getProd3TariffRounding());
        model.setProd1CountryExciseTax(entity.getProd1CountryExciseTax());
        model.setProd2CountryExciseTax(entity.getProd2CountryExciseTax());
        model.setProd3CountryExciseTax(entity.getProd3CountryExciseTax());
        model.setProd1CountryExciseTaxBase(entity.getProd1CountryExciseTaxBase());
        model.setProd2CountryExciseTaxBase(entity.getProd2CountryExciseTaxBase());
        model.setProd3CountryExciseTaxBase(entity.getProd3CountryExciseTaxBase());
        model.setProd1CountryExciseTaxAmount(entity.getProd1CountryExciseTaxAmount());
        model.setProd2CountryExciseTaxAmount(entity.getProd2CountryExciseTaxAmount());
        model.setProd3CountryExciseTaxAmount(entity.getProd3CountryExciseTaxAmount());
        model.setProd1LocalExciseTaxBase(entity.getProd1LocalExciseTaxBase());
        model.setProd2LocalExciseTaxBase(entity.getProd2LocalExciseTaxBase());
        model.setProd3LocalExciseTaxBase(entity.getProd3LocalExciseTaxBase());
        model.setProd1LocalExciseTaxAmount(entity.getProd1LocalExciseTaxAmount());
        model.setProd2LocalExciseTaxAmount(entity.getProd2LocalExciseTaxAmount());
        model.setProd3LocalExciseTaxAmount(entity.getProd3LocalExciseTaxAmount());
        model.setTariffTotalAmount(entity.getTariffTotalAmount());
        model.setCountryExciseTaxTotalAmount(entity.getCountryExciseTaxTotalAmount());
        model.setLocalExciseTaxTotalAmount(entity.getLocalExciseTaxTotalAmount());
        model.setMakeStatus(entity.getMakeStatus());
        model.setMakeTime(entity.getMakeTime());

        return model;
    }

    public PdfModel createComparePdfModel(PdfModel originalPdf, PdfModel updatedPdf) {
        if (originalPdf == null || updatedPdf == null) {
            return null;
        }

        PdfModel result = new PdfModel();
        if (updatedPdf.getWeight() != null && originalPdf.getWeight() != null) {
            result.setWeight(updatedPdf.getWeight().subtract(originalPdf.getWeight()));
        }
        if (updatedPdf.getDeclareTotalAmountUsd() != null && originalPdf.getDeclareTotalAmountUsd() != null) {
            result.setDeclareTotalAmountUsd(updatedPdf.getDeclareTotalAmountUsd().subtract(originalPdf.getDeclareFreightAmountUsd()));
        }
        if (updatedPdf.getDeclareFreightAmountUsd() != null && originalPdf.getDeclareFreightAmountUsd() != null) {
            result.setDeclareFreightAmountUsd(updatedPdf.getDeclareFreightAmountUsd().subtract(originalPdf.getDeclareFreightAmountUsd()));
        }
        if (updatedPdf.getClearanceAmount() != null && originalPdf.getClearanceAmount() != null) {
            result.setClearanceAmount(updatedPdf.getClearanceAmount().subtract(originalPdf.getClearanceAmount()));
        }
        if (updatedPdf.getTaxTotalAmount() != null && originalPdf.getTaxTotalAmount() != null) {
            result.setTaxTotalAmount(updatedPdf.getTaxTotalAmount().subtract(originalPdf.getTaxTotalAmount()));
        }

        return result;
    }

}
