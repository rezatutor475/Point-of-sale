package com.pos.model;

import java.util.Objects;

/**
 * OrderItem
 *
 * Represents a product item within an order, including quantity, price, tax, discounts, and additional business logic.
 */
public class OrderItem {

    private String productId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private double discount; // percentage (0-100)
    private double taxRate;  // percentage (0-100)
    private boolean returnable;

    public OrderItem(String productId, String productName, int quantity, double unitPrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = Math.max(quantity, 1);
        this.unitPrice = Math.max(unitPrice, 0);
        this.discount = 0.0;
        this.taxRate = 0.0;
        this.returnable = true;
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity > 0) {
            this.quantity = quantity;
        }
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        if (unitPrice >= 0) {
            this.unitPrice = unitPrice;
        }
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        if (discount >= 0 && discount <= 100) {
            this.discount = discount;
        }
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        if (taxRate >= 0 && taxRate <= 100) {
            this.taxRate = taxRate;
        }
    }

    public boolean isReturnable() {
        return returnable;
    }

    public void setReturnable(boolean returnable) {
        this.returnable = returnable;
    }

    // Business Logic
    public double getSubtotal() {
        return quantity * unitPrice;
    }

    public double getDiscountAmount() {
        return getSubtotal() * (discount / 100);
    }

    public double getTaxAmount() {
        return (getSubtotal() - getDiscountAmount()) * (taxRate / 100);
    }

    public double getTotalPrice() {
        return getSubtotal() - getDiscountAmount() + getTaxAmount();
    }

    public boolean isValid() {
        return productId != null && !productId.trim().isEmpty()
            && productName != null && !productName.trim().isEmpty()
            && quantity > 0 && unitPrice >= 0;
    }

    public boolean isHighValueItem(double threshold) {
        return getTotalPrice() > threshold;
    }

    public OrderItem copy() {
        OrderItem copy = new OrderItem(productId, productName, quantity, unitPrice);
        copy.setDiscount(discount);
        copy.setTaxRate(taxRate);
        copy.setReturnable(returnable);
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem that = (OrderItem) o;
        return Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    @Override
    public String toString() {
        return String.format(
            "OrderItem{productId='%s', productName='%s', quantity=%d, unitPrice=%.2f, discount=%.1f%%, taxRate=%.1f%%, total=%.2f, returnable=%b}",
            productId, productName, quantity, unitPrice, discount, taxRate, getTotalPrice(), returnable
        );
    }
}
