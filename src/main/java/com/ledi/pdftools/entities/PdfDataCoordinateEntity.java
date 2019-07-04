package com.ledi.pdftools.entities;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PdfDataCoordinateEntity {

    public static final String ALIGN_LEFT = "left";
    public static final String ALIGN_RIGHT = "right";

    public static final String DATA_TYPE_STRING = "String";
    public static final String DATA_TYPE_INTEGER = "Integer";
    public static final String DATA_TYPE_DECIMAL = "Decimal";

    private String id;
    private Integer page;
    private String key;
    private BigDecimal llx;
    private BigDecimal lly;
    private BigDecimal urx;
    private BigDecimal ury;
    private BigDecimal left;
    private BigDecimal top;
    private BigDecimal width;
    private BigDecimal height;
    private String align;

    private String dataType;
    private Integer decimalDigits;
    private String prefix;
    private String suffix;

}
