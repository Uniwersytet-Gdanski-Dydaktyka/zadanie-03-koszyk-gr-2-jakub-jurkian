import java.util.List;

public class ValueBasedDiscountPromotion implements Promotion {
    private final double threshold;
    private final double percentage;

    public ValueBasedDiscountPromotion(double threshold, double percentage) {
        this.threshold = threshold;
        this.percentage = percentage;
    }

    @Override
    public boolean apply(Cart cart) {
        if (cart.calculateOriginalTotal() <= threshold) return false;
        List<Product> products = cart.getProducts();
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            cart.replaceProduct(i, p.withDiscountPrice(p.getDiscountPrice() * (1.0 - percentage)));
        }
        return true;
    }
}
