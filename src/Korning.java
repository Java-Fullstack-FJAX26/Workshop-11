import java.time.Duration;
import java.time.LocalDateTime;

public class Korning {
    private LocalDateTime starttid;
    private LocalDateTime sluttid;

    public Korning(LocalDateTime starttid, LocalDateTime sluttid) {
        this.starttid = starttid;
        this.sluttid = sluttid;
    }

    public void visaTid() {
        long minuter = Duration.between(starttid, sluttid).toMinutes();
        LogManager.INSTANCE.log("Starttid: " + starttid);
        LogManager.INSTANCE.log("Sluttid: " + sluttid);
        LogManager.INSTANCE.log("Körtid: " + minuter + " minuter");
    }
}
