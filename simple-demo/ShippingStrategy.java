//Strategy-gränssnittet definierar kontraktet för alla fraktberäkningar.

public interface ShippingStrategy {
    double calculateCost(double wieghtKg);
    String getShippingName();
}
