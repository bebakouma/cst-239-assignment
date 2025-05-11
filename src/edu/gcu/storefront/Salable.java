package edu.gcu.storefront;

import java.math.BigDecimal;

/**
 * Common behaviour for anything that can be sold in the store.
 */
public interface Salable {

    /** Unique identifier (SKU, UUID, etc.). */
    String getId();

    /** Human-readable name. */
    String getName();

    /** Longer description shown to customers. */
    String getDescription();

    /** Unit price expressed as {@link BigDecimal} to avoid floating-point errors. */
    BigDecimal getPrice();

    /** Current on-hand quantity. */
    int getQuantity();

    /**
     * Adjust inventory.
     *
     * @param delta positive to add stock; negative to deduct.
     * @throws IllegalArgumentException if the adjustment would take inventory below zero.
     */
    void adjustQuantity(int delta);
}
