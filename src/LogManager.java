import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public enum LogManager {
    INSTANCE;

    private LogFormatterStrategy formatter = new SimpleTextFormatter();
    private final List<LogAppender> appenders = new ArrayList<>();

    public synchronized void addAppender(LogAppender appender) {
        appenders.add(Objects.requireNonNull(appender, "En appender måste anges."));
    }

    public synchronized void setFormatter(LogFormatterStrategy formatter) {
        this.formatter = Objects.requireNonNull(formatter, "En formatterare måste anges.");
    }

    public synchronized void log(LogLevel level, String message) {
        Objects.requireNonNull(level, "En loggnivå måste anges.");
        Objects.requireNonNull(message, "Ett loggmeddelande måste anges.");
        String formattedMessage = formatter.format(level, message);
        for (LogAppender appender : appenders) {
            try {
                appender.append(formattedMessage);
            } catch (RuntimeException exception) {
                System.err.println("En appender misslyckades: " + exception.getMessage());
            }
        }
    }

    public void log(String message) {
        log(LogLevel.INFO, message);
    }

    public synchronized void reset() {
        appenders.clear();
        formatter = new SimpleTextFormatter();
    }
}
