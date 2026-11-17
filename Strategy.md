# Förmiddag 1A: LogLevel och Strategy

Detta exempel följer övning 1A. Strategin bestämmer loggformatet och kan bytas medan programmet körs.

## LogLevel.java

```java
public enum LogLevel {
    INFO,
    WARN,
    ERROR
}
```

## LogFormatterStrategy.java

```java
@FunctionalInterface
public interface LogFormatterStrategy {
    String format(LogLevel level, String message);
}
```

## SimpleTextFormatter.java

```java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class SimpleTextFormatter implements LogFormatterStrategy {
    private static final DateTimeFormatter TID = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Override
    public String format(LogLevel level, String message) {
        return "[" + LocalDateTime.now().format(TID) + "] [" + level + "] " + message;
    }
}
```

## CsvLogFormatter.java

```java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class CsvLogFormatter implements LogFormatterStrategy {
    private static final DateTimeFormatter TID = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Override
    public String format(LogLevel level, String message) {
        String escaped = message.replace("\"", "\"\"");
        return LocalDateTime.now().format(TID) + "," + level + ",\"" + escaped + "\"";
    }
}
```

## Main.java

```java
public class Main {
    public static void main(String[] args) {
        LogFormatterStrategy formatter = new SimpleTextFormatter();
        System.out.println(formatter.format(LogLevel.INFO, "Grävmaskinen startar"));
        formatter = new CsvLogFormatter();
        System.out.println(formatter.format(LogLevel.WARN, "Låg bränslenivå"));
        System.out.println(formatter.format(LogLevel.ERROR, "Motorfel"));
    }
}
```

Spara kodblocken i filerna som anges i rubrikerna. Kör från samma mapp:

```powershell
javac -encoding UTF-8 -d out *.java
java -cp out Main
```

De tre utskrifterna får tidsstämpel och loggnivå. Den första använder textformat; de två sista använder CSV.

Behåll dessa filer till övning 1B. Där ersätts bara demonstrationen i `Main.java`.
