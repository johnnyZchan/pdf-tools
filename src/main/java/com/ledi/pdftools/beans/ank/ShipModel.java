package com.ledi.pdftools.beans.ank;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShipModel {

    @JSONField(name = "MAWB_NO")
    private String mawbNo;

    @JSONField(name = "INVOICE_NO")
    private String invoiceNo;

    @JSONField(name = "PERMIT_DTIME")
    private String permitTime;
}
