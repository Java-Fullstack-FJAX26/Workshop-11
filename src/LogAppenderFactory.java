import java.nio.file.Path;

public class LogAppenderFactory {
    public static LogAppender createAppender(String type, Path filePath) {
        if ("console".equals(type)) {
            return new ConsoleAppender();
        }
        if ("file".equals(type)) {
            if (filePath == null) {
                throw new IllegalArgumentException("En filappender kräver en sökväg.");
            }
            return new FileAppender(filePath);
        }
        throw new IllegalArgumentException("Okänd appendertyp: " + type);
    }
}
