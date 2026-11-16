import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Path loggfil = Paths.get("app.log");
        LogManager logger = LogManager.INSTANCE;
        logger.reset();
        logger.addAppender(LogAppenderFactory.createAppender("console", null));
        logger.addAppender(LogAppenderFactory.createAppender("file", loggfil));
        List<String> rader = Files.readAllLines(Paths.get("korningar.log"));

        for (int i = 1; i < rader.size(); i++) {
            String[] delar = rader.get(i).split(";");
            Chauffor chauffor = new Chauffor(delar[0]);
            Gravmaskin gravmaskin = new Gravmaskin(delar[1]);
            LocalDateTime starttid = LocalDateTime.parse(delar[2]);
            LocalDateTime sluttid = LocalDateTime.parse(delar[3]);
            Korning korning = new Korning(starttid, sluttid);

            logger.log(LogLevel.INFO, "Chaufför: " + chauffor.getNamn());
            logger.log(LogLevel.INFO, "Grävmaskin: " + gravmaskin.getModell());
            korning.visaTid();
            System.out.println();
        }
        logger.setFormatter(new CsvLogFormatter());
        logger.log(LogLevel.INFO, "Alla körningar är inlästa.");
        logger.log(LogLevel.WARN, "Demo: grävmaskinen behöver service.");
        logger.log(LogLevel.ERROR, "Demo: grävmaskinens motor startar inte.");
        System.out.println("Loggmeddelandena sparas även i " + loggfil);
    }
}
