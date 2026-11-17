import java.util.List;

public class ShippingDemo {



    // --- 4. EXEMPELDATA OCH MAIN ---
    record Order(String orderId, double weightKg, String requestedShippingType) {}

    public static void main(String[] args) {
        System.out.println("=== DEMO: STRATEGY & FACTORY PATTERN ===");

        // Exempeldata: 3 olika beställningar
        List<Order> orders = List.of(
            new Order("ORD-101", 2.5, "STANDARD"),
            new Order("ORD-102", 4.0, "EXPRESS"),
            new Order("ORD-103", 1.2, "FREE")
        );

        for (Order order : orders) {
            // 1. Använd Factory för att skapa rätt strategi
            ShippingStrategy strategy = ShippingStrategyFactory.getStrategy(order.requestedShippingType());

            // 2. Beräkna fraktkostnad via Strategy-gränssnittet
            double cost = strategy.calculateCost(order.weightKg());

            // 3. Skriv ut resultat
            System.out.printf("Order: %s | Vikt: %.1f kg | Metod: %-13s | Kostnad: %.2f kr%n",
                    order.orderId(), order.weightKg(), strategy.getShippingName(), cost);
        }
    }
}