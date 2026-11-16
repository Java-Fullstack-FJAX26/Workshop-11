public enum LogManager {
    INSTANCE;
    private LogFormatterStrategy formatter = new SimpleTextFormatter();
    private final LogAppender appender = LogAppenderFactory.createAppender();

    public synchronized void setFormatter(LogFormatterStrategy formatter) {
        this.formatter = formatter;
    }

    public synchronized void log(String message) {
        appender.append(formatter.format(message));
    }
}
