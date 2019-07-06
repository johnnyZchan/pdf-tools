package com.ledi.pdftools.constants;

public class CodeInfo {

    /**
     * 系统级别的Code
     */
    public static final int CODE_OK = 0;
    public static final int CODE_PARTIAL_OK = 1;
    public static final int CODE_SYS_ERROR = 100001;

    /**
     * 业务级别的Code
     */
    public static final int CODE_SERVICE_ERROR = 400000;
    // PDF文件不存在
    public static final int CODE_PDF_FILE_NOT_EXIST = 400001;
    // 文件类型错误
    public static final int CODE_FILE_TYPE_ERROR = 400002;
    // 单号已存在
    public static final int CODE_AWB_ALREADY_EXIST = 400003;
    // 必须项不能为空
    public static final int CODE_PARAMS_NOT_NULL = 400004;
}
