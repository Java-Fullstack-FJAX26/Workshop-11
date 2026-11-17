package demo.logging;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        LogManager logManager = LogManager.getInstance();
        logManager.addAppender(LogAppenderFactory.createAppender(
                "file", Path.of("src", "main", "resources", "log.log")));
        logManager.addAppender(LogAppenderFactory.createAppender(
                "file", Path.of("src", "main", "resources", "timestwo.log")));        
        logManager.log(LogLevel.INFO, "Hello from the log manager!");
    }
}