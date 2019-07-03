package com.ledi.pdftools.mappers.provider;

import com.ledi.pdftools.mappers.PdfListMapper;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class PdfListProvider extends BaseProvider {

    public String findByCondition(Map<String, Object> conditions, String orderSql, Integer start, Integer length) {
        StringBuffer sql = new StringBuffer("");
        sql.append("select " + PdfListMapper.COLUMNS);
        sql.append(" from pdf_list");
        sql.append(" where del_status = " + PdfListMapper.DEL_STATUS_NO);
        if (conditions != null) {
            if (conditions.containsKey("type")) {
                Integer type = this.obj2Integer(conditions.get("type"));
                if (type != null) {
                    sql.append(" and type = " + type);
                }
            }
        }
        if (StringUtils.isNotBlank(orderSql)) {
            sql.append(" order by " + orderSql);
        }
        if (start != null && length != null) {
            sql.append(" limit " + start + ", " + length);
        }

        return sql.toString();
    }

    public String countByCondition(Map<String, Object> conditions) {
        StringBuffer sql = new StringBuffer("");
        sql.append("select count(1)");
        sql.append(" from pdf_list");
        sql.append(" where del_status = " + PdfListMapper.DEL_STATUS_NO);
        if (conditions != null) {
            if (conditions.containsKey("type")) {
                Integer type = this.obj2Integer(conditions.get("type"));
                if (type != null) {
                    sql.append(" and type = " + type);
                }
            }
        }

        return sql.toString();
    }
}
