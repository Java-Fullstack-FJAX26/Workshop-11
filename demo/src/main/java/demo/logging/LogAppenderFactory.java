package demo.logging;

import java.nio.file.Path;

public class LogAppenderFactory {
    public static LogAppender createAppender(String type, Path filePath) {
        if ("console".equalsIgnoreCase(type)) {
            return new ConsoleAppender();
        } else if ("file".equalsIgnoreCase(type)) {
            if (filePath == null) {
                throw new IllegalArgumentException("Filväg måste anges för FileAppender");
            }
            return new FileAppender(filePath);
        }
        throw new IllegalArgumentException("Okänd appender-typ: " + type);
    }
}
