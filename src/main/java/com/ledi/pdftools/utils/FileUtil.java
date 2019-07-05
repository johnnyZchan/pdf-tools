package com.ledi.pdftools.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

@Slf4j
public class FileUtil {

    public static void deleteFile(String path) {
        if (StringUtils.isNotBlank(path)) {
            File file = new File(path);
            deleteFile(file);
        }
    }

    public static void deleteFile(File file) {
        try {
            if (file != null && file.exists() && file.isFile()) {
                file.delete();
            }
        } catch (Exception e) {
            log.error("error occurred : ", e);
        }
    }
}
