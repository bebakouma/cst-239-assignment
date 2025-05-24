package edu.gcu.storefront;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Facade for handling store sessions, including shopping cart operations,
 * inventory reservations, cancellations, and checkout.
 *
 * <p>Each shopper session is uniquely tracked using a UUID. The
 * {@link InventoryManager} is used to manage product availability and stock control,
 * while {@link ShoppingCart} handles item tracking per session.</p>
 *
 * <p>This class encapsulates the business logic of the storefront.</p>
 *
 * @author Batossa
 * @version 1.0
 */
public class StoreFront {

    private final InventoryManager inventory;
    /** sessionId → cart */
    private final Map<UUID, ShoppingCart> carts = new ConcurrentHashMap<>();

    /**
     * Constructs the StoreFront with a provided InventoryManager instance.
     *
     * @param inventory the InventoryManager containing products and stock
     */
    public StoreFront(InventoryManager inventory) {
        this.inventory = inventory;
    }

    /**
     * Creates a new shopper session and returns a unique session ID.
     *
     * @return a newly generated UUID representing the shopper session
     */
    public UUID startSession() {
        UUID id = UUID.randomUUID();
        carts.put(id, new ShoppingCart());
        return id;
    }

    /**
     * Adds an item to the cart and reserves inventory atomically.
     *
     * @param sessionId the UUID of the active shopping session
     * @param productId the ID of the product to add
     * @param qty the quantity to add and reserve
     * @return {@code true} if the item was successfully added; {@code false} if not enough stock
     * @throws IllegalArgumentException if the session ID is invalid
     */
    public boolean addToCart(UUID sessionId, String productId, int qty) {
        ShoppingCart cart = carts.get(sessionId);
        if (cart == null) throw new IllegalArgumentException("Unknown session");
        if (inventory.reserve(productId, qty)) {
            cart.add(productId, qty);
            return true;
        }
        return false;
    }

    /**
     * Cancels an active session, releasing any reserved inventory and removing the cart.
     *
     * @param sessionId the UUID of the session to cancel
     */
    public void cancelSession(UUID sessionId) {
        ShoppingCart cart = carts.remove(sessionId);
        if (cart == null) return;
        cart.view().forEach((pid, qty) -> inventory.release(pid, qty));
    }

    /**
     * Finalizes the purchase, deducts stock permanently, removes the cart,
     * and returns the total cost of the transaction.
     *
     * @param sessionId the UUID of the completed session
     * @return the total amount due as a {@link BigDecimal}
     * @throws IllegalArgumentException if the session ID is invalid
     */
    public BigDecimal checkout(UUID sessionId) {
        ShoppingCart cart = carts.remove(sessionId);
        if (cart == null) throw new IllegalArgumentException("Unknown session");
        return cart.total(inventory.viewCatalog());
    }
}
