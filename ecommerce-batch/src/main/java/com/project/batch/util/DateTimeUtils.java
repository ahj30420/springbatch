package com.project.batch.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss.SSS");

    public static LocalDate toLocalDate(String date) {
        return LocalDate.parse(date, dateFormat);
    }

    public static LocalDateTime toLocalDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, dateTimeFormat);
    }

    public static String toString(LocalDateTime dateTime) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(dateTime);
    }
}
