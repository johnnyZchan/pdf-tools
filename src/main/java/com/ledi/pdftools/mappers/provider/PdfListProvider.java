package com.ledi.pdftools.mappers.provider;

import com.ledi.pdftools.mappers.PdfListMapper;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            if (conditions.containsKey("makeStatus")) {
                Integer makeStatus = this.obj2Integer(conditions.get("makeStatus"));
                if (makeStatus != null) {
                    sql.append(" and make_status = " + makeStatus);
                }
            }
            if (conditions.containsKey("awbList")) {
                List<String> awbList = (ArrayList<String>)conditions.get("awbList");
                if (awbList != null && !awbList.isEmpty()) {
                    sql.append(" and awb in (" + awbList.stream().map(awb -> getSqlStringEqualVal(awb)).collect(Collectors.joining(",")) + ")");
                }
            }
        }

        return sql.toString();
    }
}
