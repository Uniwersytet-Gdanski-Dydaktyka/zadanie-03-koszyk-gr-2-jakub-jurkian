public class Product {
    private final String code;
    private final String name;
    private final double price;
    private final double discountPrice;

    public Product(String code, String name, double price) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.discountPrice = price;
    }

    // Used when copying the object with a changed discount price
    private Product(String code, String name, double price, double discountPrice) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.discountPrice = discountPrice;
    }

    public Product withDiscountPrice(double newDiscountPrice) {
        double finalPrice = Math.max(0.0, newDiscountPrice); // Edge case protection
        return new Product(this.code, this.name, this.price, finalPrice);
    }

    // Getters

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    public double getDiscountPrice() {
        return this.discountPrice;
    }

    @Override
    public String toString() {
        return String.format("%s (%s): %.2f PLN (Promotion: %.2f PLN)", name, code, price, discountPrice);
    }

    // sorting methods (static)

    public static java.util.Comparator<Product> byName() {
        return (p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName());
    }

    public static java.util.Comparator<Product> byPrice() {
        return (p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice());
    }
}