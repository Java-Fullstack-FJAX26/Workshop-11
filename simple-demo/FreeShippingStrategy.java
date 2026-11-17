public class FreeShippingStrategy implements ShippingStrategy {
    @Override
    public double calculateCost(double weightKg) {
        return 0.0;
    }

    @Override
    public String getShippingName() {
        return "Fri frakt (Kampanj)";
    }
}