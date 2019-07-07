package com.ledi.pdftools.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class DataUtil {

    public static BigDecimal round(BigDecimal value, int digit) {
        if (value != null) {
            return new BigDecimal((value.intValue() / digit) * digit);
        }

        return value;
    }

    public static String parseNumber(BigDecimal bd, int fractionDigits) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(fractionDigits);
        return nf.format(bd);
    }

    public static void main(String[] args) {
        System.out.println(parseNumber(new BigDecimal(6.3), 0));
    }
}
