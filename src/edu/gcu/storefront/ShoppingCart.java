package edu.gcu.storefront;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a user's temporary shopping cart for a single session.
 *
 * <p>Holds item quantities by product ID. Does not clone product objects—
 * lookup is done using the product catalog during checkout.</p>
 *
 * @author Batossa
 * @version 1.0
 */
public class ShoppingCart {

    /** productId → qty */
    private final Map<String, Integer> items = new LinkedHashMap<>();

    /**
     * Adds the specified quantity of a product to the cart.
     *
     * @param productId the ID of the product to add
     * @param qty the quantity to add
     */
    public void add(String productId, int qty) {
        items.merge(productId, qty, Integer::sum);
    }

    /**
     * Removes a product from the cart by its ID.
     *
     * @param productId the ID of the product to remove
     */
    public void remove(String productId) {
        items.remove(productId);
    }

    /**
     * Empties all items from the cart.
     */
    public void empty() {
        items.clear();
    }

    /**
     * Returns an unmodifiable view of items in the cart.
     *
     * @return a read-only map of product IDs to quantities
     */
    public Map<String, Integer> view() {
        return Collections.unmodifiableMap(items);
    }

    /**
     * Calculates the total price using the provided product catalog.
     *
     * @param catalog the catalog of available products
     * @return the total cost as {@link BigDecimal}
     */
    public BigDecimal total(Map<String, SalableProduct> catalog) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Map.Entry<String, Integer> e : items.entrySet()) {
            SalableProduct p = catalog.get(e.getKey());
            if (p != null) {
                sum = sum.add(p.getPrice().multiply(BigDecimal.valueOf(e.getValue())));
            }
        }
        return sum;
    }
}
