package edu.gcu.storefront;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Simple immutable-ID implementation of {@link Salable}.
 */
public class SalableProduct implements Salable {

    private final String id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private int quantity;

    public SalableProduct(String id,
                          String name,
                          String description,
                          BigDecimal price,
                          int quantity) {

        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
        this.price = Objects.requireNonNull(price);
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        this.quantity = quantity;
    }

    // ────────────────── Salable ──────────────────
    @Override public String getId()          { return id; }
    @Override public String getName()        { return name; }
    @Override public String getDescription() { return description; }
    @Override public BigDecimal getPrice()   { return price; }

    @Override
    public synchronized int getQuantity() {
        return quantity;
    }

    @Override
    public synchronized void adjustQuantity(int delta) {
        int newQty = quantity + delta;
        if (newQty < 0) {
            throw new IllegalArgumentException("Insufficient stock for " + name);
        }
        quantity = newQty;
    }

    // ────────────────── helpers ──────────────────
    @Override
    public String toString() {
        return String.format("%s (%s) - $%s (qty %d)", name, id, price, quantity);
    }
}
