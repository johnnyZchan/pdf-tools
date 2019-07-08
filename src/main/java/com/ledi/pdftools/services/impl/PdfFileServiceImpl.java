package com.ledi.pdftools.services.impl;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.*;
import com.ledi.pdftools.beans.PdfFileModel;
import com.ledi.pdftools.constants.CodeInfo;
import com.ledi.pdftools.entities.PdfDataCoordinateEntity;
import com.ledi.pdftools.entities.PdfFileEntity;
import com.ledi.pdftools.entities.PdfListEntity;
import com.ledi.pdftools.exceptions.ServiceException;
import com.ledi.pdftools.mappers.PdfFileMapper;
import com.ledi.pdftools.services.PdfDataCoordinateService;
import com.ledi.pdftools.services.PdfFileService;
import com.ledi.pdftools.services.PdfListService;
import com.ledi.pdftools.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service("pdfFileService")
@Slf4j
public class PdfFileServiceImpl implements PdfFileService {

    @Value("${pdf.tools.file.basedir}")
    String fileBaseDir;
    @Value("${pdf.tools.replace.rectangle.display}")
    boolean displayRectangle;

    @Resource
    PdfFileMapper pdfFileMapper;
    @Resource
    PdfDataCoordinateService pdfDataCoordinateService;
    @Resource
    PdfListService pdfListService;

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

