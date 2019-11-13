package com.ledi.pdftools.controllers;

import com.ledi.pdftools.beans.PdfListModel;
import com.ledi.pdftools.beans.ResponseModel;
import com.ledi.pdftools.beans.SkipModel;
import com.ledi.pdftools.services.PdfFileService;
import com.ledi.pdftools.services.PdfListService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "PDF列表接口")
@RestController
@Slf4j
public class PdfListController extends BaseController {

    @Resource
    PdfListService pdfListService;
    @Resource
    PdfFileService pdfFileService;

    @PostMapping("/pdf/list")
    public ResponseModel getPdfList(HttpServletRequest request,
                                    @RequestParam(name = "awb", required = false) String awb,
                                    @RequestParam(name = "makeStatus", required = false) Integer makeStatus,
                                    @RequestParam(name = "makeStartTime", required = false) String makeStartTime,
                                    @RequestParam(name = "makeEndTime", required = false) String makeEndTime,
                                    @RequestParam(name = "permissionStartTime", required = false) String permissionStartTime,
                                    @RequestParam(name = "permissionEndTime", required = false) String permissionEndTime,
                                    @RequestParam(name = "importer", required = false) String importer,
                                    @RequestParam(name = "start", defaultValue = "0") Integer start,
                                    @RequestParam(name = "length", defaultValue = "10") Integer length) {
        int totalCount = pdfListService.getPdfListCount(awb, makeStatus, makeStartTime, makeEndTime, permissionStartTime, permissionEndTime, importer);
        List<PdfListModel> dataList = new ArrayList<PdfListModel>();
        if (totalCount > 0) {
            dataList = pdfListService.getPdfModelList(awb, makeStatus, makeStartTime, makeEndTime, permissionStartTime, permissionEndTime, importer, start, length);
        }

        return this.getOkResponseModel(totalCount, dataList);
    }

    @GetMapping("/pdf/list/export")
    public void exportPdfList(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(name = "type") Integer type,
                              @RequestParam(name = "awb", required = false) String awb,
                              @RequestParam(name = "makeStatus", required = false) Integer makeStatus,
                              @RequestParam(name = "makeStartTime", required = false) String makeStartTime,
                              @RequestParam(name = "makeEndTime", required = false) String makeEndTime,
                              @RequestParam(name = "permissionStartTime", required = false) String permissionStartTime,
                              @RequestParam(name = "permissionEndTime", required = false) String permissionEndTime,
                              @RequestParam(name = "importer", required = false) String importer) throws Exception {
        List<PdfListModel> dataList = this.pdfListService.getPdfModelList(awb, makeStatus, makeStartTime, makeEndTime, permissionStartTime, permissionEndTime, importer, null, null);
        HSSFWorkbook wb = this.pdfFileService.getExportWorkbook(type, dataList);
        try {
            String fileName = "PdfDataList.xls";
            response.setContentType("application/msexcel");
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            wb.write(response.getOutputStream());
            response.flushBuffer();
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (Exception e) {}
            }
        }
    }

    @PutMapping("/pdf/list")
    public ResponseModel addPdf(@RequestParam(value = "pdfFileIdList[]") List<String> pdfFileIdList,
                                @RequestParam(value = "coverFlg", defaultValue = "false") boolean coverFlg) {
        List<SkipModel> skipList = this.pdfListService.addPdf(pdfFileIdList, coverFlg);
        if (skipList == null || skipList.isEmpty()) {
            return this.getOkResponseModel();
        } else {
            List<String> skipAwbList = new ArrayList<String>();
            for (SkipModel model : skipList) {
                skipAwbList.add(model.getAwb());
                this.pdfFileService.deletePdfFileById(model.getPdfFileId());
            }
            return this.getPartialOkResponseModel(skipAwbList);
        }
    }

    @DeleteMapping("/pdf/list")
    public ResponseModel delPdf(@RequestParam(value = "awbList[]") List<String> awbList) {
        this.pdfListService.delPdf(awbList);
        return this.getOkResponseModel();
    }

    @PostMapping("/pdf/list/make")
    public ResponseModel makePdf(@RequestParam(value = "awbList[]") List<String> awbList,
                                 @RequestParam(value = "deleteFlg") Boolean deleteFlg,
                                 @RequestParam(value = "replaceFlg") Boolean replaceFlg) {
        List<String> failAwbList = this.pdfListService.makePdf(awbList, deleteFlg, replaceFlg);
        if (failAwbList == null || failAwbList.isEmpty()) {
            return this.getOkResponseModel();
        }

        return this.getPartialOkResponseModel(failAwbList);
    }
}
