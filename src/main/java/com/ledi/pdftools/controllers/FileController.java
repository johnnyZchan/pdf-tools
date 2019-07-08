package com.ledi.pdftools.controllers;

import com.ledi.pdftools.beans.PdfFileModel;
import com.ledi.pdftools.beans.ResponseModel;
import com.ledi.pdftools.entities.PdfFileEntity;
import com.ledi.pdftools.entities.PdfListEntity;
import com.ledi.pdftools.services.PdfFileService;
import com.ledi.pdftools.services.PdfListService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@Slf4j
public class FileController extends BaseController {

    @Resource
    private PdfFileService pdfFileService;
    @Resource
    private PdfListService pdfListService;

    @GetMapping("/download/templates")
    public void downloadTemplates(HttpServletRequest request, HttpServletResponse response) throws Exception {
        InputStream inputStream = null;
        try {
            String fileName = "template.xlsx";

            response.setContentType("application/msexcel");
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);

            ClassPathResource classPathResource = new ClassPathResource(fileName);
            inputStream = classPathResource.getInputStream();

            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    @PostMapping("/download/pdf")
    public void downloadPdf(@RequestParam(value = "awbList") List<String> awbList,
                            @RequestParam(value = "type") Integer type,
                            HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
        List<PdfFileModel> fileList = this.pdfFileService.getPdfFileList(awbList, type);
        //响应头的设置
        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");

        //设置压缩包的名字
        //解决不同浏览器压缩包名字含有中文时乱码的问题
        String downloadName = "pdf.zip";
        response.setHeader("Content-Disposition", "attachment;fileName=\"" + downloadName + "\"");

        //设置压缩流：直接写入response，实现边压缩边下载
        ZipOutputStream zipos = null;
        DataOutputStream os = null;
        try {
            zipos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
            zipos.setMethod(ZipOutputStream.DEFLATED); //设置压缩方法

            if (fileList != null && !fileList.isEmpty()) {
                //循环将文件写入压缩流
                for (PdfFileModel fileModel : fileList) {
                    File file = new File(fileModel.getFilePath());
                    if (file != null && file.exists()) {
                        //添加ZipEntry，并ZipEntry中写入文件流
                        zipos.putNextEntry(new ZipEntry(StringUtils.isNotBlank(fileModel.getAwbReplace())?fileModel.getAwbReplace():fileModel.getAwb() + ".pdf"));
                        os = new DataOutputStream(zipos);
                        InputStream is = new FileInputStream(file);
                        byte[] b = new byte[1024];
                        int length = 0;
                        while((length = is.read(b))!= -1){
                            os.write(b, 0, length);
                        }
                        is.close();
                        zipos.closeEntry();
                    }
                }
            }
        } finally {
            try {
                os.flush();
                os.close();
                zipos.close();
            } catch (Exception e) {}
        }
    }

    @PostMapping("/pdf/upload")
    public ResponseModel pdfUpload(@RequestParam("file") MultipartFile file) throws Exception {
        PdfFileEntity result = this.pdfFileService.uploadPdfFile(file);
        return this.getOkResponseModel(result);
    }

    @PostMapping("/excel/upload")
    public ResponseModel excelUpload(@RequestParam("file") MultipartFile file,
                                     @RequestParam(name = "coverFlg", defaultValue = "false") boolean coverFlg) throws Exception {
        List<PdfListEntity> pdfListList = this.pdfFileService.uploadExcelFile(file);
        this.pdfListService.addUpdatedData(pdfListList, coverFlg);
        return this.getOkResponseModel();
    }

}
