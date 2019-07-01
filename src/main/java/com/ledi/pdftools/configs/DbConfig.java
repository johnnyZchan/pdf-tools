package com.ledi.pdftools.configs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.ledi.pdftools.mappers")
public class DbConfig {
}
