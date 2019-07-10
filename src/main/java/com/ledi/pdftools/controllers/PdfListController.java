package com.ledi.pdftools.controllers;

import com.ledi.pdftools.beans.PdfListModel;
import com.ledi.pdftools.beans.ResponseModel;
import com.ledi.pdftools.beans.SkipModel;
import com.ledi.pdftools.services.PdfFileService;
import com.ledi.pdftools.services.PdfListService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
                                    @RequestParam(name = "start", defaultValue = "0") Integer start,
                                    @RequestParam(name = "length", defaultValue = "10") Integer length) {
        int totalCount = pdfListService.getPdfListCount(awb, makeStatus, makeStartTime, makeEndTime);
        List<PdfListModel> dataList = new ArrayList<PdfListModel>();
        if (totalCount > 0) {
            dataList = pdfListService.getPdfModelList(awb, makeStatus, makeStartTime, makeEndTime, start, length);
        }

        return this.getOkResponseModel(totalCount, dataList);
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
