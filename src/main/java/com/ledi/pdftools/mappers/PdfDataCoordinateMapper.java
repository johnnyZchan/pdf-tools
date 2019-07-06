package com.ledi.pdftools.mappers;

import com.ledi.pdftools.entities.PdfDataCoordinateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface PdfDataCoordinateMapper {

    public static final String COLUMNS = "id, page_no as pageNo, field_name as fieldName, llx, lly, urx, ury, margin_left as marginLeft, margin_top as marginTop, width, height, align, data_type as dataType, decimal_digits as decimalDigits, prefix, suffix, action_type as actionType";

    @Select("select " + COLUMNS + " from pdf_data_coordinate where id=#{id}")
    PdfDataCoordinateEntity findById(String id);

    @Select("select " + COLUMNS + " from pdf_data_coordinate")
    List<PdfDataCoordinateEntity> findAll();

    @Select("select " + COLUMNS + " from pdf_data_coordinate where page_no=#{pageNo} and action_type=#{actionType}")
    List<PdfDataCoordinateEntity> findByPageAndAction(int pageNo, int actionType);

    @Update("update pdf_data_coordinate set page_no=#{pageNo}, field_name=#{fieldName}, llx=#{llx}, lly=#{lly}, urx=#{urx}, ury=#{ury}," +
            " margin_left=#{marginLeft}, margin_top=#{marginTop}, width=#{width}, height=#{height}, align=#{align}, data_type=#{dataType}, decimal_digits=#{decimalDigits}," +
            " prefix=#{prefix}, suffix=#{suffix}" +
            " where id=#{id}")
    void update(PdfDataCoordinateEntity pdfDataCoordinateEntity);
}
