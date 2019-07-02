package com.ledi.pdftools.configs;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.ledi.pdftools.mappers", sqlSessionFactoryRef = "sessionFactory")
public class DbConfig {

    @Bean
    public SqlSessionFactory sessionFactory(DataSource druidDs) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(druidDs);
        return sessionFactory.getObject();
    }
}
