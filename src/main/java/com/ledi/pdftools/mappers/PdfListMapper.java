package com.ledi.pdftools.mappers;

import com.ledi.pdftools.entities.PdfListEntity;
import com.ledi.pdftools.mappers.provider.PdfListProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface PdfListMapper {

    public static final int DEL_STATUS_NO = 0;
    public static final int DEL_STATUS_YES = 1;

    public static final int MAKE_STATUS_NO = 0;
    public static final int MAKE_STATUS_YES = 1;

    public static final String COLUMNS = "pdf_id as pdfId, type, awb, awb_replace as awbReplace, num, weight," +
            "declare_total_amount_usd as declareTotalAmountUsd, declare_freight_amount_usd as declareFreightAmountUsd, declare_freight_amount_unit as declareFreightAmountUnit, clearance_amount as clearanceAmount," +
            "bpr_amount as bprAmount, tariff, excise_tax as exciseTax, local_excise_tax as localExciseTax, tax_total_amount as taxTotalAmount,usd_jpy_exchange_rate as usdJpyExchangeRate," +
            "tariff_total_amount as tariffTotalAmount, country_excise_tax_total_amount as countryExciseTaxTotalAmount, local_excise_tax_total_amount as localExciseTaxTotalAmount," +
            "make_status as makeStatus, make_time as makeTime, del_status as delStatus, create_time as createTime, permission_time as permissionTime, importer";

    public static final String ADD_COLUMNS = "pdf_id, type, awb, awb_replace, num, weight," +
            "declare_total_amount_usd, declare_freight_amount_usd, declare_freight_amount_unit, clearance_amount," +
            "bpr_amount, tariff, excise_tax, local_excise_tax, tax_total_amount,usd_jpy_exchange_rate," +
            "tariff_total_amount, country_excise_tax_total_amount, local_excise_tax_total_amount," +
            "make_status, make_time, del_status, create_time, permission_time, importer";

    @Select("select " + COLUMNS + " from pdf_list where pdf_id=#{id} and del_status=" + DEL_STATUS_NO)
    PdfListEntity findById(String id);

    @Select("select " + COLUMNS + " from pdf_list where del_status=" + DEL_STATUS_NO)
    List<PdfListEntity> findAll();

    @Select("select " + COLUMNS + " from pdf_list where del_status=" + DEL_STATUS_NO + " and awb=#{awb}")
    List<PdfListEntity> findByAwb(String awb);

    @SelectProvider(type = PdfListProvider.class, method = "findByCondition")
    List<PdfListEntity> findByCondition(Map<String, Object> conditions, String orderSql, Integer start, Integer length);

    @Select("select " + COLUMNS + " from pdf_list where del_status=" + DEL_STATUS_NO + " and type=#{type} and awb=#{awb} limit 1")
    PdfListEntity findPdfByAwb(int type, String awb);

    @SelectProvider(type = PdfListProvider.class, method = "countByCondition")
    int countByCondition(Map<String, Object> conditions);

    @Update("update pdf_list set del_status=" + DEL_STATUS_YES + " where pdf_id=#{id}")
    void delete(String id);

    @Update("update pdf_list set type=#{type}, awb=#{awb}, awb_replace=#{awbReplace}, num=#{num}, weight=#{weight}," +
            " declare_total_amount_usd=#{declareTotalAmountUsd}, declare_freight_amount_usd=#{declareFreightAmountUsd}, declare_freight_amount_unit=#{declareFreightAmountUnit}," +
            " clearance_amount=#{clearanceAmount}, bpr_amount=#{bprAmount}, tariff=#{tariff}, excise_tax=#{exciseTax}, local_excise_tax=#{localExciseTax}," +
            " tax_total_amount=#{taxTotalAmount}, usd_jpy_exchange_rate=#{usdJpyExchangeRate}," +
            " tariff_total_amount=#{tariffTotalAmount}, country_excise_tax_total_amount=#{countryExciseTaxTotalAmount}, local_excise_tax_total_amount=#{localExciseTaxTotalAmount}," +
            " make_status=#{makeStatus}, make_time=#{makeTime}, permission_time=#{permissionTime}, importer=#{importer}" +
            " where pdf_id=#{pdfId}")
    void update(PdfListEntity pdfListEntity);

    @Insert("insert into pdf_list(" + ADD_COLUMNS + ")" +
            " values(#{pdfId}, #{type}, #{awb}, #{awbReplace}, #{num}, #{weight}, #{declareTotalAmountUsd}, #{declareFreightAmountUsd}, #{declareFreightAmountUnit}," +
            " #{clearanceAmount}, #{bprAmount}, #{tariff}, #{exciseTax}, #{localExciseTax}, #{taxTotalAmount}, #{usdJpyExchangeRate}," +
            " #{tariffTotalAmount}, #{countryExciseTaxTotalAmount}, #{localExciseTaxTotalAmount}, #{makeStatus}, #{makeTime}, #{delStatus}, now(), #{permissionTime}, #{importer})")
    void save(PdfListEntity pdfListEntity);

}
