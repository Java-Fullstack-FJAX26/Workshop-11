# Övning 1C: Factory för loggskrivare

Behåll klasserna från 1A och 1B. Lägg till fabriken och ersätt `Main.java` med exemplet nedan.

## LogAppenderFactory.java

```java
import java.nio.file.Path;
public class LogAppenderFactory {
    public static LogAppender createAppender(String type, Path filePath) {
        if ("console".equals(type)) {
            return new ConsoleAppender();
        }
        if ("file".equals(type)) {
            if (filePath == null) {
                throw new IllegalArgumentException("En loggfil måste anges.");
            }
            return new FileAppender(filePath);
        }
        throw new IllegalArgumentException("Okänd appendertyp: " + type);
    }
}
```

## Main.java

```java
import java.nio.file.Path;
public class Main {
    public static void main(String[] args) {
        LogAppender console = LogAppenderFactory.createAppender("console", null);
        LogAppender file = LogAppenderFactory.createAppender("file", Path.of("workshop.log"));
        LogFormatterStrategy format = new SimpleTextFormatter();
        String message = format.format(LogLevel.INFO, "Grävmaskinen startar");
        console.append(message);
        file.append(message);
        format = new CsvLogFormatter();
        message = format.format(LogLevel.WARN, "Grävmaskinen behöver service");
        console.append(message);
        file.append(message);
    }
}
```

Spara varje klass i filen som rubriken anger. Kör från mappen med Java-filerna:

```powershell
javac -encoding UTF-8 -d out *.java
java -cp out Main
```

Två meddelanden skrivs till både terminalen och `workshop.log`.

Fabriken skapar rätt konkret klass. `Main` använder det gemensamma gränssnittet `LogAppender`. En ogiltig typ ger `IllegalArgumentException`.

Detta är en enkel statisk fabrik, som övningens metod beskriver.
