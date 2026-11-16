import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LogManagerTest {
    private static final String TIMESTAMP = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
    private final LogManager logger = LogManager.INSTANCE;

    @TempDir
    Path tempDir;

    @BeforeEach
    void configureLogger() {
        logger.reset();
    }

    @AfterEach
    void resetLogger() {
        logger.reset();
    }

    @Test
    void writesExactlyThreeMessagesWithLevelsAndTimestamps() throws IOException {
        Path logFile = tempDir.resolve("korningar.log");
        logger.addAppender(LogAppenderFactory.createAppender("file", logFile));

        logger.log(LogLevel.INFO, "Grävmaskinen startar");
        logger.log(LogLevel.WARN, "Låg bränslenivå");
        logger.log(LogLevel.ERROR, "Hydraulikfel");

        assertTrue(Files.exists(logFile));
        List<String> lines = Files.readAllLines(logFile, StandardCharsets.UTF_8);
        assertEquals(3, lines.size());
        assertTrue(lines.get(0).matches("\\[" + TIMESTAMP + "\\] \\[INFO\\] Grävmaskinen startar"));
        assertTrue(lines.get(1).matches("\\[" + TIMESTAMP + "\\] \\[WARN\\] Låg bränslenivå"));
        assertTrue(lines.get(2).matches("\\[" + TIMESTAMP + "\\] \\[ERROR\\] Hydraulikfel"));
    }

    @Test
    void formatChangeAffectsOnlyNewMessages() throws IOException {
        Path logFile = tempDir.resolve("format.log");
        logger.addAppender(LogAppenderFactory.createAppender("file", logFile));
        logger.log(LogLevel.INFO, "Första körningen");
        String originalLine = Files.readAllLines(logFile).get(0);

        logger.setFormatter(new CsvLogFormatter());
        logger.log(LogLevel.WARN, "Andra körningen");

        List<String> lines = Files.readAllLines(logFile);
        assertEquals(2, lines.size());
        assertEquals(originalLine, lines.get(0));
        assertTrue(lines.get(1).matches(TIMESTAMP + ",WARN,\"Andra körningen\""));
    }

    @Test
    void appendsWithoutErasingPreviousMessages() throws IOException {
        Path logFile = tempDir.resolve("befintlig.log");
        Files.write(logFile, List.of("Tidigare körning"), StandardCharsets.UTF_8);
        logger.addAppender(LogAppenderFactory.createAppender("file", logFile));

        logger.log(LogLevel.INFO, "Ny körning");

        List<String> lines = Files.readAllLines(logFile);
        assertEquals(2, lines.size());
        assertEquals("Tidigare körning", lines.get(0));
        assertTrue(lines.get(1).endsWith(" [INFO] Ny körning"));
    }

    @Test
    void csvPreservesUtf8AndEscapesQuotes() throws IOException {
        Path logFile = tempDir.resolve("svenska.csv");
        logger.addAppender(LogAppenderFactory.createAppender("file", logFile));
        logger.setFormatter(new CsvLogFormatter());

        logger.log(LogLevel.ERROR, "Delzar kör \"Volvo\", nära grävmaskinen.");

        List<String> lines = Files.readAllLines(logFile, StandardCharsets.UTF_8);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).matches(TIMESTAMP + ",ERROR,.*"));
        assertTrue(lines.get(0).endsWith(",ERROR,\"Delzar kör \"\"Volvo\"\", nära grävmaskinen.\""));
    }

    @Test
    void messagesWithNewlinesRemainSinglePhysicalLines() throws IOException {
        Path logFile = tempDir.resolve("radbrytningar.log");
        logger.addAppender(LogAppenderFactory.createAppender("file", logFile));
        logger.log(LogLevel.INFO, "Start\r\nSlut");

        logger.setFormatter(new CsvLogFormatter());
        logger.log(LogLevel.WARN, "Rad ett\nRad två");

        List<String> lines = Files.readAllLines(logFile);
        assertEquals(2, lines.size());
        assertTrue(lines.get(0).endsWith(" [INFO] Start\\r\\nSlut"));
        assertTrue(lines.get(1).endsWith(",WARN,\"Rad ett\\nRad två\""));
    }

    @Test
    void failingAppenderDoesNotStopOtherAppenders() throws IOException {
        Path logFile = tempDir.resolve("fungerande.log");
        logger.addAppender(message -> {
            throw new IllegalStateException("Simulerat appenderfel");
        });
        logger.addAppender(LogAppenderFactory.createAppender("file", logFile));

        assertDoesNotThrow(() -> logger.log(LogLevel.INFO, "Körningen fortsätter"));

        List<String> lines = Files.readAllLines(logFile);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).endsWith(" [INFO] Körningen fortsätter"));
    }

    @Test
    void fileWriteFailureDoesNotCrashLogging() throws IOException {
        Path logFile = tempDir.resolve("reserv.log");
        logger.addAppender(LogAppenderFactory.createAppender("file", tempDir));
        logger.addAppender(LogAppenderFactory.createAppender("file", logFile));

        assertDoesNotThrow(() -> logger.log(LogLevel.ERROR, "Det första filmålet är en mapp"));

        List<String> lines = Files.readAllLines(logFile);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).endsWith(" [ERROR] Det första filmålet är en mapp"));
    }

    @Test
    void concurrentLoggingWritesEveryMessageOnceAsACompleteLine() throws Exception {
        Path logFile = tempDir.resolve("parallella-korningar.log");
        logger.addAppender(LogAppenderFactory.createAppender("file", logFile));
        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Callable<Void>> jobs = new ArrayList<>();
        Set<String> expectedMessages = new HashSet<>();
        for (int i = 0; i < 40; i++) {
            String message = "Körning " + i;
            expectedMessages.add(message);
            jobs.add(() -> {
                LogManager.INSTANCE.log(LogLevel.INFO, message);
                return null;
            });
        }

        try {
            for (Future<Void> result : executor.invokeAll(jobs, 10, TimeUnit.SECONDS)) {
                result.get();
            }
        } finally {
            executor.shutdownNow();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
        }

        List<String> lines = Files.readAllLines(logFile);
        assertEquals(40, lines.size());
        Set<String> actualMessages = new HashSet<>();
        for (String line : lines) {
            assertTrue(line.matches("\\[" + TIMESTAMP + "\\] \\[INFO\\] Körning \\d+"));
            actualMessages.add(line.substring(line.indexOf("Körning ")));
        }
        assertEquals(expectedMessages, actualMessages);
    }

    @Test
    void factoryCreatesAppendersAndRejectsUnknownTypes() {
        assertInstanceOf(ConsoleAppender.class, LogAppenderFactory.createAppender("console", null));
        assertInstanceOf(FileAppender.class,
                LogAppenderFactory.createAppender("file", tempDir.resolve("factory.log")));
        assertThrows(IllegalArgumentException.class,
                () -> LogAppenderFactory.createAppender("okand", null));
        assertThrows(IllegalArgumentException.class,
                () -> LogAppenderFactory.createAppender(null, null));
        assertThrows(IllegalArgumentException.class,
                () -> LogAppenderFactory.createAppender("file", null));
    }
}
