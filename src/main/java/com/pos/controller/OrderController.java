package com.pos.controller;

import com.pos.model.Order;
import com.pos.model.OrderItem;
import com.pos.service.OrderService;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OrderController
 *
 * Handles operations related to placing, updating,
 * retrieving, listing, and analyzing customer orders.
 */
public class OrderController {

    private static final Logger LOGGER = Logger.getLogger(OrderController.class.getName());
    private final OrderService orderService;

    /**
     * Constructs the OrderController with a valid OrderService.
     *
     * @param orderService Service to manage order logic.
     */
    public OrderController(OrderService orderService) {
        this.orderService = Objects.requireNonNull(orderService, "OrderService must not be null");
    }

    public Order placeOrder(Order order) {
        validateOrder(order);
        logInfo("Placing order for customer ID: %s", order.getCustomerId());
        return execute(() -> orderService.placeOrder(order), "place order");
    }

    public Order updateOrder(Order order) {
        validateOrder(order);
        logInfo("Updating order ID: %s", order.getId());
        return execute(() -> orderService.updateOrder(order), "update order");
    }

    public Order getOrderById(String orderId) {
        validateId(orderId, "Order ID");
        logInfo("Retrieving order ID: %s", orderId);
        return execute(() -> orderService.getOrderById(orderId), "retrieve order");
    }

    public List<Order> listAllOrders() {
        logInfo("Listing all orders");
        return execute(orderService::getAllOrders, "list orders");
    }

    public List<Order> listOrdersByCustomer(String customerId) {
        validateId(customerId, "Customer ID");
        logInfo("Listing orders for customer ID: %s", customerId);
        return execute(() -> orderService.getOrdersByCustomer(customerId), "list customer orders");
    }

    public List<Order> getOrdersByDate(LocalDate date) {
        Objects.requireNonNull(date, "Date must not be null");
        logInfo("Fetching orders on date: %s", date);
        return execute(() -> orderService.getOrdersByDate(date), "fetch orders by date");
    }

    public boolean cancelOrder(String orderId) {
        validateId(orderId, "Order ID");
        logInfo("Cancelling order ID: %s", orderId);
        return execute(() -> orderService.cancelOrder(orderId), "cancel order");
    }

    public double calculateCustomerSpending(String customerId) {
        validateId(customerId, "Customer ID");
        logInfo("Calculating total spending for customer ID: %s", customerId);
        return execute(() -> orderService.calculateTotalSpentByCustomer(customerId), "calculate customer spending");
    }

    // ================= VALIDATION =================

    private void validateOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (isBlank(order.getCustomerId())) {
            throw new IllegalArgumentException("Customer ID must not be empty");
        }
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
        for (OrderItem item : order.getItems()) {
            if (isBlank(item.getProductId())) {
                throw new IllegalArgumentException("Order item product ID must not be empty");
            }
            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Order item quantity must be positive");
            }
        }
    }

    private void validateId(String id, String fieldName) {
        if (isBlank(id)) {
            throw new IllegalArgumentException(fieldName + " must not be null or empty");
        }
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    private void logInfo(String message, Object... args) {
        LOGGER.info(String.format(message, args));
    }

    private <T> T execute(SupplierWithException<T> action, String operation) {
        try {
            return action.get();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to " + operation, e);
            throw new IllegalStateException("Operation failed: " + operation, e);
        }
    }

    @FunctionalInterface
    private interface SupplierWithException<T> {
        T get() throws Exception;
    }
}
