public class ConsoleAppender implements LogAppender {
    public void append(String formattedMessage) {
        System.out.println(formattedMessage);
    }
}
