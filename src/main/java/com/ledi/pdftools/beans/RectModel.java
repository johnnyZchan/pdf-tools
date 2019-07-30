package com.ledi.pdftools.beans;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class RectModel {

    private BigDecimal llx;
    private BigDecimal lly;
    private BigDecimal urx;
    private BigDecimal ury;
}
