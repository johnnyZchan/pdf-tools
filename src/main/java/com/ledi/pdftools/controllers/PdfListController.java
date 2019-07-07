package com.ledi.pdftools.controllers;

import com.ledi.pdftools.beans.PdfListModel;
import com.ledi.pdftools.beans.ResponseModel;
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

    @PostMapping("/pdf/list")
    public ResponseModel getPdfList(HttpServletRequest request,
                                    @RequestHeader(name = "lang", defaultValue = "sc") String lang,
                                    @RequestParam(name = "start", defaultValue = "0") Integer start,
                                    @RequestParam(name = "length", defaultValue = "10") Integer length) {
        int totalCount = pdfListService.getPdfListCount();
        List<PdfListModel> dataList = new ArrayList<PdfListModel>();
        if (totalCount > 0) {
            dataList = pdfListService.getPdfModelList(start, length);
        }

        return this.getOkResponseModel(totalCount, dataList);
    }

    @PutMapping("/pdf/list/{pdfFileId}")
    public ResponseModel addPdf(@PathVariable(value = "pdfFileId") String pdfFileId,
                                @RequestParam(value = "coverFlg", defaultValue = "false") boolean coverFlg) {
        this.pdfListService.addPdf(pdfFileId, coverFlg);
        return this.getOkResponseModel();
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
