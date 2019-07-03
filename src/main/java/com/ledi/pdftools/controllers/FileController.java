package com.ledi.pdftools.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@Slf4j
public class FileController extends BaseController {

    @GetMapping("/download/templates")
    public void downloadTemplates(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileName = "template.xlsx";
        File file = ResourceUtils.getFile("classpath:template.xlsx");
        if (file != null && file.exists()) {
            response.setContentType("application/msexcel");
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);

            Files.copy(new FileInputStream(file), response.getOutputStream());
            response.getOutputStream().flush();
        }
    }
}
