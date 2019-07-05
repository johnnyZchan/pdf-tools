package com.ledi.pdftools.utils;

import java.math.BigDecimal;

public class DataUtil {

    public static BigDecimal round(BigDecimal value, int digit) {
        if (value != null) {
            return new BigDecimal((value.intValue() / digit) * digit);
        }

        return value;
    }
}
