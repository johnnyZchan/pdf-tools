package com.ledi.pdftools;

import com.aspose.pdf.License;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStream;

@SpringBootApplication
@Slf4j
public class PdfToolsApplication {

    public static void main(String[] args) {
        log.info("当前环境：" + System.getProperty("spring.profiles.active"));
        SpringApplication.run(PdfToolsApplication.class, args);

        InputStream license = PdfToolsApplication.class.getClassLoader().getResourceAsStream("license.xml");
        try {
            new License().setLicense(license);
        } catch (Exception e) {
            log.warn("Error occurred in aspose license read!!!", e);
        }
    }

}
