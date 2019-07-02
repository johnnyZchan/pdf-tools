package com.ledi.pdftools.controllers;

import com.ledi.pdftools.beans.ResponseModel;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "PDF列表接口")
@RestController
@Slf4j
public class PdfListController extends BaseController {


    @GetMapping("/pdf/list")
    public ResponseModel getPdfList(HttpServletRequest request,
                                    @RequestHeader(name = "lang", defaultValue = "sc") String lang,
                                    @RequestParam(name = "page", defaultValue = "0") Integer page,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {


        return this.getOkResponseModel();
    }
}
