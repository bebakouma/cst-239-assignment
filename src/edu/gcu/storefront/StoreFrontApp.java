package edu.gcu.storefront;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

/**
 * Command-line application that demonstrates usage of the store front system.
 *
 * <p>This demo initializes sample products, allows the user to interact with a store via
 * the console, and supports adding products to the cart, canceling a purchase,
 * or completing a checkout.</p>
 *
 * <p>Build & Run:</p>
 * <pre>{@code
 * javac -d out $(find src -name "*.java")
 * java  -cp out edu.gcu.storefront.StoreFrontApp
 * }</pre>
 *
 * @author Batossa
 * @version 1.0
 */
public final class StoreFrontApp {

    private StoreFrontApp() {}

    public static void main(String[] args) {

        // 1) Seed products
        Map<String, SalableProduct> seed = new HashMap<>();
        seed.put("A100", new SalableProduct("A100", "Notebook", "100-page spiraled notebook",
                new BigDecimal("2.75"), 10));
        seed.put("B200", new SalableProduct("B200", "Gel Pen", "Blue gel ink pen",
                new BigDecimal("1.25"), 5));
        seed.put("C300", new SalableProduct("C300", "Stapler", "Mini stapler",
                new BigDecimal("4.95"), 3));

        InventoryManager inv = new InventoryManager();
        inv.initStore(seed);
        StoreFront store = new StoreFront(inv);

        // 2) Simple CLI loop
        UUID session = store.startSession();
        System.out.println("=== Welcome to the Store Front ===");
        try (Scanner sc = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.println("\nCatalog:");
                for (SalableProduct p : inv.viewCatalog().values()) {
                    System.out.println(p);
                }

                System.out.print(
                    "\nOptions:\n" +
                    "1) Add to cart\n" +
                    "2) Cancel purchase\n" +
                    "3) Checkout\n" +
                    "0) Exit\n" +
                    "> "
                );

                String input = sc.nextLine().trim();
                switch (input) {
                    case "1":
                        System.out.print("Enter product ID: ");
                        String id = sc.nextLine().trim();
                        System.out.print("Qty: ");
                        int qty = Integer.parseInt(sc.nextLine().trim());
                        boolean ok = store.addToCart(session, id, qty);
                        System.out.println(ok ? "[OK] Added!" : "[X] Not enough stock.");
                        break;

                    case "2":
                        store.cancelSession(session);
                        session = store.startSession(); // fresh cart
                        System.out.println("Cart cleared, reservations released.");
                        break;

                    case "3":
                        BigDecimal total = store.checkout(session);
                        System.out.println("Thank you! Total due = $" + total);
                        running = false;
                        break;

                    case "0":
                        store.cancelSession(session);
                        running = false;
                        break;

                    default:
                        System.out.println("Unknown choice.");
                        break;
                }
            }
        }

        System.out.println("\nGoodbye!");
    }
}
