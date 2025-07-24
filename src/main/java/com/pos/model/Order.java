package com.pos.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a customer's purchase order, including items, timestamps,
 * status, payment, and delivery information.
 */
public class Order {

    private final String orderId;
    private String customerId;
    private final LocalDateTime orderDate;
    private final List<OrderItem> items;
    private double totalAmount;
    private boolean paid;
    private String paymentMethod;
    private String notes;
    private String status;
    private boolean deliveryRequired;
    private String deliveryAddress;
    private LocalDateTime deliveryScheduled;

    public Order() {
        this.orderId = UUID.randomUUID().toString();
        this.orderDate = LocalDateTime.now();
        this.items = new ArrayList<>();
        this.status = "PENDING";
        this.paid = false;
        this.deliveryRequired = false;
    }

    public Order(String customerId) {
        this();
        this.customerId = customerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public void addItem(OrderItem item) {
        if (item != null) {
            items.add(item);
            recalculateTotal();
        }
    }

    public void removeItem(OrderItem item) {
        if (items.remove(item)) {
            recalculateTotal();
        }
    }

    public void clearItems() {
        items.clear();
        recalculateTotal();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    private void recalculateTotal() {
        this.totalAmount = items.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
    }

    public boolean isPaid() {
        return paid;
    }

    public void markAsPaid(String paymentMethod) {
        this.paid = true;
        this.paymentMethod = paymentMethod;
        this.status = "COMPLETED";
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status != null && !status.trim().isEmpty()) {
            this.status = status.toUpperCase();
        }
    }

    public boolean isCancellable() {
        return !"COMPLETED".equalsIgnoreCase(status) && !"CANCELED".equalsIgnoreCase(status);
    }

    public void cancelOrder() {
        if (isCancellable()) {
            this.status = "CANCELED";
        }
    }

    public boolean isDelivered() {
        return "DELIVERED".equalsIgnoreCase(status);
    }

    public boolean isDeliveryRequired() {
        return deliveryRequired;
    }

    public void setDeliveryRequired(boolean deliveryRequired) {
        this.deliveryRequired = deliveryRequired;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public LocalDateTime getDeliveryScheduled() {
        return deliveryScheduled;
    }

    public void scheduleDelivery(LocalDateTime deliveryDateTime) {
        if (deliveryRequired && deliveryDateTime != null) {
            this.deliveryScheduled = deliveryDateTime;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(orderId, order.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", paid=" + paid +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", status='" + status + '\'' +
                ", deliveryRequired=" + deliveryRequired +
                ", deliveryScheduled=" + deliveryScheduled +
                ", itemsCount=" + items.size() +
                '}';
    }
}
