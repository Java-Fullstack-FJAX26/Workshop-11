package demo.logging;

import java.util.ArrayList;
import java.util.List;

public class LogManager {
    private static LogManager instance;
    private LogFormatterStrategy formatter;
    private final List<LogAppender> appenders = new ArrayList<>();

    private LogManager() {
        this.formatter = new TextLogFormatter();
    }

    public static synchronized LogManager getInstance() {
        if (instance == null) {
            instance = new LogManager();
        }
        return instance;
    }

    public void setFormatter(LogFormatterStrategy formatter) {
        this.formatter = formatter;
    }

    public void addAppender(LogAppender appender) {
        this.appenders.add(appender);
    }

    public void clearAppenders() {
        this.appenders.clear();
    }

    public void log(LogLevel level, String message) {
        String formatted = formatter.format(level, message);
        for (LogAppender appender : appenders) {
            appender.append(formatted);
        }
    }
}
