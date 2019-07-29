package com.ledi.pdftools.mappers;

import com.ledi.pdftools.entities.PdfListDetailEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface PdfListDetailMapper {

    public static final String COLUMNS = "pdf_detail_id as pdfDetailId, pdf_id as pdfId, prod_no as prodNo, declare_amount_usd as declareAmountUsd," +
            "tariff_rate as tariffRate, freight_pct as freightPct, declare_amount_jpy as declareAmountJpy, tariff_base as tariffBase, tariff," +
            "tariff_rounding as tariffRounding, country_excise_tax as countryExciseTax, country_excise_tax_base as countryExciseTaxBase," +
            "country_excise_tax_amount as countryExciseTaxAmount, local_excise_tax_base as localExciseTaxBase, local_excise_tax_amount as localExciseTaxAmount";

    public static final String ADD_COLUMNS = "pdf_detail_id, pdf_id, prod_no, declare_amount_usd, tariff_rate, freight_pct, declare_amount_jpy, tariff_base, tariff," +
            " tariff_rounding, country_excise_tax, country_excise_tax_base, country_excise_tax_amount, local_excise_tax_base, local_excise_tax_amount";

    @Select("select " + COLUMNS + " from pdf_list_detail where pdf_detail_id=#{id}")
    PdfListDetailEntity findById(String id);

    @Select("select " + COLUMNS + " from pdf_list_detail")
    List<PdfListDetailEntity> findAll();

    @Select("select " + COLUMNS + " from pdf_list_detail where pdf_id=#{pdfId} order by prod_no")
    List<PdfListDetailEntity> findByPdfId(String pdfId);

    @Select("select " + COLUMNS + " from pdf_list_detail where pdf_id=#{pdfId} and prod_no=#{prodNo} limit 1")
    PdfListDetailEntity findByPdfIdAndProdNo(String pdfId, String prodNo);

    @Delete("delete from pdf_list_detail where pdf_detail_id=#{id}")
    void delete(String id);

    @Delete("delete from pdf_list_detail where pdf_id=#{pdfId}")
    void deleteByPdfId(String pdfId);

    @Update("update pdf_list_detail set pdf_id=#{pdfId}, prod_no=#{prodNo}, declare_amount_usd=#{declareAmountUsd}, tariff_rate=#{tariffRate}, freight_pct=#{freightPct}, declare_amount_jpy=#{declareAmountJpy}," +
            " tariff_base=#{tariffBase}, tariff=#{tariff}, tariff_rounding=#{tariffRounding}, country_excise_tax=#{countryExciseTax}, country_excise_tax_base=#{countryExciseTaxBase}, country_excise_tax_amount=#{countryExciseTaxAmount}," +
            " localExciseTaxBase=#{local_excise_tax_base}, local_excise_tax_amount=#{localExciseTaxAmount} where pdf_detail_id=#{pdfDetailId}")
    void update(PdfListDetailEntity pdfListDetailEntity);

    @Insert("insert into pdf_list_detail(" + ADD_COLUMNS + ")" +
            " values(#{pdfDetailId}, #{pdfId}, #{prodNo}, #{declareAmountUsd}, #{tariffRate}, #{freightPct}, #{declareAmountJpy}, #{tariffBase}, #{tariff}, #{tariffRounding}," +
            " #{countryExciseTax}, #{countryExciseTaxBase}, #{countryExciseTaxAmount}, #{localExciseTaxBase}, #{localExciseTaxAmount})")
    void save(PdfListDetailEntity pdfListDetailEntity);


}
