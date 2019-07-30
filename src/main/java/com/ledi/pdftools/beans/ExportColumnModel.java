package com.ledi.pdftools.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportColumnModel {

    public ExportColumnModel(String title, String dataFieldType, String dataFieldCategory, String dataFieldName) {
        this.title = title;
        this.dataFieldType = dataFieldType;
        this.dataFieldCategory = dataFieldCategory;
        this.dataFieldName = dataFieldName;
    }

    private String title;
    private String dataFieldType;
    private String dataFieldCategory;
    private String dataFieldName;
}
