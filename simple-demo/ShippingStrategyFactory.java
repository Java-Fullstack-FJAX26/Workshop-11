//Factory-klassen skapar och returnerar rätt fraktstrategi baserat på en kod eller val.
public class ShippingStrategyFactory {
    public static ShippingStrategy getStrategy(String shippingType) {
        if (shippingType == null) {
            throw new IllegalArgumentException("Frakttyp kan inte vara null");
        }
        return switch (shippingType.toUpperCase().trim()) {
            case "STANDARD" -> new StandardShippingStrategy();
            case "EXPRESS"  -> new ExpressShippingStrategy();
            case "FREE"     -> new FreeShippingStrategy();
            default -> throw new IllegalArgumentException("Okänd Frakttyp");
        };
    }
}
