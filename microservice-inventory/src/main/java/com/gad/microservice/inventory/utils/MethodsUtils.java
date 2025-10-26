package com.gad.microservice.inventory.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MethodsUtils {
    private MethodsUtils() {
    }

    public static String datetimeNowFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
}
