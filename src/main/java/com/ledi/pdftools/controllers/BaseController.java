package com.ledi.pdftools.controllers;

import com.ledi.pdftools.beans.ResponseModel;
import com.ledi.pdftools.constants.CodeInfo;
import com.ledi.pdftools.utils.MessageUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

public abstract class BaseController implements Serializable {

    public ResponseModel getOkResponseModel() {
        return getOkResponseModel(null);
    }

    public ResponseModel getOkResponseModel(Object data) {
        ResponseModel model = new ResponseModel();
        model.setCode(CodeInfo.CODE_OK);
        model.setMessage(MessageUtil.getMessage(CodeInfo.CODE_OK));
        model.setData(data);
        return model;
    }

    public ResponseModel getOkResponseModel(int total, Object data) {
        ResponseModel model = this.getOkResponseModel(data);
        model.setTotal(total);
        return model;
    }

    public ResponseModel getPartialOkResponseModel(List<String> failList) {
        ResponseModel model = new ResponseModel();
        model.setCode(CodeInfo.CODE_PARTIAL_OK);
        model.setMessage(MessageUtil.getMessage(CodeInfo.CODE_PARTIAL_OK, StringUtils.join(failList, ",")));
        model.setData(failList);
        return model;
    }
}
