import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        List<String> rader = Files.readAllLines(Paths.get("korningar.log"));
        LogManager logger = LogManager.INSTANCE;

        for (int i = 1; i < rader.size(); i++) {
            String[] delar = rader.get(i).split(";");
            Chauffor chauffor = new Chauffor(delar[0]);
            Gravmaskin gravmaskin = new Gravmaskin(delar[1]);
            LocalDateTime starttid = LocalDateTime.parse(delar[2]);
            LocalDateTime sluttid = LocalDateTime.parse(delar[3]);
            Korning korning = new Korning(starttid, sluttid);

            logger.log("Chaufför: " + chauffor.getNamn());
            logger.log("Grävmaskin: " + gravmaskin.getModell());
            korning.visaTid();
            System.out.println();
        }
        logger.setFormatter(new CsvLogFormatter());
        logger.log("Alla körningar är inlästa.");
    }
}
