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

    public static final String COLUMNS = "id, page, key, llx, lly, urx, ury, left, top, width, height, align, data_type as dataType, decimal_digits as decimalDigits, prefix, suffix";

    @Select("select " + COLUMNS + " from pdf_data_coordinate where id=#{id}")
    PdfDataCoordinateEntity findById(String id);

    @Select("select " + COLUMNS + " from pdf_data_coordinate")
    List<PdfDataCoordinateEntity> findAll();

    @Select("select " + COLUMNS + " from pdf_data_coordinate where page=#{page}")
    List<PdfDataCoordinateEntity> findByPage(int page);

    @Update("update pdf_data_coordinate set page=#{page}, key=#{key}, llx=#{llx}, lly=#{lly}, urx=#{urx}, ury=#{ury}," +
            " left=#{left}, top=#{top}, width=#{width}, height=#{height}, align=#{align}, data_type=#{dataType}, decimal_digits=#{decimalDigits}" +
            " prefix=#{prefix}, suffix=#{suffix}" +
            " where id=#{id}")
    void update(PdfDataCoordinateEntity pdfDataCoordinateEntity);
}
