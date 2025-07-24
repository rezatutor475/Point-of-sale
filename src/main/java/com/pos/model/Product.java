package com.pos.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Product
 *
 * Represents a product in the POS system with essential details
 * such as identifier, name, description, price, and stock information.
 */
public class Product {

    private String productId;
    private String name;
    private String description;
    private double price;
    private int stockQuantity;
    private String category;
    private boolean active;
    private String supplier;
    private String barcode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private double discountRate; // percentage e.g., 0.15 for 15%

    public Product(String productId, String name, String description, double price, int stockQuantity) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        setPrice(price);
        setStockQuantity(stockQuantity);
        this.active = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.discountRate = 0.0;
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
        updateTimestamp();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        updateTimestamp();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        updateTimestamp();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
        updateTimestamp();
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        this.stockQuantity = stockQuantity;
        updateTimestamp();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
        updateTimestamp();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        updateTimestamp();
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
        updateTimestamp();
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
        updateTimestamp();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        if (discountRate < 0 || discountRate > 1) {
            throw new IllegalArgumentException("Discount rate must be between 0 and 1");
        }
        this.discountRate = discountRate;
        updateTimestamp();
    }

    // Utility methods
    public boolean isInStock() {
        return stockQuantity > 0;
    }

    public void reduceStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (quantity > stockQuantity) {
            throw new IllegalArgumentException("Insufficient stock to reduce");
        }
        this.stockQuantity -= quantity;
        updateTimestamp();
    }

    public void restock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Restock quantity must be positive");
        }
        this.stockQuantity += quantity;
        updateTimestamp();
    }

    public boolean isValidPrice() {
        return price >= 0 && price <= 1_000_000;
    }

    public boolean isSameProduct(Product other) {
        return other != null && Objects.equals(this.productId, other.productId);
    }

    public boolean hasValidCategory() {
        return category != null && !category.trim().isEmpty();
    }

    public boolean isValidBarcode() {
        return barcode != null && barcode.matches("\\d{8,13}");
    }

    public boolean isAvailableForSale() {
        return isInStock() && isActive();
    }

    public double getDiscountedPrice() {
        return price * (1 - discountRate);
    }

    private void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productId, product.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    @Override
    public String toString() {
        return String.format(
            "Product{id='%s', name='%s', description='%s', price=%.2f, discountedPrice=%.2f, stockQuantity=%d, category='%s', active=%b, supplier='%s', barcode='%s', createdAt='%s', updatedAt='%s'}",
            productId, name, description, price, getDiscountedPrice(), stockQuantity, category, active, supplier, barcode, createdAt, updatedAt);
    }
}
