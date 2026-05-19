import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

public class CartTest {

    @Test
    void shouldAcceptMoreProductsThanInitialCapacity() {
        // Given
        Cart cart = new Cart();
        Product p1 = new Product("A", "Prod1", 100);
        Product p2 = new Product("B", "Prod2", 100);
        Product p3 = new Product("C", "Prod3", 100);

        // When
        cart.addProduct(p1);
        cart.addProduct(p2);
        cart.addProduct(p3);

        // Then
        assertEquals(3, cart.getProducts().size());
        assertEquals(300.0, cart.calculateTotal());
    }

    @Test
    void shouldNotAddNullProductToCart() {
        // Given
        Cart cart = new Cart();

        // When
        cart.addProduct(null);

        // Then
        assertThat(cart.getSize()).isEqualTo(0);
    }

    @Test
    void shouldReturnZeroTotalForEmptyCart() {
        // Given
        Cart cart = new Cart();

        // When
        double total = cart.calculateTotal();

        // Then
        assertThat(total).isEqualTo(0.0);
    }

    @Test
    void shouldCalculateTotalCorrectlyForMultipleProducts() {
        // Given
        Cart cart = new Cart();
        cart.addProduct(new Product("A", "Mleko", 3.50));
        cart.addProduct(new Product("B", "Chleb", 4.20));
        cart.addProduct(new Product("C", "Masło", 7.30));

        // When
        double total = cart.calculateTotal();

        // Then
        assertThat(total).isEqualTo(15.00);
    }

    @Test
    void shouldGetCorrectlyTheNAmountOfCheapestProducts() {
        // Given
        Cart cart = new Cart();
        Product p1 = new Product("A", "Mleko", 3.50);
        Product p2 = new Product("B", "Chleb", 4.20);
        Product p3 = new Product("C", "Masło", 7.30);

        cart.addProduct(p1);
        cart.addProduct(p2);
        cart.addProduct(p3);

        // When
        int n = 2;
        List<Product> cheapestProducts = cart.findNCheapest(n);

        // Then
        assertThat(cheapestProducts).hasSize(n);
        assertThat(cheapestProducts).containsExactly(p1, p2);
    }

    @Test
    void shouldFindCheapestAndMostExpensiveProducts() {
        // Given
        Cart cart = new Cart();
        Product p1 = new Product("1", "Cheap", 10.0);
        Product p2 = new Product("2", "Medium", 50.0);
        Product p3 = new Product("3", "Expensive", 100.0);
        cart.addProduct(p1);
        cart.addProduct(p2);
        cart.addProduct(p3);

        // When & Then
        assertThat(cart.findCheapest()).isEqualTo(p1);
        assertThat(cart.findMostExpensive()).isEqualTo(p3);
    }

    @Test
    void shouldHandleEmptyCartGracefully() {
        Cart cart = new Cart();
        assertThat(cart.findCheapest()).isNull();
        assertThat(cart.findMostExpensive()).isNull();
        assertThat(cart.findNCheapest(5)).isEmpty();
    }

    @Test
    void shouldHandleInvalidNValues() {
        // Given
        Cart cart = new Cart();
        cart.addProduct(new Product("1", "A", 100.0));

        // When & Then
        // Case A: n greatest then cart
        assertThat(cart.findNCheapest(10)).hasSize(1);

        // Case B:  N equals 0 or negative
        assertThat(cart.findNCheapest(0)).isEmpty();
        assertThat(cart.findNCheapest(-5)).isEmpty();
    }

    @Test
    void shouldSortProductsByPriceAndThenByName() {
        // Given
        Cart cart = new Cart();
        Product p1 = new Product("1", "Carpet", 100.0);
        Product p2 = new Product("2", "Computer", 100.0);
        Product p3 = new Product("3", "Lamp", 99.0);
        cart.addProduct(p1);
        cart.addProduct(p2);
        cart.addProduct(p3);

        // When
        cart.sort(Product.byPrice().thenComparing(Product.byName()));
        // Then
        assertThat(cart.getProducts()).containsExactly(p3, p1, p2);
    }

    @Test
    void shouldApplyDefaultSortOrderDescendingPriceThenAlphabetical() {
        // Given
        Cart cart = new Cart();
        Product banana = new Product("B", "Banana", 5.0);
        Product apple  = new Product("A", "Apple",  5.0);
        Product cherry = new Product("C", "Cherry", 3.0);

        // When
        cart.addProduct(banana);
        cart.addProduct(apple);
        cart.addProduct(cherry);

        // Then - Apple and Banana both 5.0 → alphabetical → Apple first; Cherry last
        assertThat(cart.getProducts()).containsExactly(apple, banana, cherry);
    }
}
