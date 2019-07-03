package com.ledi.pdftools.controllers;

import com.ledi.pdftools.beans.PdfListModel;
import com.ledi.pdftools.beans.ResponseModel;
import com.ledi.pdftools.services.PdfListService;
import io.swagger.annotations.Api;
import javafx.scene.chart.ValueAxis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
        List<PdfListModel> dataList = null;
        if (totalCount > 0) {
            dataList = pdfListService.getPdfModelList(start, length);
        }

        return this.getOkResponseModel(totalCount, dataList);
    }

    @PutMapping("/pdf/list/{pdfFileId}")
    public ResponseModel addPdf(@PathVariable(value = "pdfFileId") String pdfFileId) {
        this.pdfListService.addPdf(pdfFileId);
        return this.getOkResponseModel();
    }
}
