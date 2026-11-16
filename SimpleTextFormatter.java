import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SimpleTextFormatter implements LogFormatterStrategy {
    private static final DateTimeFormatter TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String format(LogLevel level, String message) {
        String singleLine = message.replace("\r", "\\r").replace("\n", "\\n");
        return "[" + LocalDateTime.now().format(TIMESTAMP) + "] [" + level + "] " + singleLine;
    }
}
