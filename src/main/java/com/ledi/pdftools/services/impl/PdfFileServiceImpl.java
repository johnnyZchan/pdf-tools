package com.ledi.pdftools.services.impl;

import com.aspose.pdf.*;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.*;
import com.ledi.pdftools.beans.*;
import com.ledi.pdftools.constants.CodeInfo;
import com.ledi.pdftools.entities.PdfDataCoordinateEntity;
import com.ledi.pdftools.entities.PdfFileEntity;
import com.ledi.pdftools.entities.PdfListEntity;
import com.ledi.pdftools.exceptions.ServiceException;
import com.ledi.pdftools.mappers.PdfFileMapper;
import com.ledi.pdftools.services.PdfDataCoordinateService;
import com.ledi.pdftools.services.PdfFileService;
import com.ledi.pdftools.services.PdfListDetailService;
import com.ledi.pdftools.services.PdfListService;
import com.ledi.pdftools.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("pdfFileService")
@Slf4j
public class PdfFileServiceImpl implements PdfFileService {

    @Value("${pdf.tools.file.basedir}")
    String fileBaseDir;
    @Value("${pdf.tools.replace.rectangle.display}")
    boolean displayRectangle;

    @Value("${pdf.tools.file.template.rectangle.left}")
    double templateRectLeft;
    @Value("${pdf.tools.file.template.rectangle.top}")
    double templateRectTop;
    @Value("${pdf.tools.file.template.rectangle.width}")
    double templateRectWidth;
    @Value("${pdf.tools.file.template.rectangle.height}")
    double templateRectHeight;

    @Resource
    PdfFileMapper pdfFileMapper;
    @Resource
    PdfDataCoordinateService pdfDataCoordinateService;
    @Resource
    PdfListService pdfListService;
    @Resource
    PdfListDetailService pdfListDetailService;

    @Transactional
    public PdfFileEntity uploadPdfFile(MultipartFile file) throws Exception {
        if (file == null) {
            return null;
        }

        String pdfFileId = IDUtil.uuid();
        String filePath = fileBaseDir + pdfFileId + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        file.transferTo(new File(filePath));

        return savePdfFile(pdfFileId, file.getOriginalFilename(), filePath);
    }

    @Transactional
    public PdfFileEntity uploadPdfFile(File file) {
        if (file == null || !file.exists()) {
            return null;
        }

        String pdfFileId = IDUtil.uuid();
        return savePdfFile(pdfFileId, file.getName(), file.getAbsolutePath());
    }

