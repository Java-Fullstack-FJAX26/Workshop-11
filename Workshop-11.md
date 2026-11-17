Chas Academy

**Fördjupning Java · vecka 6 · Workshop 11: Designmönster i praktiken**

# Filbaserad loggning (Factory, Strategy, Singleton)

I denna workshop strukturerar du ett loggsystem med hjälp av designmönster. Systemet ska stödja loggnivåerna `INFO`, `WARN` och `ERROR`, utbytbara loggformat samt filskrivning med Java NIO.2 och hanterade I/O-fel.

Workshopen behandlar **tre designmönster**: Factory, Strategy och Singleton. Filskrivning och tester är praktiska moment där mönstren används.

## Upplägg

- **Förmiddag:** lärargenomgång av övning 1A, 1B och 1C. Här byggs formatterare, appenders och fabriken.
- **Eftermiddag:** eleverna implementerar själva Singleton i övning 2A och JUnit-tester i övning 2B.

## Övergripande mål

Efter workshopen ska du kunna:

- Använda **Factory** för att samla skapandet av komponenter bakom ett gemensamt gränssnitt.
- Använda **Strategy** för att byta formateringsregler under körning.
- Använda **Singleton** för en gemensam, trådsäker åtkomstpunkt till centrala resurser.
- Skriva till fil med **Java NIO.2**, `Path`, `Files.newBufferedWriter`, `StandardOpenOption.APPEND` och `try-with-resources`.
- Skriva **JUnit 5-tester med `@TempDir`** som verifierar filskrivningen och formatbytet.

## 1. Teoretisk sammanfattning

### A. Singleton Pattern

Singleton ger tillgång till en enda gemensam instans, exempelvis en `LogManager`.

- **Användning:** centrala resurser som konfiguration, trådpooler och logghantering.
- **Java:** använd en `enum`, eller en privat konstruktor med en trådsäker `getInstance()`-metod.
- **Trådsäkerhet:** även ändringar av formatterare och appenderlista behöver skyddas. En `enum` gör själva instansieringen trådsäker.

### B. Strategy Pattern

Strategy samlar olika sätt att utföra samma uppgift bakom ett gemensamt gränssnitt. Klienten kan byta strategi under körning.

- **Användning:** välj exempelvis vanlig text eller CSV för samma loggmeddelande.
- **I workshopen:** `SimpleTextFormatter` och `CsvLogFormatter` implementerar `LogFormatterStrategy`.

### C. Factory Pattern

Factory samlar koden som skapar objekt. Klienten ber om en komponent och får tillbaka den genom ett gemensamt gränssnitt.

- **Användning:** välj om en loggrad ska skrivas till konsolen eller en fil.
- **I workshopen:** `LogAppenderFactory` skapar en `ConsoleAppender` eller `FileAppender` och returnerar typen `LogAppender`.
- Övning 1C använder en **enkel statisk fabrik**.

## 2. Förmiddag: formatterare, appenders och Factory

Förmiddagen har **tre övningar**:

| Övning | Innehåll | Kodexempel |
| --- | --- | --- |
| 1A | Loggnivåer och Strategy för text/CSV | [Strategy.md](Strategy.md) |
| 1B | Konsol- och filskrivning med appenders | [Filskrivning.md](Filskrivning.md) |
| 1C | Factory som väljer appender | [Factory.md](Factory.md) |

Exemplen bygger på varandra. Spara varje klass eller gränssnitt i filen som anges ovanför kodblocket. Behåll filerna från tidigare övningar och ersätt `Main.java` med den aktuella delens demonstration.

Använd Java 17 eller senare. När Java-filerna ligger i samma övningsmapp kör du:

```powershell
javac -encoding UTF-8 -d out *.java
java -cp out Main
```

### Övning 1A: LogLevel och Strategy för formatering

1. Skapa `enum LogLevel` med `INFO`, `WARN` och `ERROR`.
2. Skapa gränssnittet `LogFormatterStrategy`:

   ```java
   @FunctionalInterface
   public interface LogFormatterStrategy {
       String format(LogLevel level, String message);
   }
   ```

