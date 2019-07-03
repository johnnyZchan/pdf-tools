package com.ledi.pdftools.beans;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseModel<T> implements Serializable {

    private Integer code;
    private String message;
    private T data;
    private Integer total;
}
