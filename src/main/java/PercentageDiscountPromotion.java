import java.util.List;

public class PercentageDiscountPromotion implements Promotion {
    private final double percentage;

    public PercentageDiscountPromotion(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public boolean apply(Cart cart) {
        List<Product> products = cart.getProducts();
        if (products.isEmpty()) return false;
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            cart.replaceProduct(i, p.withDiscountPrice(p.getDiscountPrice() * (1.0 - percentage)));
        }
        return true;
    }
}
