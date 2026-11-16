public class LogAppenderFactory {
    public static LogAppender createAppender() {
        return new ConsoleAppender();
    }
}
