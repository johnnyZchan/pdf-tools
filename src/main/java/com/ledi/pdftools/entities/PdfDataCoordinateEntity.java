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

    public static final int ACTION_TYPE_DEL = 1;
    public static final int ACTION_TYPE_REPLACE = 2;

    private String id;
    private Integer pageNo;
    private String fieldName;
    private BigDecimal llx;
    private BigDecimal lly;
    private BigDecimal urx;
    private BigDecimal ury;
    private BigDecimal marginLeft;
    private BigDecimal marginTop;
    private BigDecimal width;
    private BigDecimal height;
    private String align;

    private String dataType;
    private Integer decimalDigits;
    private String prefix;
    private String suffix;

    private Integer actionType;

}