    @Transactional
    public PdfFileEntity savePdfFile(String id, String name, String path) {
        PdfFileEntity entity = new PdfFileEntity();
        entity.setPdfFileId(id);
        entity.setFileName(name);
        entity.setFilePath(path);
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
                    String declareFreightAmountUnit = null;
                    Double prod1DeclareAmountUsd = null;
                    Double prod2DeclareAmountUsd = null;
                    Double prod3DeclareAmountUsd = null;
                    Double prod4DeclareAmountUsd = null;
                    Double prod5DeclareAmountUsd = null;
                    Double prod6DeclareAmountUsd = null;
                    Double prod7DeclareAmountUsd = null;
                    Double prod8DeclareAmountUsd = null;
                    Double prod9DeclareAmountUsd = null;
                    Double prod10DeclareAmountUsd = null;

                    try {
                        // 单号
                        awb = DataUtil.getExcelCellStringValue(row.getCell(0));
                        // 换单号
                        awbReplace = DataUtil.getExcelCellStringValue(row.getCell(1));
                        // 件数
                        num = DataUtil.getExcelCellDoubleValue(row.getCell(2));
                        // 重量
                        weight = DataUtil.getExcelCellDoubleValue(row.getCell(3));
                        // 总申报价值USD
                        declareTotalAmountUsd = DataUtil.getExcelCellDoubleValue(row.getCell(4));
                        // 申报运费USD
                        declareFreightAmountUsd = DataUtil.getExcelCellDoubleValue(row.getCell(5));
                        // 申报运费单位
                        declareFreightAmountUnit = DataUtil.getExcelCellStringValue(row.getCell(6));
                        // 品名1美⾦申报价值
                        prod1DeclareAmountUsd = DataUtil.getExcelCellDoubleValue(row.getCell(7));
                        // 品名2美⾦申报价值
                        prod2DeclareAmountUsd = DataUtil.getExcelCellDoubleValue(row.getCell(8));
                        // 品名3美⾦申报价值
                        prod3DeclareAmountUsd = DataUtil.getExcelCellDoubleValue(row.getCell(9));

                        // 品名4美⾦申报价值
                        prod4DeclareAmountUsd = DataUtil.getExcelCellDoubleValue(row.getCell(10));
                        // 品名5美⾦申报价值
                        prod5DeclareAmountUsd = DataUtil.getExcelCellDoubleValue(row.getCell(11));
                        // 品名6美⾦申报价值
                        prod6DeclareAmountUsd = DataUtil.getExcelCellDoubleValue(row.getCell(12));
                        // 品名7美⾦申报价值
                        prod7DeclareAmountUsd = DataUtil.getExcelCellDoubleValue(row.getCell(13));
                        // 品名8美⾦申报价值
                        prod8DeclareAmountUsd = DataUtil.getExcelCellDoubleValue(row.getCell(14));
                        // 品名9美⾦申报价值
                        prod9DeclareAmountUsd = DataUtil.getExcelCellDoubleValue(row.getCell(15));
                        // 品名10美⾦申报价值
                        prod10DeclareAmountUsd = DataUtil.getExcelCellDoubleValue(row.getCell(16));
                    } catch (Exception e) {
                        log.warn("Excel数据格式不正确", e);
                    }

                    // 单号、总申报价值USD、申报运费USD、品名1美金申报价值是必须项
                    entity = new PdfListEntity();
                    if (StringUtils.isBlank(awb)) {
//                        throw new ServiceException(CodeInfo.CODE_PARAMS_NOT_NULL, MessageUtil.getMessage(CodeInfo.CODE_PARAMS_NOT_NULL, "单号"));
                        continue;
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
                        throw new ServiceException(CodeInfo.CODE_PARAMS_NOT_NULL, MessageUtil.getMessage(CodeInfo.CODE_PARAMS_NOT_NULL, "总申报价值USD"));
                    }
                    entity.setDeclareTotalAmountUsd(new BigDecimal(declareTotalAmountUsd.toString()));
                    if (declareFreightAmountUsd == null) {
                        throw new ServiceException(CodeInfo.CODE_PARAMS_NOT_NULL, MessageUtil.getMessage(CodeInfo.CODE_PARAMS_NOT_NULL, "申报运费USD"));
                    }
                    if (StringUtils.isNotBlank(declareFreightAmountUnit)) {
                        entity.setDeclareFreightAmountUnit(declareFreightAmountUnit);
                    }

                    entity.setDeclareFreightAmountUsd(new BigDecimal(declareFreightAmountUsd.toString()));
                    if (prod1DeclareAmountUsd == null) {
                        throw new ServiceException(CodeInfo.CODE_PARAMS_NOT_NULL, MessageUtil.getMessage(CodeInfo.CODE_PARAMS_NOT_NULL, "品名1美金申报价值"));
                    }
                    entity.setDetail("prod1", "declareAmountUsd", new BigDecimal(prod1DeclareAmountUsd));
                    if (prod2DeclareAmountUsd != null) {
                        entity.setDetail("prod2", "declareAmountUsd", new BigDecimal(prod2DeclareAmountUsd));
                    }
                    if (prod3DeclareAmountUsd != null) {
                        entity.setDetail("prod3", "declareAmountUsd", new BigDecimal(prod3DeclareAmountUsd));
                    }
                    if (prod4DeclareAmountUsd != null) {
                        entity.setDetail("prod4", "declareAmountUsd", new BigDecimal(prod4DeclareAmountUsd));
                    }
                    if (prod5DeclareAmountUsd != null) {
                        entity.setDetail("prod5", "declareAmountUsd", new BigDecimal(prod5DeclareAmountUsd));
                    }
                    if (prod6DeclareAmountUsd != null) {
                        entity.setDetail("prod6", "declareAmountUsd", new BigDecimal(prod6DeclareAmountUsd));
                    }
                    if (prod7DeclareAmountUsd != null) {
                        entity.setDetail("prod7", "declareAmountUsd", new BigDecimal(prod7DeclareAmountUsd));
                    }
                    if (prod8DeclareAmountUsd != null) {
                        entity.setDetail("prod8", "declareAmountUsd", new BigDecimal(prod8DeclareAmountUsd));
                    }
                    if (prod9DeclareAmountUsd != null) {
                        entity.setDetail("prod9", "declareAmountUsd", new BigDecimal(prod9DeclareAmountUsd));
                    }
                    if (prod10DeclareAmountUsd != null) {
                        entity.setDetail("prod10", "declareAmountUsd", new BigDecimal(prod10DeclareAmountUsd));
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

        Document document = null;
        PdfListEntity result = new PdfListEntity();
        String templateName = this.getFileTemplateName(pdfFileEntity.getFilePath());
        if (StringUtils.isBlank(templateName)) {
            throw new ServiceException(MessageUtil.getMessage("pdf.file.template.name.get.error"));
        }
        try {
            document = new Document(pdfFileEntity.getFilePath());
            for (int page = 1; page <= document.getPages().size(); page ++) {
                List<PdfDataCoordinateEntity> dataCoordinateList = this.pdfDataCoordinateService.getReplacePageDataCoordinateList(templateName, page);
                if (dataCoordinateList != null && dataCoordinateList.size() > 0) {
                    for (PdfDataCoordinateEntity coordinate : dataCoordinateList) {
                        String data = null;
                        if (coordinate.getReadType().equals(PdfDataCoordinateEntity.READ_TYPE_ASPOSE)) {
                            data = this.readDataByAspose(pdfFileEntity, page, coordinate);
                        } else if (coordinate.getReadType().equals(PdfDataCoordinateEntity.READ_TYPE_ITEXT)) {
                            data = this.readDataByItext(pdfFileEntity, page, coordinate);
                        }

                        Object convertData = null;
                        try {
                            convertData = convertData2Value(data, coordinate);
                        } catch (NumberFormatException e) {
                            convertData = null;
                        }
                        log.info("文件[" + pdfFileEntity.getPdfFileId() + "]的第[" + page + "]页，字段[" + coordinate.getFieldType() + "." + coordinate.getFieldCategory() + "." + coordinate.getFieldName() + "]=[" + data + "]，转换后数据[" + convertData + "]，读取方式[" + coordinate.getReadType() + "]");
                        if (PdfDataCoordinateEntity.FIELD_TYPE_LIST.equals(coordinate.getFieldType())) {
                            BeanUtil.setFieldValue(result, coordinate.getFieldName(), convertData);
                        } else if (PdfDataCoordinateEntity.FIELD_TYPE_DETAIL.equals(coordinate.getFieldType())) {
                            result.setDetail(coordinate.getFieldCategory(), coordinate.getFieldName(), convertData);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("error occurred : ", e);
            throw new ServiceException(MessageUtil.getMessage("pdf.file.read.error"));
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (Exception e) {}
            }
        }

        return result;
    }

    public String readDataByItext(PdfFileEntity pdfFile, int page, PdfDataCoordinateEntity coordinate) throws IOException {
        PdfReader reader = null;
        try {
            reader = new PdfReader(pdfFile.getFilePath());
            return this.readData(reader, page, coordinate.getLlx(), coordinate.getLly(), coordinate.getUrx(), coordinate.getUry());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {}
            }
        }
    }

    public String readDataByAspose(PdfFileEntity pdfFile, int page, PdfDataCoordinateEntity coordinate) throws IOException {
        Document document = null;
        try {
            document = new Document(pdfFile.getFilePath());
            return this.readData(document, page, coordinate.getLlx(), coordinate.getLly(), coordinate.getUrx(), coordinate.getUry());
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (Exception e) {}
            }
        }
    }

    public String getFileTemplateName(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }

        PdfReader reader = null;
        try {
            reader = new PdfReader(filePath);
            RectModel rectModel = this.pdfDataCoordinateService.createRect(templateRectLeft, templateRectTop, templateRectWidth, templateRectHeight);
            if (rectModel != null) {
                String result = this.readData(reader, 1, rectModel.getLlx(), rectModel.getLly(), rectModel.getUrx(), rectModel.getUry());
                if (StringUtils.isNotBlank(result)) {
                    return result.replaceAll("<", "").replaceAll(">", "");
                }
            }
        } catch (Exception e) {
            log.error("获取文件模板名称失败", e);
        }

        return null;
    }

    public String readData(Object pdf, int page, BigDecimal llx, BigDecimal lly, BigDecimal urx, BigDecimal ury) throws IOException {
        if (pdf == null || llx == null || lly == null || urx == null || ury == null) {
            return null;
        }

        String result = null;
        if (pdf instanceof Document) {
            Document document = (Document)pdf;

            com.aspose.pdf.Rectangle rectangle = new com.aspose.pdf.Rectangle(llx.doubleValue(), lly.doubleValue(), urx.doubleValue(), ury.doubleValue());
            TextSearchOptions searchOptions = new TextSearchOptions(rectangle);

            TextFragmentAbsorber textFragmentAbsorber = new TextFragmentAbsorber();
            textFragmentAbsorber.setTextSearchOptions(searchOptions);

            document.getPages().get_Item(page).accept(textFragmentAbsorber);
            TextFragmentCollection textFragmentCollection = textFragmentAbsorber.getTextFragments();
            if (textFragmentCollection != null && textFragmentCollection.size() > 0) {
                result = textFragmentCollection.get_Item(1).getText();
            }
        } else if (pdf instanceof PdfReader) {
            PdfReader reader = (PdfReader)pdf;

            Rectangle rect = new Rectangle(llx.floatValue(), lly.floatValue(), urx.floatValue(), ury.floatValue());
            RenderFilter filter = new RegionTextRenderFilter(rect);
            TextExtractionStrategy strategy = new FilteredTextRenderListener(new LocationTextExtractionStrategy(), filter);
            result = PdfTextExtractor.getTextFromPage(reader, page, strategy);
        }

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

        this.pdfFileMapper.delete(pdfFileEntity.getPdfFileId());
        FileUtil.deleteFile(pdfFileEntity.getFilePath());
    }

    @Transactional
    public void deletePdfFileById(String id) {
        if (StringUtils.isBlank(id)) {
            return;
        }

        PdfFileEntity pdfFileEntity = this.getPdfFile(id);
        this.deletePdfFile(pdfFileEntity);
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

    public void replacePdfFile(PdfListEntity updatedPdfEntity, PdfListEntity originalPdfEntity, boolean isClear, boolean isReplace) {
        if (updatedPdfEntity == null || originalPdfEntity == null) {
            return;
        }

        updatedPdfEntity.initPdfListDetailMap(this.pdfListDetailService.getPdfListDetail(updatedPdfEntity.getPdfId()));
        originalPdfEntity.initPdfListDetailMap(this.pdfListDetailService.getPdfListDetail(originalPdfEntity.getPdfId()));

        PdfFileEntity originalPdfFile = this.getPdfFileByPdfId(originalPdfEntity.getPdfId());
        if (originalPdfFile == null) {
            return;
        }
        PdfFileEntity updatedPdfFile = this.getPdfFileByPdfId(updatedPdfEntity.getPdfId());
        if (updatedPdfFile == null) {
            updatedPdfFile = new PdfFileEntity();
            updatedPdfFile.setPdfFileId(IDUtil.uuid());
            updatedPdfFile.setPdfId(updatedPdfEntity.getPdfId());

            String fileName = updatedPdfFile.getPdfFileId() + ".pdf";
            updatedPdfFile.setFileName(fileName);
            updatedPdfFile.setFilePath(fileBaseDir + fileName);
            this.pdfFileMapper.save(updatedPdfFile);
        }

        File originalFile = new File(originalPdfFile.getFilePath());
        if (originalFile == null || !originalFile.exists()) {
            throw new ServiceException(CodeInfo.CODE_PDF_FILE_NOT_EXIST);
        }

        Document document = null;
        String templateName = this.getFileTemplateName(originalPdfFile.getFilePath());
        if (StringUtils.isBlank(templateName)) {
            throw new ServiceException(MessageUtil.getMessage("pdf.file.template.name.get.error"));
        }
        try {
            document = new Document(originalPdfFile.getFilePath());
            document.getInfo().setAuthor("");
            for (int page = 1; page <= document.getPages().size(); page ++) {
                if (isReplace) {
                    List<PdfDataCoordinateEntity> dataCoordinateList = this.pdfDataCoordinateService.getReplacePageDataCoordinateList(templateName, page);
                    if (dataCoordinateList != null && dataCoordinateList.size() > 0) {
                        for (PdfDataCoordinateEntity coordinate : dataCoordinateList) {
                            String replaceText = getCoordinateText(coordinate, updatedPdfEntity);
                            String originalText = getCoordinateText(coordinate, originalPdfEntity);
                            log.info("replacePdfFileData[" + coordinate.getFieldType() + "." + coordinate.getFieldCategory() + "." + coordinate.getFieldName() + "] : originalText[" + originalText + "] - replaceText[" + replaceText + "]");
                            if (StringUtils.isBlank(replaceText)
                                    || StringUtils.isBlank(originalText)
                                    || replaceText.equals(originalText)) {
                                continue;
                            }

                            com.aspose.pdf.Rectangle rectangle = new com.aspose.pdf.Rectangle(coordinate.getLlx().doubleValue(), coordinate.getLly().doubleValue(), coordinate.getUrx().doubleValue(), coordinate.getUry().doubleValue());

                            TextReplaceOptions replaceOptions = new TextReplaceOptions();
                            replaceOptions.setReplaceAdjustmentAction(TextReplaceOptions.ReplaceAdjustment.None);
                            TextSearchOptions searchOptions = new TextSearchOptions(rectangle);

                            TextFragmentAbsorber textFragmentAbsorber = new TextFragmentAbsorber();
                            textFragmentAbsorber.setTextReplaceOptions(replaceOptions);
                            textFragmentAbsorber.setTextSearchOptions(searchOptions);

                            document.getPages().get_Item(page).accept(textFragmentAbsorber);
                            TextFragmentCollection textFragmentCollection = textFragmentAbsorber.getTextFragments();
                            for (TextFragment textFragment : (Iterable<TextFragment>) textFragmentCollection) {
                                log.info("replacePdfFileData[" + coordinate.getFieldType() + "." + coordinate.getFieldCategory() + "." + coordinate.getFieldName() + "] : textFragment.getText()[" + textFragment.getText() + "] - rectangle[" + textFragment.getRectangle() + "]");
                                if (!originalText.equals(textFragment.getText())) {
                                    continue;
                                }

                                double replaceBeforeURX = textFragment.getRectangle().getURX();
                                textFragment.setText(replaceText);
                                double replaceAfterURX = textFragment.getRectangle().getURX();
                                double gap = replaceAfterURX - replaceBeforeURX;

                                if (PdfDataCoordinateEntity.ALIGN_RIGHT.equals(coordinate.getAlign())) {
                                    if (gap != 0.0) {
                                        double baseLineX = textFragment.getBaselinePosition().getXIndent();
                                        double baseLineY = textFragment.getBaselinePosition().getYIndent();

                                        textFragment.setBaselinePosition(new Position(baseLineX - gap, baseLineY + 1.2432));
                                    }
                                }

                                if (this.displayRectangle) {
                                    textFragment.getTextState().setBackgroundColor(Color.getYellow());
                                }
                            }
                        }
                    }
                }

                if (isClear) {
                    List<PdfDataCoordinateEntity> dataCoordinateList = this.pdfDataCoordinateService.getDeletePageDataCoordinateList(templateName, page);
                    if (dataCoordinateList != null && dataCoordinateList.size() > 0) {
                        for (PdfDataCoordinateEntity coordinate : dataCoordinateList) {
                            if (StringUtils.isNotBlank(coordinate.getFieldName())) {
                                // 如果有更新数据，则不替换
                                String replaceText = getCoordinateText(coordinate, updatedPdfEntity);
                                if (StringUtils.isNotBlank(replaceText)) {
                                    continue;
                                }
                            }

                            com.aspose.pdf.Rectangle rectangle = new com.aspose.pdf.Rectangle(coordinate.getLlx().doubleValue(), coordinate.getLly().doubleValue(), coordinate.getUrx().doubleValue(), coordinate.getUry().doubleValue());

                            TextReplaceOptions replaceOptions = new TextReplaceOptions();
                            replaceOptions.setReplaceAdjustmentAction(TextReplaceOptions.ReplaceAdjustment.None);
                            TextSearchOptions searchOptions = new TextSearchOptions(rectangle);

                            TextFragmentAbsorber textFragmentAbsorber = new TextFragmentAbsorber();
                            textFragmentAbsorber.setTextReplaceOptions(replaceOptions);
                            textFragmentAbsorber.setTextSearchOptions(searchOptions);

                            document.getPages().get_Item(page).accept(textFragmentAbsorber);
                            TextFragmentCollection textFragmentCollection = textFragmentAbsorber.getTextFragments();
                            for (TextFragment textFragment : (Iterable<TextFragment>) textFragmentCollection) {
                                textFragment.setText(" ");
                                if (this.displayRectangle) {
                                    textFragment.getTextState().setBackgroundColor(Color.getGray());
                                }
                            }
                        }
                    }
                }
            }

            document.save(updatedPdfFile.getFilePath());
        } catch (Exception e) {
            log.error("error occurred : ", e);
            throw new ServiceException(MessageUtil.getMessage("pdf.file.replace.error"));
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (Exception e) {}
            }
        }
    }

    private String getCoordinateText(PdfDataCoordinateEntity coordinate, PdfListEntity updatedPdf) {
        String result = "";
        try {
            Object obj = null;
            if (PdfDataCoordinateEntity.FIELD_TYPE_LIST.equals(coordinate.getFieldType())) {
                obj = BeanUtil.getFieldValue(updatedPdf, coordinate.getFieldName());
            } else if (PdfDataCoordinateEntity.FIELD_TYPE_DETAIL.equals(coordinate.getFieldType())) {
                obj = updatedPdf.getDetail(coordinate.getFieldCategory(), coordinate.getFieldName());
            }

            if (obj != null) {
                if (PdfDataCoordinateEntity.DATA_TYPE_DECIMAL.equals(coordinate.getDataType())) {
                    BigDecimal bd = (BigDecimal)obj;
                    result = DataUtil.parseNumber(bd, coordinate.getDecimalDigits());
                } else if ("awb".equals(coordinate.getFieldName())) { // 如果是单号，需要判断是否用换单号替换
                    if (StringUtils.isNotBlank(updatedPdf.getAwbReplace())) {
                        result = updatedPdf.getAwbReplace();
                    } else {
                        result = updatedPdf.getAwb();
                    }
                } else {
                    result = String.valueOf(obj);
                }
            }

            if (StringUtils.isNotBlank(result)) {
                if (StringUtils.isNotBlank(coordinate.getPrefix())) {
                    result = coordinate.getPrefix() + result;
                }
                if (StringUtils.isNotBlank(coordinate.getSuffix())) {
                    result = result + coordinate.getSuffix();
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

    public String getFileBaseDir() {
        return fileBaseDir;
    }

    public HSSFWorkbook getExportWorkbook(int type, List<PdfListModel> datas) {
        HSSFWorkbook wb = new HSSFWorkbook();
        String sheetName = "对比数据";
        if (type == PdfListEntity.TYPE_ORIGINAL) {
            sheetName = "原始数据";
        } else if (type == PdfListEntity.TYPE_UPDATED) {
            sheetName = "更新数据";
        }
        HSSFSheet sheet = wb.createSheet(sheetName);

        List<ExportColumnModel> titles = this.getColumns();
        HSSFRow row = sheet.createRow(0);
        //创建标题
        for(int i = 0; i < titles.size(); i ++) {
            row.createCell(i).setCellValue(titles.get(i).getTitle());
        }

        // 创建内容
        PdfModel pdfModel = null;
        PdfDetailModel pdfDetailModel = null;
        for (int i = 0; i < datas.size(); i ++) {
            row = sheet.createRow(i + 1);

            Map<String, PdfListDetailModel> pdfDetailModelMap = new HashMap<String, PdfListDetailModel>();
            if (datas.get(i).getDetailList() != null && !datas.get(i).getDetailList().isEmpty()) {
                datas.get(i).getDetailList().forEach(detail -> {
                    pdfDetailModelMap.put(detail.getProdNo(), detail);
                });
            }

            for (int j = 0; j < titles.size(); j ++) {
                pdfModel = null;
                pdfDetailModel = null;

                if (PdfDataCoordinateEntity.FIELD_TYPE_DETAIL.equals(titles.get(j).getDataFieldType())) {
                    PdfListDetailModel pdfListDetailModel = pdfDetailModelMap.get(titles.get(j).getDataFieldCategory());
                    if (pdfListDetailModel != null) {
                        if (type == PdfListEntity.TYPE_ORIGINAL) {
                            pdfDetailModel = pdfListDetailModel.getOriginalPdfDetail();
                        } else if (type == PdfListEntity.TYPE_UPDATED) {
                            pdfDetailModel = pdfListDetailModel.getUpdatedPdfDetail();
                        }
                    }

                    row.createCell(j).setCellValue(this.createRowValue(pdfDetailModel, titles.get(j).getDataFieldName()));
                } else {
                    if (type == PdfListEntity.TYPE_ORIGINAL) {
                        pdfModel = datas.get(i).getOriginalPdf();
                    } else if (type == PdfListEntity.TYPE_UPDATED) {
                        pdfModel = datas.get(i).getUpdatedPdf();
                    } else {
                        pdfModel = datas.get(i).getComparePdf();
                        if (pdfModel != null && datas.get(i).getUpdatedPdf() != null) {
                            pdfModel.setAwb(datas.get(i).getUpdatedPdf().getAwb());
                            pdfModel.setAwbReplace(datas.get(i).getUpdatedPdf().getAwbReplace());
                        }
                    }

                    row.createCell(j).setCellValue(this.createRowValue(pdfModel, titles.get(j).getDataFieldName()));
                }
            }
        }

        return wb;
    }

    private List<ExportColumnModel> getColumns() {
        List<ExportColumnModel> result = new ArrayList<ExportColumnModel>();
        result.add(new ExportColumnModel("单号", PdfDataCoordinateEntity.FIELD_TYPE_LIST, null, "awb"));
        result.add(new ExportColumnModel("换单号", PdfDataCoordinateEntity.FIELD_TYPE_LIST, null, "awbReplace"));
        result.add(new ExportColumnModel("制作时间", PdfDataCoordinateEntity.FIELD_TYPE_LIST, null, "makeTime"));
        result.add(new ExportColumnModel("许可时间", PdfDataCoordinateEntity.FIELD_TYPE_LIST, null, "permissionTime"));
        result.add(new ExportColumnModel("制作状态", PdfDataCoordinateEntity.FIELD_TYPE_LIST, null, "makeStatus"));
        result.add(new ExportColumnModel("件数", PdfDataCoordinateEntity.FIELD_TYPE_LIST, null, "num"));
        result.add(new ExportColumnModel("重量", PdfDataCoordinateEntity.FIELD_TYPE_LIST, null, "weight"));
        result.add(new ExportColumnModel("总申报价值USD", PdfDataCoordinateEntity.FIELD_TYPE_LIST, null, "declareTotalAmountUsd"));
        result.add(new ExportColumnModel("申报运费", PdfDataCoordinateEntity.FIELD_TYPE_LIST, null, "declareFreightAmountUsd"));
        result.add(new ExportColumnModel("申报运费单位", PdfDataCoordinateEntity.FIELD_TYPE_LIST, null, "declareFreightAmountUnit"));
        result.add(new ExportColumnModel("通关金额", PdfDataCoordinateEntity.FIELD_TYPE_LIST, null, "clearanceAmount"));
        result.add(new ExportColumnModel("BPR合计", PdfDataCoordinateEntity.FIELD_TYPE_LIST, null, "bprAmount"));
        result.add(new ExportColumnModel("关税", PdfDataCoordinateEntity.FIELD_TYPE_LIST, null, "tariff"));
        result.add(new ExportColumnModel("消费税", PdfDataCoordinateEntity.FIELD_TYPE_LIST, null, "exciseTax"));
        result.add(new ExportColumnModel("地方消费税", PdfDataCoordinateEntity.FIELD_TYPE_LIST, null, "localExciseTax"));
        result.add(new ExportColumnModel("税金合计", PdfDataCoordinateEntity.FIELD_TYPE_LIST, null, "taxTotalAmount"));
        result.add(new ExportColumnModel("美元日元汇率", PdfDataCoordinateEntity.FIELD_TYPE_LIST, null, "usdJpyExchangeRate"));
        result.add(new ExportColumnModel("进口商", PdfDataCoordinateEntity.FIELD_TYPE_LIST, null, "importer"));

        int index = 1;
        int max = 10;
        while (index <= max) {
            String fieldCategory = "prod" + index;
            result.add(new ExportColumnModel("品名" + index + "美金申报价值", PdfDataCoordinateEntity.FIELD_TYPE_DETAIL, fieldCategory, "declareAmountUsd"));
            result.add(new ExportColumnModel("品名" + index + "关税率", PdfDataCoordinateEntity.FIELD_TYPE_DETAIL, fieldCategory, "tariffRate"));
            result.add(new ExportColumnModel("品名" + index + "运费比重", PdfDataCoordinateEntity.FIELD_TYPE_DETAIL, fieldCategory, "freightPct"));
            result.add(new ExportColumnModel("品名" + index + "日元申报价值", PdfDataCoordinateEntity.FIELD_TYPE_DETAIL, fieldCategory, "declareAmountJpy"));
            result.add(new ExportColumnModel("品名" + index + "关税计算基数", PdfDataCoordinateEntity.FIELD_TYPE_DETAIL, fieldCategory, "tariffBase"));
            result.add(new ExportColumnModel("品名" + index + "关税额", PdfDataCoordinateEntity.FIELD_TYPE_DETAIL, fieldCategory, "tariff"));
            result.add(new ExportColumnModel("品名" + index + "关税取整", PdfDataCoordinateEntity.FIELD_TYPE_DETAIL, fieldCategory, "tariffRounding"));
            result.add(new ExportColumnModel("品名" + index + "国内消费税", PdfDataCoordinateEntity.FIELD_TYPE_DETAIL, fieldCategory, "countryExciseTax"));
            result.add(new ExportColumnModel("品名" + index + "国内消费税额基数", PdfDataCoordinateEntity.FIELD_TYPE_DETAIL, fieldCategory, "countryExciseTaxBase"));
            result.add(new ExportColumnModel("品名" + index + "国内消费税金额", PdfDataCoordinateEntity.FIELD_TYPE_DETAIL, fieldCategory, "countryExciseTaxAmount"));
            result.add(new ExportColumnModel("品名" + index + "地方消费税基数", PdfDataCoordinateEntity.FIELD_TYPE_DETAIL, fieldCategory, "localExciseTaxBase"));
            result.add(new ExportColumnModel("品名" + index + "地方消费税金额", PdfDataCoordinateEntity.FIELD_TYPE_DETAIL, fieldCategory, "localExciseTaxAmount"));

            index ++;
        }

        result.add(new ExportColumnModel("关税合计", PdfDataCoordinateEntity.FIELD_TYPE_LIST, null, "tariffTotalAmount"));
        result.add(new ExportColumnModel("国内消费税合计", PdfDataCoordinateEntity.FIELD_TYPE_LIST, null, "countryExciseTaxTotalAmount"));
        result.add(new ExportColumnModel("地方消费税合计", PdfDataCoordinateEntity.FIELD_TYPE_LIST, null, "localExciseTaxTotalAmount"));

        return result;
    }

    private String createRowValue(Object data, String fieldName) {
        if (data == null || StringUtils.isBlank(fieldName)) {
            return "";
        }
        Object fieldVal = null;
        try {
            fieldVal = BeanUtil.getFieldValue(data, fieldName);
        } catch (Exception e) {
            log.warn("获取数据失败", e);
        }
        if (fieldVal == null) {
            return "";
        }

        if ("makeStatus".equals(fieldName)) {
            int makeStatus = Integer.parseInt(fieldVal.toString());
            if (makeStatus == PdfListEntity.MAKE_STATUS_YES) {
                return "已制作";
            } else {
                return "未制作";
            }
        } else if ("tariffRate".equals(fieldName)
                || "freightPct".equals(fieldName)) {
            return (new BigDecimal(fieldVal.toString()).multiply(new BigDecimal(100))).toPlainString() + "%";
        } else {
            return fieldVal.toString();
        }
    }
}
