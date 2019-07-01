package com.ledi.pdftools.handlers;

import com.ledi.pdftools.beans.ResponseModel;
import com.ledi.pdftools.constants.CodeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class SpringExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ResponseModel> handlerOtherExceptions(final Exception ex) {
        log.error(ex.getMessage(), ex);

        int code = CodeInfo.CODE_SYS_ERROR;
        String message = "系统错误";

        ResponseModel model = new ResponseModel();
        model.setCode(code);
        model.setMessage(message);
        return new ResponseEntity(model, HttpStatus.OK);
    }
}
