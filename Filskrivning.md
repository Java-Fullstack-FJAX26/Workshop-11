# Förmiddag 1B: konsol- och filskrivning

Behåll formatterarna och `LogLevel` från 1A. Här får båda skrivarna samma färdiga loggrad.

Spara varje klass och gränssnitt i en egen fil med namnet nedan.

## LogAppender.java

```java
public interface LogAppender {
    void append(String formattedMessage);
}
```

## ConsoleAppender.java

```java
public class ConsoleAppender implements LogAppender {
    @Override
    public void append(String formattedMessage) {
        System.out.println(formattedMessage);
    }
}
```

## FileAppender.java

```java
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class FileAppender implements LogAppender {
    private final Path path;

    public FileAppender(Path path) {
        this.path = path;
    }

    @Override
    public void append(String formattedMessage) {
        try (var writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(formattedMessage);
            writer.newLine();
        } catch (IOException error) {
            System.err.println("Kunde inte skriva loggfilen: " + error.getMessage());
        }
    }
}
```

## Main.java

Ersätt `Main.java` från 1A med detta exempel.

```java
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        LogFormatterStrategy formatter = new SimpleTextFormatter();
        String rad = formatter.format(LogLevel.INFO, "Grävmaskinen startar");
        LogAppender konsol = new ConsoleAppender();
        LogAppender fil = new FileAppender(Path.of("workshop.log"));
        konsol.append(rad);
        fil.append(rad);
    }
}
```

Kompilera och kör från mappen med Java-filerna:

```powershell
javac -encoding UTF-8 -d out *.java
java -cp out Main
```

Loggraden visas i terminalen och sparas i `workshop.log`. Kör igen: filen får en ny rad och de gamla raderna finns kvar.

`CREATE` skapar filen om den saknas. `APPEND` lägger till rader. `try-with-resources` stänger filskrivaren automatiskt, och `catch` rapporterar I/O-fel.
