package com.ledi.pdftools.utils;

import com.ledi.pdftools.beans.PdfModel;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

public class BeanUtil {

    public static void setFieldValue(Object bean, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = bean.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(bean, value);
    }

    public static void main(String args[]) {
        PdfModel model = new PdfModel();
        try {
            setFieldValue(model, "num", 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(model.getNum());
    }
}
