public class Main {
    public static void main(String[] args) {
        LogManager logger = LogManager.INSTANCE;
        String meddelande = "Delzar startar grävmaskinen.";
        logger.log(meddelande);
        logger.setFormatter(new CsvLogFormatter());
        logger.log(meddelande);
    }
}
