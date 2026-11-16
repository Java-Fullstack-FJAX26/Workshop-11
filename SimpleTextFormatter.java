public class SimpleTextFormatter implements LogFormatterStrategy {
    public String format(String message) {
        return "[INFO] " + message;
    }
}
