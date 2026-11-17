package demo.logging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LogManagerTest {

    @BeforeEach
    void setUp() {
        LogManager.getInstance().clearAppenders();
        LogManager.getInstance().setFormatter(new TextLogFormatter());
    }

    @Test
    void testFileLoggingWithTempDir(@TempDir Path tempDir) throws IOException {
        Path logFile = tempDir.resolve("test-app.log");
        LogAppender fileAppender = LogAppenderFactory.createAppender("file", logFile);

        LogManager manager = LogManager.getInstance();
        manager.addAppender(fileAppender);

        manager.log(LogLevel.INFO, "Systemet har startats");
        manager.log(LogLevel.WARN, "Hög minnesanvändning noterad");

        assertTrue(Files.exists(logFile));
        List<String> lines = Files.readAllLines(logFile);
        System.out.println("Generated log entries:");
        lines.forEach(System.out::println);
        assertEquals(2, lines.size());
        assertTrue(lines.get(0).contains("[INFO] Systemet har startats"));
        assertTrue(lines.get(1).contains("[WARN] Hög minnesanvändning noterad"));
    }
}