    public PdfFileEntity getPdfFileByPdfId(String pdfId) {
        List<PdfFileEntity> list = this.pdfFileMapper.findByPdfId(pdfId);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    public PdfFileEntity getPdfFile(String pdfFileId) {
        return this.pdfFileMapper.findById(pdfFileId);
    }

    public String getPdfFilePath(String pdfFileId) {
        PdfFileEntity entity = this.getPdfFile(pdfFileId);
        if (entity != null) {
            return entity.getFilePath();
        }

        return null;
    }

    public List<PdfListEntity> uploadExcelFile(MultipartFile file) throws Exception {
        File tmpFile = null;
        Workbook wb = null;
        try {
            if (file == null) {
                return null;
            }

            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            tmpFile = new File(fileBaseDir + IDUtil.uuid() + "." + fileExtension);
            file.transferTo(tmpFile);

            List<PdfListEntity> result = null;
            //根据文件后缀（xls/xlsx）进行判断
            if ( "xls".equals(fileExtension)) {
                wb = new HSSFWorkbook(new FileInputStream(tmpFile));
            } else if ("xlsx".equals(fileExtension)) {
                wb = new XSSFWorkbook(new FileInputStream(tmpFile));
            } else {
                throw new ServiceException(CodeInfo.CODE_FILE_TYPE_ERROR);
            }

            Sheet sheet = wb.getSheetAt(0);
            int firstRowIndex = sheet.getFirstRowNum() + 2;   //前两列是列名，所以不读
            int lastRowIndex = sheet.getLastRowNum();

            result = new ArrayList<PdfListEntity>();
            PdfListEntity entity = null;
            for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex ++) {   //遍历行
                Row row = sheet.getRow(rIndex);
                if (row != null) {
                    String awb = null;
                    String awbReplace = null;
                    Double num = null;
                    Double weight = null;
                    Double declareTotalAmountUsd = null;
                    Double declareFreightAmountUsd = null;
                    Double prod1DeclareAmountUsd = null;
                    Double prod2DeclareAmountUsd = null;
                    Double prod3DeclareAmountUsd = null;

                    try {
                        // 单号
                        awb = row.getCell(0) != null ? row.getCell(0).getStringCellValue() : null;
                        // 换单号
                        awbReplace = row.getCell(1) != null ? row.getCell(1).getStringCellValue() : null;
                        // 件数
                        num = row.getCell(2) != null ? row.getCell(2).getNumericCellValue() : null;
                        // 重量
                        weight = row.getCell(3) != null ? row.getCell(3).getNumericCellValue() : null;
                        // 总申报价值USD
                        declareTotalAmountUsd = row.getCell(4) != null ? row.getCell(4).getNumericCellValue() : null;
                        // 申报运费USD
                        declareFreightAmountUsd = row.getCell(5) != null ? row.getCell(5).getNumericCellValue() : null;
                        // 品名1美⾦申报价值
                        prod1DeclareAmountUsd = row.getCell(6) != null ? row.getCell(6).getNumericCellValue() : null;
                        // 品名2美⾦申报价值
                        prod2DeclareAmountUsd = row.getCell(7) != null ? row.getCell(7).getNumericCellValue() : null;
                        // 品名3美⾦申报价值
                        prod3DeclareAmountUsd = row.getCell(8) != null ? row.getCell(8).getNumericCellValue() : null;
                    } catch (Exception e) {
                        log.warn("Excel数据格式不正确", e);
                    }

                    // 单号、总申报价值USD、申报运费USD、品名1美金申报价值是必须项
                    entity = new PdfListEntity();
                    if (StringUtils.isBlank(awb)) {
                        throw new ServiceException(CodeInfo.CODE_PARAMS_NOT_NULL, "单号");
                    }
                    entity.setAwb(awb);
                    entity.setAwbReplace(awbReplace);
                    if (num != null) {
                        entity.setNum(num.intValue());
                    }
                    if (weight != null) {
                        entity.setWeight(new BigDecimal(weight.toString()));
                    }
                    if (declareTotalAmountUsd == null) {
                        throw new ServiceException(CodeInfo.CODE_PARAMS_NOT_NULL, "总申报价值USD");
                    }
                    entity.setDeclareTotalAmountUsd(new BigDecimal(declareTotalAmountUsd.toString()));
                    if (declareFreightAmountUsd == null) {
                        throw new ServiceException(CodeInfo.CODE_PARAMS_NOT_NULL, "申报运费USD");
                    }
                    entity.setDeclareFreightAmountUsd(new BigDecimal(declareFreightAmountUsd.toString()));
                    if (prod1DeclareAmountUsd == null) {
                        throw new ServiceException(CodeInfo.CODE_PARAMS_NOT_NULL, "品名1美金申报价值");
                    }
                    entity.setProd1DeclareAmountUsd(new BigDecimal(prod1DeclareAmountUsd.toString()));
                    if (prod2DeclareAmountUsd != null) {
                        entity.setProd2DeclareAmountUsd(new BigDecimal(prod2DeclareAmountUsd.toString()));
                    }
                    if (prod3DeclareAmountUsd != null) {
                        entity.setProd3DeclareAmountUsd(new BigDecimal(prod3DeclareAmountUsd.toString()));
                    }
                    result.add(entity);
                }
            }

            return result;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("error occurred : ", e);
            throw new ServiceException("excel.file.read.error");
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception e) {}
            }
            if (tmpFile != null && tmpFile.exists()) {
                try {
                    tmpFile.delete();
                } catch (Exception e) {}
            }
        }
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
                List<PdfDataCoordinateEntity> dataCoordinateList = this.pdfDataCoordinateService.getReplacePageDataCoordinateList(page);
                if (dataCoordinateList != null && dataCoordinateList.size() > 0) {
                    for (PdfDataCoordinateEntity coordinate : dataCoordinateList) {
                        String data = this.readData(reader, page, coordinate.getLlx(), coordinate.getLly(), coordinate.getUrx(), coordinate.getUry());
                        Object convertData = null;
                        try {
                            convertData = convertData2Value(data, coordinate);
                        } catch (NumberFormatException e) {
                            convertData = null;
                        }
                        log.info("文件[" + pdfFileEntity.getPdfFileId() + "]的第[" + page + "]页，字段[" + coordinate.getFieldName() + "]=[" + data + "]，转换后数据[" + convertData + "]");
                        BeanUtil.setFieldValue(result, coordinate.getFieldName(), convertData);
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
                    if ("%".equals(entity.getSuffix())) {
                        return (new BigDecimal(bdData)).divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_UP);
                    }
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
            data = data.substring(data.indexOf(entity.getPrefix()) + 1);
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

    @Transactional
    public void deletePdfFile(String pdfId) {
        if (StringUtils.isBlank(pdfId)) {
            return;
        }

        List<PdfFileEntity> pdfFileList = this.pdfFileMapper.findByPdfId(pdfId);
        if (pdfFileList != null && pdfFileList.size() > 0) {
            for (PdfFileEntity entity : pdfFileList) {
                this.deletePdfFile(entity);
            }
        }
    }

    @Transactional
    public void deletePdfFile(PdfFileEntity pdfFileEntity) {
        if (pdfFileEntity == null) {
            return;
        }

        FileUtil.deleteFile(pdfFileEntity.getFilePath());
        this.pdfFileMapper.delete(pdfFileEntity.getPdfFileId());

    }

    @Transactional
    public void makeUpdatedFile(PdfListEntity originalPdf, PdfListEntity updatedPdf) throws ServiceException {
        try {
            if (originalPdf == null || updatedPdf == null) {
                return;
            }

            PdfFileEntity updatedPdfFile = this.getPdfFileByPdfId(updatedPdf.getPdfId());
            if (updatedPdfFile == null) {
                updatedPdfFile = new PdfFileEntity();
                updatedPdfFile.setPdfFileId(IDUtil.uuid());
                updatedPdfFile.setPdfId(updatedPdf.getPdfId());

                String fileName = updatedPdfFile.getPdfFileId() + ".pdf";
                updatedPdfFile.setFileName(fileName);
                updatedPdfFile.setFilePath(fileBaseDir + fileName);
                this.pdfFileMapper.save(updatedPdfFile);
            }

            PdfFileEntity originalPdfFile = this.getPdfFileByPdfId(originalPdf.getPdfId());
            if (originalPdfFile != null && StringUtils.isNotBlank(originalPdfFile.getFilePath())) {
                File originalFile = new File(originalPdfFile.getFilePath());
                File updatedFile = new File(updatedPdfFile.getFilePath());
                if (!updatedFile.exists()) {
                    updatedFile.createNewFile();
                }
                FileUtil.copyFile(originalFile, updatedFile);
            }
        } catch (Exception e) {
            log.error("拷贝文件失败", e);
            throw new ServiceException("file.copy.error");
        }
    }

    public void clearPdfFile(PdfFileEntity pdfFileEntity) {
        if (pdfFileEntity == null) {
            return;
        }
        File pdfFile = new File(pdfFileEntity.getFilePath());
        if (pdfFile == null || !pdfFile.exists()) {
            throw new ServiceException(CodeInfo.CODE_PDF_FILE_NOT_EXIST);
        }

        PdfReader reader = null;
        PdfStamper stamper = null;
        String outputPath = fileBaseDir + pdfFileEntity.getPdfFileId() + "_updated.pdf";
        try {
            reader = new PdfReader(pdfFileEntity.getFilePath());
            stamper = new PdfStamper(reader, new FileOutputStream(outputPath));

            for (int page = 1; page <= reader.getNumberOfPages(); page ++) {
                PdfContentByte canvas = stamper.getOverContent(page);

                List<PdfDataCoordinateEntity> dataCoordinateList = this.pdfDataCoordinateService.getDeletePageDataCoordinateList(page);
                if (dataCoordinateList != null && dataCoordinateList.size() > 0) {
                    for (PdfDataCoordinateEntity coordinate : dataCoordinateList) {
                        Rectangle rect = new Rectangle(coordinate.getLlx().floatValue(), coordinate.getLly().floatValue(), coordinate.getUrx().floatValue(), coordinate.getUry().floatValue());
                        rect.setBackgroundColor(BaseColor.WHITE);
                        canvas.rectangle(rect);
                        canvas.stroke();
                    }
                }
            }

            stamper.close();
            reader.close();
            // 将_updated.pdf文件覆盖原文件
            File tmpFile = new File(outputPath);
            if (tmpFile.exists()) {
                FileUtil.copyFile(tmpFile, pdfFile);
                FileUtil.deleteFile(tmpFile);
            }
        } catch (Exception e) {
            log.error("error occurred : ", e);
            throw new ServiceException(MessageUtil.getMessage("pdf.file.clear.error"));
        } finally {
            if (stamper != null) {
                try {
                    stamper.close();
                } catch (Exception e) {}
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {}
            }
        }
    }

    public void replacePdfFile(PdfListEntity updatedPdfEntity, PdfListEntity originalPdfEntity, PdfFileEntity pdfFileEntity) {
        if (pdfFileEntity == null || updatedPdfEntity == null || originalPdfEntity == null) {
            return;
        }
        File pdfFile = new File(pdfFileEntity.getFilePath());
        if (pdfFile == null || !pdfFile.exists()) {
            throw new ServiceException(CodeInfo.CODE_PDF_FILE_NOT_EXIST);
        }

        PdfReader reader = null;
        PdfStamper stamper = null;
        String outputPath = fileBaseDir + pdfFileEntity.getPdfFileId() + "_updated.pdf";
        try {
            reader = new PdfReader(pdfFileEntity.getFilePath());
            stamper = new PdfStamper(reader, new FileOutputStream(outputPath));

            for (int page = 1; page <= reader.getNumberOfPages(); page ++) {
                PdfContentByte canvas = stamper.getOverContent(page);

                List<PdfDataCoordinateEntity> dataCoordinateList = this.pdfDataCoordinateService.getReplacePageDataCoordinateList(page);
                if (dataCoordinateList != null && dataCoordinateList.size() > 0) {
                    for (PdfDataCoordinateEntity coordinate : dataCoordinateList) {
                        String replaceText = getCoordinateText(coordinate, updatedPdfEntity);
                        String originalText = getCoordinateText(coordinate, originalPdfEntity);
                        if (StringUtils.isBlank(replaceText)
                                || (StringUtils.isNotBlank(originalText) && replaceText.equals(originalText))) {
                            continue;
                        }

                        Rectangle rect = new Rectangle(coordinate.getLlx().floatValue(), coordinate.getLly().floatValue(), coordinate.getUrx().floatValue(), coordinate.getUry().floatValue());
                        rect.setBackgroundColor(BaseColor.WHITE);
                        if (displayRectangle) {
                            rect.setBorder(Rectangle.BOX);
                            rect.setBorderWidth(1);
                            rect.setBorderColor(BaseColor.RED);
                            rect.setUseVariableBorders(false);
                        }
                        canvas.rectangle(rect);
                        canvas.stroke();

                        canvas.beginText();
//                        BaseFont bf = BaseFont.createFont("ZS僑僔僢僋", "Identity-H", false);
                        BaseFont bf = BaseFont.createFont();
                        canvas.setFontAndSize(bf, 8.88f);
                        int align = Element.ALIGN_LEFT;
                        float x = rect.getLeft();
                        float y = rect.getBottom();
                        if (PdfDataCoordinateEntity.ALIGN_RIGHT.equals(coordinate.getAlign())) {
                            align = Element.ALIGN_RIGHT;
                            x = rect.getRight();
                            y = rect.getBottom();
                        }
                        canvas.showTextAligned(align, replaceText, x, y, 0);
                        canvas.endText();
                    }
                }
            }

            stamper.close();
            reader.close();
            // 将_updated.pdf文件覆盖原文件
            File tmpFile = new File(outputPath);
            if (tmpFile.exists()) {
                FileUtil.copyFile(tmpFile, pdfFile);
                FileUtil.deleteFile(tmpFile);
            }
        } catch (Exception e) {
            log.error("error occurred : ", e);
            throw new ServiceException(MessageUtil.getMessage("pdf.file.replace.error"));
        } finally {
            if (stamper != null) {
                try {
                    stamper.close();
                } catch (Exception e) {}
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {}
            }
        }
    }

    private String getCoordinateText(PdfDataCoordinateEntity coordinate, PdfListEntity updatedPdf) {
        String result = "";
        try {
            Object obj = BeanUtil.getFieldValue(updatedPdf, coordinate.getFieldName());
            if (obj != null) {
                if (PdfDataCoordinateEntity.DATA_TYPE_DECIMAL.equals(coordinate.getDataType())) {
                    BigDecimal bd = (BigDecimal)obj;
                    result = DataUtil.parseNumber(bd, coordinate.getDecimalDigits());
                } else {
                    result = String.valueOf(obj);
                }
            }

            if (StringUtils.isNotBlank(result)) {
                if (StringUtils.isNotBlank(coordinate.getPrefix())) {
                    if ("\\".equals(coordinate.getPrefix())) {
                        result = "¥" + result;
                    } else {
                        result = coordinate.getPrefix() + result;
                    }
                }
                if (StringUtils.isNotBlank(coordinate.getSuffix())) {
                    result = result + coordinate.getSuffix();
                }
            }

            // 如果是单号，需要判断是否用换单号替换
            if ("awb".equals(coordinate.getFieldName())) {
                if (StringUtils.isNotBlank(updatedPdf.getAwbReplace())) {
                    result = updatedPdf.getAwbReplace();
                } else {
                    result = "";
                }
            }

            return result;
        } catch (Exception e) {
            log.error("error occurred : ", e);
            return null;
        }
    }

    public List<PdfFileModel> getPdfFileList(List<String> awbList, int type) {
        if (awbList == null || awbList.isEmpty()) {
            return null;
        }

        List<PdfFileModel> result = null;
        List<PdfListEntity> pdfList = null;
        if (type == PdfListEntity.TYPE_ORIGINAL) {
            pdfList = this.pdfListService.getPdfList(awbList, type, null);
        } else if (type == PdfListEntity.TYPE_UPDATED) {
            pdfList = this.pdfListService.getPdfList(awbList, type);
        }
        if (pdfList != null && !pdfList.isEmpty()) {
            result = new ArrayList<PdfFileModel>();
            PdfFileModel model = null;
            PdfFileEntity fileEntity = null;
            for (PdfListEntity entity : pdfList) {
                fileEntity = this.getPdfFileByPdfId(entity.getPdfId());
                if (fileEntity == null) {
                    continue;
                }

                model = new PdfFileModel();
                model.setAwb(entity.getAwb());
                model.setAwbReplace(entity.getAwbReplace());
                model.setFilePath(fileEntity.getFilePath());
                result.add(model);
            }
        }

        return result;
    }
}
