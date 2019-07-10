package com.ledi.pdftools.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
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

    public static void copyFile(File srcFile, File destFile) throws Exception {
        FileUtils.copyFile(srcFile, destFile);
    }

    public static void main(String[] args) {
        File file = new File("/Users/johnny/Development/ledi/8009180462.pdf");
        System.out.println(file.getAbsolutePath());
        System.out.println(file.getName());
    }
}
