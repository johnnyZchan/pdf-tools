package com.ledi.pdftools.mappers.provider;

import com.ledi.pdftools.mappers.PdfListMapper;
import com.ledi.pdftools.utils.DataUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.DateUtil;

import java.sql.Timestamp;
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
            if (conditions.containsKey("awb")) {
                String awb = this.obj2String(conditions.get("awb"));
                if (StringUtils.isNotBlank(awb)) {
                    sql.append(" and awb like " + this.getSqlStringLikeVal(awb));
                }
            }
            if (conditions.containsKey("makeStartTime")) {
                Timestamp makeStartTime = this.obj2Timestamp(conditions.get("makeStartTime"));
                if (makeStartTime != null) {
                    sql.append(" and make_time >= " + this.getSqlStringEqualVal(DataUtil.formatTimestamp2String(makeStartTime, "yyyy-MM-dd HH:mm:ss")));
                }
            }
            if (conditions.containsKey("makeEndTime")) {
                Timestamp makeEndTime = this.obj2Timestamp(conditions.get("makeEndTime"));
                if (makeEndTime != null) {
                    sql.append(" and make_time <= " + this.getSqlStringEqualVal(DataUtil.formatTimestamp2String(makeEndTime, "yyyy-MM-dd HH:mm:ss")));
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
            if (conditions.containsKey("awb")) {
                String awb = this.obj2String(conditions.get("awb"));
                if (StringUtils.isNotBlank(awb)) {
                    sql.append(" and awb like " + this.getSqlStringLikeVal(awb));
                }
            }
            if (conditions.containsKey("makeStartTime")) {
                Timestamp makeStartTime = this.obj2Timestamp(conditions.get("makeStartTime"));
                if (makeStartTime != null) {
                    sql.append(" and make_time >= " + this.getSqlStringEqualVal(DataUtil.formatTimestamp2String(makeStartTime, "yyyy-MM-dd HH:mm:ss")));
                }
            }
            if (conditions.containsKey("makeEndTime")) {
                Timestamp makeEndTime = this.obj2Timestamp(conditions.get("makeEndTime"));
                if (makeEndTime != null) {
                    sql.append(" and make_time <= " + this.getSqlStringEqualVal(DataUtil.formatTimestamp2String(makeEndTime, "yyyy-MM-dd HH:mm:ss")));
                }
            }
        }

        return sql.toString();
    }
}
