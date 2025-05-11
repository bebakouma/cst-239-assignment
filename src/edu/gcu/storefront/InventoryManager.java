package edu.gcu.storefront;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Central place for stock control.  Thread-safe via ConcurrentHashMap and intrinsic
 * locking on {@link SalableProduct#adjustQuantity(int)}.
 */
public class InventoryManager {

    /** Keyed by product ID (SKU). */
    private final Map<String, SalableProduct> catalog = new ConcurrentHashMap<>();

    /** One-shot bootstrap of initial products.  */
    public void initStore(Map<String, SalableProduct> initialStock) {
        catalog.clear();
        catalog.putAll(initialStock);
    }

    public Map<String, SalableProduct> viewCatalog() {
        return Collections.unmodifiableMap(catalog);
    }

    /** Attempt to reserve stock; returns {@code true} if successful. */
    public boolean reserve(String productId, int qty) {
        SalableProduct p = catalog.get(productId);
        if (p == null) return false;
        synchronized (p) {
            if (p.getQuantity() < qty) return false;
            p.adjustQuantity(-qty);
            return true;
        }
    }

    /** Release a previous reservation (e.g., on cart cancel). */
    public void release(String productId, int qty) {
        SalableProduct p = catalog.get(productId);
        if (p != null) {
            p.adjustQuantity(qty);
        }
    }
}
