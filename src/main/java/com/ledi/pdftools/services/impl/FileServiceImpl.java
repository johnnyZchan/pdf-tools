package com.ledi.pdftools.services.impl;

import com.ledi.pdftools.entities.PdfFileEntity;
import com.ledi.pdftools.mappers.PdfFileMapper;
import com.ledi.pdftools.services.FileService;
import com.ledi.pdftools.utils.IDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;

@Service("fileService")
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${pdf.tools.file.basedir}")
    String fileBaseDir;

    @Resource
    PdfFileMapper pdfFileMapper;

    @Transactional
    public PdfFileEntity uploadPdfFile(MultipartFile file) throws Exception {
        if (file == null) {
            return null;
        }

        String pdfFileId = IDUtil.uuid();
        String filePath = fileBaseDir + pdfFileId + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        file.transferTo(new File(filePath));

        PdfFileEntity entity = new PdfFileEntity();
        entity.setPdfFileId(pdfFileId);
        entity.setFileName(file.getOriginalFilename());
        entity.setFilePath(filePath);
        this.pdfFileMapper.save(entity);

        return entity;
    }
}
