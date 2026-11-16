public class CsvLogFormatter implements LogFormatterStrategy {
    public String format(String message) {
        return "INFO,\"" + message.replace("\"", "\"\"") + "\"";
    }
}
