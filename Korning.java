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
        System.out.println("Starttid: " + starttid);
        System.out.println("Sluttid: " + sluttid);
        System.out.println("Körtid: " + minuter + " minuter");
    }
}
