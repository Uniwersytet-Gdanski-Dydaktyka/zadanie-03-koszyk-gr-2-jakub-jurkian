import java.util.List;

public class CouponPromotion implements Promotion {
    private final String targetProductCode;
    private final double discountPercentage;

    public CouponPromotion(String targetProductCode, double discountPercentage) {
        this.targetProductCode = targetProductCode;
        this.discountPercentage = discountPercentage;
    }

    @Override
    public boolean apply(Cart cart) {
        List<Product> products = cart.getProducts();

        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            if (p.getCode().equals(targetProductCode)) {
                double newPrice = p.getPrice() * (1.0 - discountPercentage);
                cart.replaceProduct(i, p.withDiscountPrice(newPrice));
                return true;
            }
        }
        return false;
    }
}
