package com.pos.controller;

import com.pos.model.Order;
import com.pos.model.PaymentResult;
import com.pos.service.OrderService;
import com.pos.service.payment.PaymentService;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * POSController
 *
 * Central controller for Point of Sale transactions.
 * Handles order placement, payment processing, refunds, cancellations, and receipt printing.
 */
public class POSController {

    private static final Logger logger = Logger.getLogger(POSController.class.getName());

    private final OrderService orderService;
    private final PaymentService paymentService;

    /**
     * Constructs a POSController with required services.
     *
     * @param orderService   Service to handle order operations.
     * @param paymentService Service to handle payment operations.
     */
    public POSController(OrderService orderService, PaymentService paymentService) {
        this.orderService = Objects.requireNonNull(orderService, "OrderService must not be null");
        this.paymentService = Objects.requireNonNull(paymentService, "PaymentService must not be null");
    }

    /**
     * Processes an order including validation, total calculation, persistence, and payment.
     *
     * @param order the order to process
     * @return PaymentResult containing the outcome of the payment
     */
    public PaymentResult processOrder(Order order) {
        validateOrder(order);
        logger.info("Starting order processing");

        try {
            orderService.calculateTotals(order);
            orderService.saveOrder(order);

            PaymentResult result = paymentService.processPayment(order);
            order.setPaymentStatus(result.getStatus());

            logger.info("Order processed successfully with payment status: " + result.getStatus());
            return result;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during order processing", e);
            throw new IllegalStateException("Order processing failed", e);
        }
    }

    /**
     * Cancels an existing order.
     *
     * @param orderId ID of the order
     * @return true if successfully cancelled
     */
    public boolean cancelOrder(String orderId) {
        validateOrderId(orderId);
        logger.info("Cancelling order with ID: " + orderId);
        return orderService.cancelOrder(orderId);
    }

    /**
     * Refunds a previously completed order.
     *
     * @param orderId ID of the order
     * @return true if refund is successful
     */
    public boolean refundOrder(String orderId) {
        validateOrderId(orderId);
        logger.info("Refunding order with ID: " + orderId);
        return paymentService.refundPayment(orderId);
    }

    /**
     * Prints a receipt for a given order.
     *
     * @param orderId the ID of the order
     * @return true if receipt printed successfully
     */
    public boolean printReceipt(String orderId) {
        validateOrderId(orderId);
        logger.info("Printing receipt for order ID: " + orderId);
        return orderService.printReceipt(orderId);
    }

    /**
     * Attempts to reprocess payment for a given order ID.
     *
     * @param orderId the ID of the order
     * @return PaymentResult of the retry
     */
    public PaymentResult retryPayment(String orderId) {
        validateOrderId(orderId);
        logger.info("Retrying payment for order ID: " + orderId);

        try {
            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                throw new IllegalArgumentException("Order not found for ID: " + orderId);
            }
            return processOrder(order);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Payment retry failed", e);
            throw new IllegalStateException("Retrying payment failed", e);
        }
    }

    /**
     * Validates the order.
     *
     * @param order the order to validate
     */
    private void validateOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order must not be null");
        }
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
    }

    /**
     * Validates the order ID.
     *
     * @param orderId the ID to validate
     */
    private void validateOrderId(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID must not be null or empty");
        }
    }
}
