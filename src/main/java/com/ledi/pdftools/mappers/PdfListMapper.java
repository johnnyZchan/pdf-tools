package com.ledi.pdftools.mappers;

import com.ledi.pdftools.entities.PdfListEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface PdfListMapper {

    public static final int DEL_STATUS_NO = 0;
    public static final int DEL_STATUS_YES = 1;

    public static final int MAKE_STATUS_NO = 0;
    public static final int MAKE_STATUS_YES = 1;

    public static final String COLUMNS = "pdf_id as pdfId, type, awb, awb_replace as awbReplace, num, weight, declare_freight_price as declareFreightPrice," +
            "declare_total_amount_usd as declareTotalAmountUsd, declare_freight_amount_usd as declareFreightAmountUsd, clearance_amount as clearanceAmount," +
            "bpr_amount as bprAmount, tariff, excise_tax as exciseTax, local_excise_tax as localExciseTax, tax_total_amount as taxTotalAmount,usd_jpy_exchange_rate as usdJpyExchangeRate," +
            "prod1_declare_amount_usd as prod1DeclareAmountUsd, prod2_declare_amount_usd as prod2DeclareAmountUsd, prod3_declare_amount_usd as prod3DeclareAmountUsd," +
            "prod1_tariff_rate as prod1TariffRate, prod2_tariff_rate as prod2TariffRate, prod3_tariff_rate as prod3TariffRate," +
            "prod1_freight_pct as prod1FreightPct, prod2_freight_pct as prod2FreightPct, prod3_freight_pct as prod3FreightPct," +
            "prod1_declare_amount_jpy as prod1DeclareAmountJpy, prod2_declare_amount_jpy as prod2DeclareAmountJpy, prod3_declare_amount_jpy as prod3DeclareAmountJpy," +
            "prod1_tariff_base as prod1TariffBase, prod2_tariff_base as prod2TariffBase, prod3_tariff_base as prod3TariffBase," +
            "prod1_tariff as prod1Tariff, prod2_tariff as prod2Tariff, prod3_tariff as prod3Tariff," +
            "prod1_tariff_rounding as prod1TariffRounding, prod2_tariff_rounding as prod2TariffRounding, prod3_tariff_rounding as prod3TariffRounding," +
            "prod1_country_excise_tax as prod1CountryExciseTax, prod2_country_excise_tax as prod2CountryExciseTax, prod3_country_excise_tax as prod3CountryExciseTax," +
            "prod1_country_excise_tax_base as prod1CountryExciseTaxBase, prod2_country_excise_tax_base as prod2CountryExciseTaxBase, prod3_country_excise_tax_base as prod3CountryExciseTaxBase," +
            "prod1_country_excise_tax_amount as prod1CountryExciseTaxAmount, prod2_country_excise_tax_amount as prod2CountryExciseTaxAmount, prod3_country_excise_tax_amount as prod3CountryExciseTaxAmount," +
            "prod1_local_excise_tax_base as prod1LocalExciseTaxBase, prod2_local_excise_tax_base as prod2LocalExciseTaxBase, prod3_local_excise_tax_base as prod3LocalExciseTaxBase," +
            "prod1_local_excise_tax_amount as prod1LocalExciseTaxAmount, prod2_local_excise_tax_amount as prod2LocalExciseTaxAmount, prod3_local_excise_tax_amount as prod3LocalExciseTaxAmount," +
            "tariff_total_amount as tariffTotalAmount, country_excise_tax_total_amount as countryExciseTaxTotalAmount, local_excise_tax_total_amount as localExciseTaxTotalAmount," +
            "make_status as makeStatus, make_time as makeTime, del_status as delStatus, create_time as createTime";

    @Select("select " + COLUMNS + " from pdf_list where pdf_id=#{id} and del_status=" + DEL_STATUS_NO)
    PdfListEntity findById(String id);

    @Select("select " + COLUMNS + " from pdf_list where del_status=" + DEL_STATUS_NO)
    List<PdfListEntity> findAll();

    @Update("update pdf_list set del_status=" + DEL_STATUS_YES + " where pdf_id=#{id}")
    void delete(String id);

    @Update("update pdf_list set type=#{type}, awb=#{awb}, awb_replace=#{awbReplace}, num=#{num}, weight=#{weight}," +
            " declare_freight_price=#{declareFreightPrice}, declare_total_amount_usd=#{declareTotalAmountUsd}, declare_freight_amount_usd=#{declareFreightAmountUsd}," +
            " clearance_amount=#{clearanceAmount}, bpr_amount=#{bprAmount}, tariff=#{tariff}, excise_tax=#{exciseTax}, local_excise_tax=#{localExciseTax}," +
            " tax_total_amount=#{taxTotalAmount}, usd_jpy_exchange_rate=#{usdJpyExchangeRate}," +
            " prod1_declare_amount_usd=#{prod1DeclareAmountUsd}, prod2_declare_amount_usd=#{prod2DeclareAmountUsd}, prod3_declare_amount_usd=#{prod3DeclareAmountUsd}," +
            " prod1_tariff_rate=#{prod1TariffRate}, prod2_tariff_rate=#{prod2TariffRate}, prod3_tariff_rate=#{prod3TariffRate}," +
            " prod1_freight_pct=#{prod1FreightPct}, prod2_freight_pct=#{prod2FreightPct}, prod3_freight_pct=#{prod3FreightPct}," +
            " prod1_declare_amount_jpy=#{prod1DeclareAmountJpy}, prod2_declare_amount_jpy=#{prod2DeclareAmountJpy}, prod3_declare_amount_jpy=#{prod3DeclareAmountJpy}," +
            " prod1_tariff_base=#{prod1TariffBase}, prod2_tariff_base=#{prod2TariffBase}, prod3_tariff_base=#{prod3TariffBase}," +
            " prod1_tariff=#{prod1Tariff}, prod2_tariff=#{prod2Tariff}, prod3_tariff=#{prod3Tariff}," +
            " prod1_tariff_rounding=#{prod1TariffRounding}, prod2_tariff_rounding=#{prod2TariffRounding}, prod3_tariff_rounding=#{prod3TariffRounding}," +
            " prod1_country_excise_tax=#{prod1CountryExciseTax}, prod2_country_excise_tax=#{prod2CountryExciseTax}, prod3_country_excise_tax=#{prod3CountryExciseTax}," +
            " prod1_country_excise_tax_base=#{prod1CountryExciseTaxBase}, prod2_country_excise_tax_base=#{prod2CountryExciseTaxBase}, prod3_country_excise_tax_base=#{prod3CountryExciseTaxBase}," +
            " prod1_country_excise_tax_amount=#{prod1CountryExciseTaxAmount}, prod2_country_excise_tax_amount=#{prod2CountryExciseTaxAmount}, prod3_country_excise_tax_amount=#{prod3CountryExciseTaxAmount}," +
            " prod1_local_excise_tax_base=#{prod1LocalExciseTaxBase}, prod2_local_excise_tax_base=#{prod2LocalExciseTaxBase}, prod3_local_excise_tax_base=#{prod3LocalExciseTaxBase}," +
            " prod1_local_excise_tax_amount=#{prod1LocalExciseTaxAmount}, prod2_local_excise_tax_amount=#{prod2LocalExciseTaxAmount}, prod3_local_excise_tax_amount=#{prod3LocalExciseTaxAmount}," +
            " tariff_total_amount=#{tariffTotalAmount}, country_excise_tax_total_amount=#{countryExciseTaxTotalAmount}, local_excise_tax_total_amount=#{localExciseTaxTotalAmount}," +
            " make_status=#{makeStatus}, make_time=#{makeTime}" +
            " where pdf_id=#{pdfId}")
    void update(PdfListEntity pdfListEntity);

    @Insert("insert into pdf_list(" + COLUMNS + ")" +
            " values(#{pdfId}, #{type}, #{awb}, #{awbReplace}, #{num}, #{weight}, #{declareFreightPrice}, #{declareTotalAmountUsd}, #{declareFreightAmountUsd}," +
            " #{clearanceAmount}, #{bprAmount}, #{tariff}, #{exciseTax}, #{localExciseTax}, #{taxTotalAmount}, #{usdJpyExchangeRate}," +
            " #{prod1DeclareAmountUsd}, #{prod2DeclareAmountUsd}, #{prod3DeclareAmountUsd}," +
            " #{prod1TariffRate}, #{prod2TariffRate}, #{prod3TariffRate}," +
            " #{prod1FreightPct}, #{prod2FreightPct}, #{prod3FreightPct}," +
            " #{prod1DeclareAmountJpy}, #{prod2DeclareAmountJpy}, #{prod3DeclareAmountJpy}," +
            " #{prod1TariffBase}, #{prod2TariffBase}, #{prod3TariffBase}," +
            " #{prod1Tariff}, #{prod2Tariff}, #{prod3Tariff}," +
            " #{prod1TariffRounding}, #{prod2TariffRounding}, #{prod3TariffRounding}," +
            " #{prod1CountryExciseTax}, #{prod2CountryExciseTax}, #{prod3CountryExciseTax}," +
            " #{prod1CountryExciseTaxBase}, #{prod2CountryExciseTaxBase}, #{prod3CountryExciseTaxBase}," +
            " #{prod1CountryExciseTaxAmount}, #{prod2CountryExciseTaxAmount}, #{prod3CountryExciseTaxAmount}," +
            " #{prod1LocalExciseTaxBase}, #{prod2LocalExciseTaxBase}, #{prod3LocalExciseTaxBase}," +
            " #{prod1LocalExciseTaxAmount}, #{prod2LocalExciseTaxAmount}, #{prod3LocalExciseTaxAmount}," +
            " #{tariffTotalAmount}, #{countryExciseTaxTotalAmount}, #{localExciseTaxTotalAmount}, #{makeStatus}, #{makeTime}, #{delStatus}, now())")
    void save(PdfListEntity pdfListEntity);

}
