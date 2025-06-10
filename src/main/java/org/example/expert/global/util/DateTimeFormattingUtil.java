package org.example.expert.global.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class DateTimeFormattingUtil {
    public static String formattedNow(String format) {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern(format));
    }
}
