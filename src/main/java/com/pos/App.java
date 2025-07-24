package com.pos;

import com.pos.controller.InventoryController;
import com.pos.controller.OrderController;
import com.pos.controller.CustomerController;
import com.pos.controller.POSController;
import com.pos.model.Product;
import com.pos.model.Customer;

import java.util.Scanner;

/**
 * Entry point for the POS system.
 * Provides a command-line interface for interacting with inventory, orders, customers, and payments.
 */
public class App {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        InventoryController inventoryController = new InventoryController();
        OrderController orderController = new OrderController();
        CustomerController customerController = new CustomerController();
        POSController posController = new POSController();

        boolean isRunning = true;
        while (isRunning) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> inventoryController.listProducts();
                case "2" -> addProductFlow(inventoryController);
                case "3" -> orderController.createOrder(scanner);
                case "4" -> customerController.listCustomers();
                case "5" -> registerCustomerFlow(customerController);
                case "6" -> posController.processTransaction(scanner);
                case "7" -> posController.printSalesReport();
                case "8" -> searchProductFlow(inventoryController);
                case "9" -> {
                    System.out.println("\nThank you for using the POS System. Goodbye!");
                    isRunning = false;
                }
                default -> System.out.println("\n[!] Invalid option. Please enter a number from 1 to 9.");
            }
        }
    }

    /**
     * Displays the main menu to the user.
     */
    private static void printMenu() {
        System.out.println("\n========= POINT OF SALE MENU =========");
        System.out.println("1. View All Products");
        System.out.println("2. Add New Product");
        System.out.println("3. Create New Order");
        System.out.println("4. View All Customers");
        System.out.println("5. Register New Customer");
        System.out.println("6. Process Payment");
        System.out.println("7. Generate Sales Report");
        System.out.println("8. Search Product by Name");
        System.out.println("9. Exit Program");
        System.out.print("Select an option (1-9): ");
    }

    /**
     * Handles user input for adding a new product.
     *
     * @param controller the InventoryController instance
     */
    private static void addProductFlow(InventoryController controller) {
        try {
            System.out.print("\nEnter product name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter product price: ");
            double price = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter product quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine().trim());

            controller.addProduct(new Product(name, price, quantity));
            System.out.println("[✓] Product added successfully.");

        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid numeric input. Please try again.");
        }
    }

    /**
     * Handles user input for registering a new customer.
     *
     * @param controller the CustomerController instance
     */
    private static void registerCustomerFlow(CustomerController controller) {
        System.out.print("\nEnter customer name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter contact number: ");
        String contact = scanner.nextLine().trim();

        controller.registerCustomer(new Customer(name, contact));
        System.out.println("[✓] Customer registered successfully.");
    }

    /**
     * Handles product search by name.
     *
     * @param controller the InventoryController instance
     */
    private static void searchProductFlow(InventoryController controller) {
        System.out.print("\nEnter product name to search: ");
        String name = scanner.nextLine().trim();

        controller.searchProductByName(name);
    }
}
