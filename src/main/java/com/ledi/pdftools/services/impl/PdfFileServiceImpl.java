package com.ledi.pdftools.services.impl;

import com.aspose.pdf.*;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.*;
import com.ledi.pdftools.beans.ExportColumnModel;
import com.ledi.pdftools.beans.PdfFileModel;
import com.ledi.pdftools.beans.PdfListModel;
import com.ledi.pdftools.beans.PdfModel;
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
        try {
            document = new Document(originalPdfFile.getFilePath());
            document.getInfo().setAuthor("");
            for (int page = 1; page <= document.getPages().size(); page ++) {
                if (isReplace) {
                    List<PdfDataCoordinateEntity> dataCoordinateList = this.pdfDataCoordinateService.getReplacePageDataCoordinateList(page);
                    if (dataCoordinateList != null && dataCoordinateList.size() > 0) {
                        for (PdfDataCoordinateEntity coordinate : dataCoordinateList) {
                            String replaceText = getCoordinateText(coordinate, updatedPdfEntity);
                            String originalText = getCoordinateText(coordinate, originalPdfEntity);
                            log.info("replacePdfFileData[" + coordinate.getFieldName() + "] : originalText[" + originalText + "] - replaceText[" + replaceText + "]");
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
                                log.info("replacePdfFileData[" + coordinate.getFieldName() + "] : textFragment.getText()[" + textFragment.getText() + "] - rectangle[" + textFragment.getRectangle() + "]");
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
                    List<PdfDataCoordinateEntity> dataCoordinateList = this.pdfDataCoordinateService.getDeletePageDataCoordinateList(page);
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
            Object obj = BeanUtil.getFieldValue(updatedPdf, coordinate.getFieldName());
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
        for (int i = 0; i < datas.size(); i ++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < titles.size(); j ++) {
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

        return wb;
    }

    private List<ExportColumnModel> getColumns() {
        List<ExportColumnModel> result = new ArrayList<ExportColumnModel>();
        result.add(new ExportColumnModel("单号", "awb"));
        result.add(new ExportColumnModel("换单号", "awbReplace"));
        result.add(new ExportColumnModel("制作时间", "makeTime"));
        result.add(new ExportColumnModel("许可时间", "permissionTime"));
        result.add(new ExportColumnModel("制作状态", "makeStatus"));
        result.add(new ExportColumnModel("件数", "num"));
        result.add(new ExportColumnModel("重量", "weight"));
        result.add(new ExportColumnModel("总申报价值USD", "declareTotalAmountUsd"));
        result.add(new ExportColumnModel("申报运费USD", "declareFreightAmountUsd"));
        result.add(new ExportColumnModel("通关金额", "clearanceAmount"));
        result.add(new ExportColumnModel("BPR合计", "bprAmount"));
        result.add(new ExportColumnModel("关税", "tariff"));
        result.add(new ExportColumnModel("消费税", "exciseTax"));
        result.add(new ExportColumnModel("地方消费税", "localExciseTax"));
        result.add(new ExportColumnModel("税金合计", "taxTotalAmount"));
        result.add(new ExportColumnModel("美元日元汇率", "usdJpyExchangeRate"));

        result.add(new ExportColumnModel("品名1美金申报价值", "prod1DeclareAmountUsd"));
        result.add(new ExportColumnModel("品名1关税率", "prod1TariffRate"));
        result.add(new ExportColumnModel("品名1运费比重", "prod1FreightPct"));
        result.add(new ExportColumnModel("品名1日元申报价值", "prod1DeclareAmountJpy"));
        result.add(new ExportColumnModel("品名1关税计算基数", "prod1TariffBase"));
        result.add(new ExportColumnModel("品名1关税额", "prod1Tariff"));
        result.add(new ExportColumnModel("品名1关税取整", "prod1TariffRounding"));
        result.add(new ExportColumnModel("品名1国内消费税", "prod1CountryExciseTax"));
        result.add(new ExportColumnModel("品名1国内消费税额基数", "prod1CountryExciseTaxBase"));
        result.add(new ExportColumnModel("品名1国内消费税金额", "prod1CountryExciseTaxAmount"));
        result.add(new ExportColumnModel("品名1地方消费税基数", "prod1LocalExciseTaxBase"));
        result.add(new ExportColumnModel("品名1地方消费税金额", "prod1LocalExciseTaxAmount"));

        result.add(new ExportColumnModel("品名2美金申报价值", "prod2DeclareAmountUsd"));
        result.add(new ExportColumnModel("品名2关税率", "prod2TariffRate"));
        result.add(new ExportColumnModel("品名2运费比重", "prod2FreightPct"));
        result.add(new ExportColumnModel("品名2日元申报价值", "prod2DeclareAmountJpy"));
        result.add(new ExportColumnModel("品名2关税计算基数", "prod2TariffBase"));
        result.add(new ExportColumnModel("品名2关税额", "prod2Tariff"));
        result.add(new ExportColumnModel("品名2关税取整", "prod2TariffRounding"));
        result.add(new ExportColumnModel("品名2国内消费税", "prod2CountryExciseTax"));
        result.add(new ExportColumnModel("品名2国内消费税额基数", "prod2CountryExciseTaxBase"));
        result.add(new ExportColumnModel("品名2国内消费税金额", "prod2CountryExciseTaxAmount"));
        result.add(new ExportColumnModel("品名2地方消费税基数", "prod2LocalExciseTaxBase"));
        result.add(new ExportColumnModel("品名2地方消费税金额", "prod2LocalExciseTaxAmount"));

        result.add(new ExportColumnModel("品名3美金申报价值", "prod3DeclareAmountUsd"));
        result.add(new ExportColumnModel("品名3关税率", "prod3TariffRate"));
        result.add(new ExportColumnModel("品名3运费比重", "prod3FreightPct"));
        result.add(new ExportColumnModel("品名3日元申报价值", "prod3DeclareAmountJpy"));
        result.add(new ExportColumnModel("品名3关税计算基数", "prod3TariffBase"));
        result.add(new ExportColumnModel("品名3关税额", "prod3Tariff"));
        result.add(new ExportColumnModel("品名3关税取整", "prod3TariffRounding"));
        result.add(new ExportColumnModel("品名3国内消费税", "prod3CountryExciseTax"));
        result.add(new ExportColumnModel("品名3国内消费税额基数", "prod3CountryExciseTaxBase"));
        result.add(new ExportColumnModel("品名3国内消费税金额", "prod3CountryExciseTaxAmount"));
        result.add(new ExportColumnModel("品名3地方消费税基数", "prod3LocalExciseTaxBase"));
        result.add(new ExportColumnModel("品名3地方消费税金额", "prod3LocalExciseTaxAmount"));

        result.add(new ExportColumnModel("关税合计", "tariffTotalAmount"));
        result.add(new ExportColumnModel("国内消费税合计", "countryExciseTaxTotalAmount"));
        result.add(new ExportColumnModel("地方消费税合计", "localExciseTaxTotalAmount"));

        return result;
    }

    private String createRowValue(PdfModel pdfModel, String fieldName) {
        if (pdfModel == null || StringUtils.isBlank(fieldName)) {
            return "";
        }
        Object fieldVal = null;
        try {
            fieldVal = BeanUtil.getFieldValue(pdfModel, fieldName);
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
        } else if ("prod1TariffRate".equals(fieldName) || "prod2TariffRate".equals(fieldName) || "prod3TariffRate".equals(fieldName)
                || "prod1FreightPct".equals(fieldName) || "prod2FreightPct".equals(fieldName) || "prod3FreightPct".equals(fieldName)) {
            return (new BigDecimal(fieldVal.toString()).multiply(new BigDecimal(100))).toPlainString() + "%";
        } else {
            return fieldVal.toString();
        }
    }
}
