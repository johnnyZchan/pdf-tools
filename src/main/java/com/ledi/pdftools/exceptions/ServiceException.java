package com.ledi.pdftools.exceptions;

import com.ledi.pdftools.constants.CodeInfo;
import com.ledi.pdftools.utils.MessageUtil;
import org.apache.commons.lang3.StringUtils;

public class ServiceException extends BaseException{

    public ServiceException() {
        super(CodeInfo.CODE_SERVICE_ERROR, MessageUtil.getMessage(CodeInfo.CODE_SERVICE_ERROR));
    }

    public ServiceException(Integer code) {
        super(code, StringUtils.isNotBlank(MessageUtil.getMessage(code))?MessageUtil.getMessage(code):MessageUtil.getMessage(CodeInfo.CODE_SERVICE_ERROR));
    }

    public ServiceException(String message) {
        super(CodeInfo.CODE_SERVICE_ERROR, message);
    }

    public ServiceException(Integer code, String message) {
        super(code, message);
    }
}
