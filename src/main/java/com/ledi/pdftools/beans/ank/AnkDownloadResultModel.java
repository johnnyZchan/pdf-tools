package com.ledi.pdftools.beans.ank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnkDownloadResultModel {

    public AnkDownloadResultModel() {
        this.totalCount = 0;
        this.successCount = 0;
        this.failCount = 0;
        this.passCount = 0;
    }

    private Integer totalCount;
    private Integer successCount;
    private Integer failCount;
    private Integer passCount;
}
