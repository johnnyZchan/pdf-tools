package com.ledi.pdftools.utils;

import com.fasterxml.uuid.Generators;

import java.util.UUID;

public class IDUtil {

    public static String uuid() {
        UUID id = Generators.timeBasedGenerator().generate();
        return id.toString().replaceAll("-", "");
    }
}
