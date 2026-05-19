import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Cart {
    private final List<Product> products = new ArrayList<>();
    private final List<Promotion> promotions = new ArrayList<>();
    private Comparator<Product> currentComparator = Product.byPrice().reversed().thenComparing(Product.byName());

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public int getSize() {
        return products.size();
    }

    public void addProduct(Product product) {
        if (product == null)
            return;
        products.add(product);
        products.sort(currentComparator);
    }

    public void replaceProduct(int index, Product product) {
        if (index >= 0 && index < products.size() && product != null) {
            products.set(index, product);
        }
    }

    // promotions

    public void addPromotion(Promotion promotion) {
        if (promotion != null)
            promotions.add(promotion);
    }

    public void removePromotion(Promotion promotion) {
        promotions.remove(promotion);
    }

    public void applyAllPromotions() {
        for (Promotion promotion : promotions) {
            promotion.apply(this);
        }
    }

    // Sum of current discount prices — the amount the customer pays
    public double calculateTotal() {
        double total = 0.0;
        for (Product p : products) {
            total += p.getDiscountPrice();
        }
        return total;
    }

    // Sum of original prices — used by threshold-based promotions
    public double calculateOriginalTotal() {
        double total = 0.0;
        for (Product p : products) {
            total += p.getPrice();
        }
        return total;
    }

    // searching logic

    public Product findCheapest() {
        List<Product> result = findNCheapest(1);
        return result.isEmpty() ? null : result.get(0);
    }

    public Product findMostExpensive() {
        List<Product> result = findNMostExpensive(1);
        return result.isEmpty() ? null : result.get(0);
    }

    public List<Product> findNCheapest(int n) {
        return findNProductsWithComparator(n, Product.byPrice());
    }

    public List<Product> findNMostExpensive(int n) {
        return findNProductsWithComparator(n, Product.byPrice().reversed());
    }

    public List<Product> findNProductsWithComparator(int n, Comparator<Product> comparator) {
        if (products.isEmpty() || n <= 0)
            return new ArrayList<>();
        List<Product> sortedCopy = new ArrayList<>(products);
        sortedCopy.sort(comparator);
        return new ArrayList<>(sortedCopy.subList(0, Math.min(n, products.size())));
    }

    // sorting logic

    public void sort(Comparator<Product> comparator) {
        if (comparator == null)
            return;
        currentComparator = comparator;
        products.sort(currentComparator);
    }
}
