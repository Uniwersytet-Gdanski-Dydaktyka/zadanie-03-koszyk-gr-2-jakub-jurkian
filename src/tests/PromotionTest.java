import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

public class PromotionTest {

    // ── BuyTwoGetOneFreePromotion ────────────────────────────────────────────

    @Test
    void buyTwoGetOneFree_shouldMakeCheapestProductFree() {
        Cart cart = new Cart();
        Product p1 = new Product("A", "Mleko", 10.0);
        Product p2 = new Product("B", "Chleb", 20.0);
        Product p3 = new Product("C", "Masło", 30.0);
        cart.addProduct(p1);
        cart.addProduct(p2);
        cart.addProduct(p3);

        new BuyTwoGetOneFreePromotion().apply(cart);

        List<Product> products = cart.getProducts();
        double cheapestDiscounted = 0.0;
        for (Product p : products) {
            if (p.getCode().equals("A")) {
                cheapestDiscounted = p.getDiscountPrice();
            }
        }
        assertThat(cheapestDiscounted).isEqualTo(0.0);
        assertThat(cart.calculateTotal()).isEqualTo(50.0); // 0 + 20 + 30
    }

    @Test
    void buyTwoGetOneFree_shouldNotApplyWithFewerThanThreeProducts() {
        Cart cart = new Cart();
        cart.addProduct(new Product("A", "Mleko", 10.0));
        cart.addProduct(new Product("B", "Chleb", 20.0));

        boolean applied = new BuyTwoGetOneFreePromotion().apply(cart);

        assertThat(applied).isFalse();
        assertThat(cart.calculateTotal()).isEqualTo(30.0);
    }

    @Test
    void buyTwoGetOneFree_shouldNotApplyOnEmptyCart() {
        Cart cart = new Cart();
        boolean applied = new BuyTwoGetOneFreePromotion().apply(cart);
        assertThat(applied).isFalse();
    }

    // ── CouponPromotion ──────────────────────────────────────────────────────

    @Test
    void coupon_shouldApply30PercentDiscountToTargetProduct() {
        Cart cart = new Cart();
        cart.addProduct(new Product("A", "Laptop", 1000.0));
        cart.addProduct(new Product("B", "Mysz", 100.0));

        new CouponPromotion("A", 0.30).apply(cart);

        List<Product> products = cart.getProducts();
        double laptopDiscount = 0.0;
        for (Product p : products) {
            if (p.getCode().equals("A")) {
                laptopDiscount = p.getDiscountPrice();
            }
        }
        assertThat(laptopDiscount).isEqualTo(700.0);
        assertThat(cart.calculateTotal()).isEqualTo(800.0); // 700 + 100
    }

    @Test
    void coupon_shouldReturnFalseWhenProductNotInCart() {
        Cart cart = new Cart();
        cart.addProduct(new Product("A", "Laptop", 1000.0));

        boolean applied = new CouponPromotion("X", 0.30).apply(cart);

        assertThat(applied).isFalse();
        assertThat(cart.calculateTotal()).isEqualTo(1000.0);
    }

    // ── PercentageDiscountPromotion ──────────────────────────────────────────

    @Test
    void percentageDiscount_shouldApplyToAllProducts() {
        Cart cart = new Cart();
        cart.addProduct(new Product("A", "A", 100.0));
        cart.addProduct(new Product("B", "B", 200.0));

        new PercentageDiscountPromotion(0.10).apply(cart);

        assertThat(cart.calculateTotal()).isEqualTo(270.0); // 90 + 180
    }

    @Test
    void percentageDiscount_shouldReturnFalseOnEmptyCart() {
        Cart cart = new Cart();
        boolean applied = new PercentageDiscountPromotion(0.05).apply(cart);
        assertThat(applied).isFalse();
    }

    // ── ValueBasedDiscountPromotion ──────────────────────────────────────────

    @Test
    void valueBasedDiscount_shouldApplyWhenTotalExceedsThreshold() {
        Cart cart = new Cart();
        cart.addProduct(new Product("A", "A", 200.0));
        cart.addProduct(new Product("B", "B", 150.0)); // total = 350 > 300

        new ValueBasedDiscountPromotion(300.0, 0.05).apply(cart);

        // 5% off: 200*0.95 + 150*0.95 = 190 + 142.5 = 332.5
        assertThat(cart.calculateTotal()).isEqualTo(332.5);
    }

    @Test
    void valueBasedDiscount_shouldNotApplyWhenTotalBelowThreshold() {
        Cart cart = new Cart();
        cart.addProduct(new Product("A", "A", 100.0));
        cart.addProduct(new Product("B", "B", 100.0)); // total = 200, threshold = 300

        boolean applied = new ValueBasedDiscountPromotion(300.0, 0.05).apply(cart);

        assertThat(applied).isFalse();
        assertThat(cart.calculateTotal()).isEqualTo(200.0);
    }

    // ── applyAllPromotions / multiple promotions ─────────────────────────────

    @Test
    void applyAllPromotions_shouldApplyRegisteredPromotionsInOrder() {
        Cart cart = new Cart();
        cart.addProduct(new Product("A", "Cheap", 10.0));
        cart.addProduct(new Product("B", "Mid", 20.0));
        cart.addProduct(new Product("C", "Exp", 30.0));

        // 2+1: cheapest (10) becomes free → total = 50
        // then 10% off all current discountPrices: 0*0.9 + 20*0.9 + 30*0.9 = 0 + 18 + 27 = 45
        cart.addPromotion(new BuyTwoGetOneFreePromotion());
        cart.addPromotion(new PercentageDiscountPromotion(0.10));
        cart.applyAllPromotions();

        assertThat(cart.calculateTotal()).isEqualTo(45.0);
    }

    @Test
    void removePromotion_shouldPreventItFromBeingApplied() {
        Cart cart = new Cart();
        cart.addProduct(new Product("A", "A", 100.0));
        cart.addProduct(new Product("B", "B", 100.0));
        cart.addProduct(new Product("C", "C", 100.0));

        Promotion buyTwoGetOne = new BuyTwoGetOneFreePromotion();
        cart.addPromotion(buyTwoGetOne);
        cart.removePromotion(buyTwoGetOne);
        cart.applyAllPromotions();

        assertThat(cart.calculateTotal()).isEqualTo(300.0);
    }
}
