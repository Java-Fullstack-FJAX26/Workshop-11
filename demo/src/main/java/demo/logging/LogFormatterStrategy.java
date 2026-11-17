package demo.logging;

@FunctionalInterface
public interface LogFormatterStrategy {
    String format(LogLevel level, String message);
}
