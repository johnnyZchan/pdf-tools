package com.ledi.pdftools.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

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

    public static String formatTimestamp2String(Timestamp time, String pattern) {
        try {
            if (time == null) {
                return null;
            }

            if (StringUtils.isBlank(pattern)) {
                pattern = "yyyy-MM-dd HH:mm:ss";
            }

            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(time);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Timestamp convertString2Timestamp(String time, String pattern) {
        try {
            if (StringUtils.isBlank(time)) {
                return null;
            }

            if (StringUtils.isBlank(pattern)) {
                pattern = "yyyy-MM-dd HH:mm:ss";
            }

            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return new Timestamp(sdf.parse(time).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        System.out.println(parseNumber(new BigDecimal(6.3), 0));
    }
}
