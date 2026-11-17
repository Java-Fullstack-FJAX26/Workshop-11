package demo.logging;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileAppender implements LogAppender {
    private final Path filePath;

    public FileAppender(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public void append(String formattedMessage) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            writer.write(formattedMessage);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Kunde inte skriva logg till fil: " + filePath, e);
        }
    }
}
