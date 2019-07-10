package com.ledi.pdftools.beans.ank;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShipListRespModel {

    @JSONField(name = "StatusCode")
    private Integer statusCode;
    private Integer total;
    private List<ShipModel> rows;
}
