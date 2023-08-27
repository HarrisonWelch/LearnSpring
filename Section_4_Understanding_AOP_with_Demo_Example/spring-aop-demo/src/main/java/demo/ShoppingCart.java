package demo;

import org.springframework.stereotype.Component;

@Component
public class ShoppingCart {
    public void checkout(String status) {
        // Logging
        // Authentication & Authorization
        // Sanitize the Data
        System.out.println("Checkout Method from Shopping Cart Called");
        // This is a join-point
    }

    public int quantity() {
        return 2;
    }
}
