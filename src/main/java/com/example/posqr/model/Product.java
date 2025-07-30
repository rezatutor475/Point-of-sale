package com.example.posqr.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 1024)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantityInStock;

    private String category;

    private String barcode;

    private String qrCodePath;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean discontinued;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Custom Utility Methods

    public boolean isInStock() {
        return quantityInStock != null && quantityInStock > 0 && !discontinued;
    }

    public void reduceStock(int amount) {
        if (quantityInStock == null || quantityInStock < amount) {
            throw new IllegalArgumentException("Insufficient stock for product: " + name);
        }
        this.quantityInStock -= amount;
    }

    public void increaseStock(int amount) {
        if (quantityInStock == null) {
            quantityInStock = 0;
        }
        this.quantityInStock += amount;
    }

    public boolean isLowStock() {
        return quantityInStock != null && quantityInStock <= 5;
    }

    public boolean isExpired() {
        // Placeholder logic for products with expiration dates (future enhancement)
        return false;
    }

    public void markDiscontinued() {
        this.discontinued = true;
    }

    public void restoreProduct() {
        this.discontinued = false;
    }

    public boolean isDiscontinued() {
        return discontinued;
    }

    public String getDisplayName() {
        return String.format("%s (%s)", name, category != null ? category : "Uncategorized");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getQrCodePath() {
        return qrCodePath;
    }

    public void setQrCodePath(String qrCodePath) {
        this.qrCodePath = qrCodePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setDiscontinued(boolean discontinued) {
        this.discontinued = discontinued;
    }
} 
