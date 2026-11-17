package demo.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TextLogFormatter implements LogFormatterStrategy {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String format(LogLevel level, String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        return String.format("[%s] [%s] %s", timestamp, level, message);
    }
}
