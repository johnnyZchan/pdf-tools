package com.ledi.pdftools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class PdfToolsApplication {

    public static void main(String[] args) {
        log.info("当前环境：" + System.getProperty("spring.profiles.active"));
        SpringApplication.run(PdfToolsApplication.class, args);
    }

}
