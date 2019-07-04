package com.ledi.pdftools.services.impl;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.*;
import com.ledi.pdftools.constants.CodeInfo;
import com.ledi.pdftools.entities.PdfDataCoordinateEntity;
import com.ledi.pdftools.entities.PdfFileEntity;
import com.ledi.pdftools.entities.PdfListEntity;
import com.ledi.pdftools.exceptions.ServiceException;
import com.ledi.pdftools.mappers.PdfFileMapper;
import com.ledi.pdftools.services.PdfDataCoordinateService;
import com.ledi.pdftools.services.PdfFileService;
import com.ledi.pdftools.utils.BeanUtil;
import com.ledi.pdftools.utils.IDUtil;
import com.ledi.pdftools.utils.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service("pdfFileService")
@Slf4j
public class PdfFileServiceImpl implements PdfFileService {

    @Value("${pdf.tools.file.basedir}")
    String fileBaseDir;

    @Resource
    PdfFileMapper pdfFileMapper;
    @Resource
    PdfDataCoordinateService pdfDataCoordinateService;

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

    public PdfListEntity readDataFromPdfFile(PdfFileEntity pdfFileEntity) {
        if (pdfFileEntity == null) {
            return null;
        }
        File pdfFile = new File(pdfFileEntity.getFilePath());
        if (pdfFile == null || !pdfFile.exists()) {
            throw new ServiceException(CodeInfo.CODE_PDF_FILE_NOT_EXIST);
        }

        PdfReader reader = null;
        PdfListEntity result = new PdfListEntity();
        try {
            reader = new PdfReader(pdfFileEntity.getFilePath());
            for (int page = 1; page <= reader.getNumberOfPages(); page ++) {
                List<PdfDataCoordinateEntity> dataCoordinateList = this.pdfDataCoordinateService.getPageDataCoordinateList(page);
                if (dataCoordinateList != null && dataCoordinateList.size() > 0) {
                    for (PdfDataCoordinateEntity coordinate : dataCoordinateList) {
                        String data = this.readData(reader, page, coordinate.getLlx(), coordinate.getLly(), coordinate.getUrx(), coordinate.getUry());
                        BeanUtil.setFieldValue(result, coordinate.getKey(), convertData2Value(data, coordinate));
                    }
                }
            }
        } catch (Exception e) {
            log.error("error occurred : ", e);
            throw new ServiceException(MessageUtil.getMessage("pdf.file.read.error"));
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {}
            }
        }

        return result;
    }

    public String readData(PdfReader reader, int page, BigDecimal llx, BigDecimal lly, BigDecimal urx, BigDecimal ury) throws IOException {
        if (reader == null || llx == null || lly == null || urx == null || ury == null) {
            return null;
        }

        Rectangle rect = new Rectangle(llx.floatValue(), lly.floatValue(), urx.floatValue(), ury.floatValue());
        RenderFilter filter = new RegionTextRenderFilter(rect);
        TextExtractionStrategy strategy = new FilteredTextRenderListener(new LocationTextExtractionStrategy(), filter);
        String result = PdfTextExtractor.getTextFromPage(reader, page, strategy);
        return result;
    }

    public Object convertData2Value(String data, PdfDataCoordinateEntity entity) {
        if (data == null || entity == null) {
            return null;
        }
        if (StringUtils.isNotBlank(entity.getDataType())) {
            if (PdfDataCoordinateEntity.DATA_TYPE_STRING.equals(entity.getDataType())) {
                return data;
            } else if (PdfDataCoordinateEntity.DATA_TYPE_INTEGER.equals(entity.getDataType())) {
                String iData = formatData(data, entity);
                if (StringUtils.isNotBlank(iData)) {
                    return Integer.parseInt(iData);
                }
                return null;
            } else if (PdfDataCoordinateEntity.DATA_TYPE_DECIMAL.equals(entity.getDataType())) {
                String bdData = formatData(data, entity);
                if (StringUtils.isNotBlank(bdData)) {
                    return new BigDecimal(bdData);
                }
                return null;
            }
        }

        return data;
    }

    public String formatData(String data, PdfDataCoordinateEntity entity) {
        if (data == null || entity == null) {
            return null;
        }

        data = data.trim();
        // 去掉前缀
        if (StringUtils.isNotBlank(entity.getPrefix()) && data.startsWith(entity.getPrefix())) {
            data = data.substring(data.indexOf(entity.getPrefix()));
        }
        // 去掉后缀
        if (StringUtils.isNotBlank(entity.getSuffix()) && data.endsWith(entity.getSuffix())) {
            data = data.substring(0, data.lastIndexOf(entity.getSuffix()));
        }
        if (data.contains(",")) {
            data = data.replaceAll(",", "");
        }

        return data;
    }
}
