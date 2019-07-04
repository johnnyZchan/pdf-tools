package com.ledi.pdftools.controllers;

import com.ledi.pdftools.beans.ResponseModel;
import com.ledi.pdftools.entities.PdfFileEntity;
import com.ledi.pdftools.services.PdfFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@Slf4j
public class FileController extends BaseController {

    @Resource
    private PdfFileService pdfFileService;

    @GetMapping("/download/templates")
    public void downloadTemplates(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileName = "template.xlsx";
        Path file = Paths.get(ResourceUtils.getFile("classpath:" + fileName).getPath());
        if (Files.exists(file)) {
            response.setContentType("application/msexcel");
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);

            Files.copy(file, response.getOutputStream());
            response.getOutputStream().flush();
        }
    }

    @PostMapping("/pdf/upload")
    public ResponseModel pdfUpload(@RequestParam("file") MultipartFile file) throws Exception {
        PdfFileEntity result = this.pdfFileService.uploadPdfFile(file);
        return this.getOkResponseModel(result);
    }
}