3. Implementera `SimpleTextFormatter`. Den ska returnera tidsstämpel, loggnivå och meddelande:

   ```text
   [2026-11-11 10:00:00] [INFO] Grävmaskinen startar
   ```

4. Implementera `CsvLogFormatter`. Den ska returnera tidsstämpel, loggnivå och ett citerat meddelande:

   ```text
   2026-11-11 10:00:00,INFO,"Grävmaskinen startar"
   ```

   Dubbla eventuella citattecken inuti CSV-meddelandet.

Visa i `Main` hur formatteraren kan bytas från text till CSV under körning.

### Övning 1B: LogAppender och filskrivning med NIO.2

1. Skapa gränssnittet `LogAppender`:

   ```java
   public interface LogAppender {
       void append(String formattedMessage);
   }
   ```

2. Implementera `ConsoleAppender`, som skriver meddelandet till `System.out`.
3. Implementera `FileAppender`, som tar emot en `Path` i konstruktorn och skriver en loggrad i taget.
4. Använd följande anrop inuti `try-with-resources`:

   ```java
   Files.newBufferedWriter(path, StandardCharsets.UTF_8,
           StandardOpenOption.CREATE, StandardOpenOption.APPEND)
   ```

5. Skriv meddelandet och en radbrytning. Hantera `IOException` med ett tydligt felmeddelande.

`CREATE` skapar filen om den saknas. `APPEND` lägger till nya rader utan att radera tidigare innehåll. `try-with-resources` ser till att skrivaren stängs även om skrivningen misslyckas.

Visa i `Main` hur samma formaterade meddelande skickas till både konsolen och filen `workshop.log`.

### Övning 1C: LogAppenderFactory

1. Skapa `LogAppenderFactory` med den statiska metoden:

   ```java
   public static LogAppender createAppender(String type, Path filePath)
   ```

2. Returnera en `ConsoleAppender` om `type` är `"console"`. Då behövs ingen filsökväg.
3. Returnera en `FileAppender` med angiven sökväg om `type` är `"file"`.
4. Kasta `IllegalArgumentException` med ett tydligt meddelande om typen är ogiltig. Kräv en sökväg för filalternativet.

Ändra `Main` så att appenders skapas genom fabriken. Klienten ska använda gränssnittet `LogAppender`.

Efter förmiddagen kan programmet formatera loggar och skriva dem till konsol och fil. Singleton och JUnit-tester byggs under eftermiddagen.

## 3. Eftermiddag: elevernas självständiga arbete

Här anges uppgifterna. Implementationen gör eleverna själva utifrån förmiddagens klasser.

### Övning 2A: Trådsäker Singleton LogManager

1. Skapa `LogManager` som Singleton.
2. Låt den hålla en aktiv `LogFormatterStrategy` och en lista med `LogAppender`.
3. Lägg till metoder för att registrera appenders och byta formatterare.
4. Implementera:

   ```java
   public void log(LogLevel level, String message)
   ```

5. Formatera meddelandet en gång och skicka resultatet till varje appender.
6. Gör åtkomsten till formatteraren och appenderlistan trådsäker, exempelvis med synkroniserade metoder.

### Övning 2B: JUnit 5-integrationstester med @TempDir

1. Skapa testklassen `LogManagerTest`.
2. Använd `@TempDir Path tempDir` för att få en tillfällig testmapp.
3. Konfigurera `LogManager` med en `FileAppender` i testmappen. Logga tre meddelanden och verifiera med `Files.readAllLines(filePath)` att filen finns och har exakt tre rader.
4. Skriv ett test som byter formatterare under körning och verifierar att bara de nya raderna får det nya formatet.
5. Återställ Singleton-instansens inställningar mellan testerna så att testerna inte påverkar varandra.

## 4. Reflektionsfrågor

1. Varför är det bättre att klienten ber `LogAppenderFactory` om en appender än att själv skapa `new FileAppender(path)`?
2. Hur gör Strategy det enklare att lägga till exempelvis `JsonLogFormatter` utan att ändra `LogManager`?
3. Varför är `try-with-resources` viktigt när `FileAppender` skriver med `StandardOpenOption.APPEND`?
