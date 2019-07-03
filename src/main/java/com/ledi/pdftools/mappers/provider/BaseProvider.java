package com.ledi.pdftools.mappers.provider;

import java.sql.Timestamp;
import java.util.Date;

public abstract class BaseProvider {

    public String obj2String(Object obj) {
        String result = null;
        if (obj != null && obj instanceof String) {
            result = String.valueOf(obj);
        }

        return result;
    }

    public Integer obj2Integer(Object obj) {
        Integer result = null;
        if (obj != null && obj instanceof Integer) {
            result = Integer.parseInt(String.valueOf(obj));
        }

        return result;
    }

    public Timestamp obj2Timestamp(Object obj) {
        Timestamp result = null;
        if (obj != null) {
            if (obj instanceof Timestamp) {
                result = (Timestamp)obj;
            } else if (obj instanceof Date) {
                result = new Timestamp(((Date)obj).getTime());
            }
        }

        return result;
    }

    public String getSqlStringEqualVal(String val) {
        return "'" + val + "'";
    }

    public String getSqlStringLikeVal(String val) {
        return "'%" + val + "%'";
    }
}
