package com.ledi.pdftools.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportColumnModel {

    public ExportColumnModel(String title, String dataFieldName) {
        this.title = title;
        this.dataFieldName = dataFieldName;
    }

    private String title;
    private String dataFieldName;
}
