public class StandardShippingStrategy implements ShippingStrategy {
    @Override
    public double calculateCost(double weightKg) {
        return 50.0 + (weightKg * 10.0);
    }

    @Override
    public String getShippingName() {
        return "Standardfrakt (3-5 dagar)";
    }
}
