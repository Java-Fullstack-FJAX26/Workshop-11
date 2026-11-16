import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class FileAppender implements LogAppender {
    private final Path path;

    public FileAppender(Path path) {
        this.path = Objects.requireNonNull(path, "Loggfilens sökväg måste anges.");
    }

    @Override
    public synchronized void append(String formattedMessage) {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(formattedMessage);
            writer.newLine();
        } catch (IOException exception) {
            System.err.println("Kunde inte skriva loggfilen " + path + ": " + exception.getMessage());
        }
    }
}
