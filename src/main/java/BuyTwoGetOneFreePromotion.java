import java.util.List;

public class BuyTwoGetOneFreePromotion implements Promotion {
    @Override
    public boolean apply(Cart cart) {
        if (cart.getSize() < 3) return false;

        Product cheapest = cart.findCheapest();
        List<Product> products = cart.getProducts();

        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            if (p.getCode().equals(cheapest.getCode()) && p.getDiscountPrice() > 0.0) {
                cart.replaceProduct(i, cheapest.withDiscountPrice(0.0));
                return true;
            }
        }
        return false;
    }
}
