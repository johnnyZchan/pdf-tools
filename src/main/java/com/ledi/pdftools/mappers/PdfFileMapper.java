package com.ledi.pdftools.mappers;

import com.ledi.pdftools.entities.PdfFileEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface PdfFileMapper {

    public static final String COLUMNS = "pdf_file_id as pdfFileId, pdf_id as pdfId, file_name as fileName, file_path as filePath, file_url as fileUrl";

    @Select("select " + COLUMNS + " from pdf_file where pdf_id=#{pdfId}")
    List<PdfFileEntity> findByPdfId(String pdfId);

    @Delete("delete from pdf_file where pdf_file_id=#{id}")
    void delete(String id);

    @Update("update pdf_file set pdf_id=#{pdfId}, file_name=#{fileName}, file_path=#{filePath}, file_url=#{fileUrl}" +
            " where pdf_file_id=#{pdfFileId}")
    void update(PdfFileEntity pdfFileEntity);

    @Insert("insert into pdf_file(" + COLUMNS + ")" +
            " values(#{pdfFileId}, #{pdfId}, #{fileName}, #{filePath}, #{fileUrl})")
    void save(PdfFileEntity pdfFileEntity);

}
