// 2. Expressfrakt: 150 kr grundavgift + 25 kr/kg
public class ExpressShippingStrategy implements ShippingStrategy {
    @Override
    public double calculateCost(double weightKg) {
        return 150.0 + (weightKg * 25.0);
    }

    @Override
    public String getShippingName() {
        return "Expressfrakt (Nästa dag)";
    }
}
