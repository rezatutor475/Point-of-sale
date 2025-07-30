package com.example.posqr.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    private String paymentMethod;

    private String customerName;

    private String receiptNumber;

    private boolean refunded = false;

    private String notes;

    private LocalDateTime saleDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        if (saleDate == null) {
            saleDate = createdAt;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Business Logic Methods

    public boolean isRefunded() {
        return refunded;
    }

    public void markAsRefunded() {
        this.refunded = true;
    }

    public boolean isHighValueSale(BigDecimal threshold) {
        return totalAmount != null && totalAmount.compareTo(threshold) > 0;
    }

    public String generateDisplayLabel() {
        return String.format("Sale #%s - %s (%s)",
                receiptNumber != null ? receiptNumber : id,
                customerName != null ? customerName : "Unknown Customer",
                saleDate != null ? saleDate.toLocalDate().toString() : "Unknown Date");
    }

    public BigDecimal calculateUnitPrice() {
        if (quantity != null && quantity > 0 && totalAmount != null) {
            return totalAmount.divide(BigDecimal.valueOf(quantity), 2, BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    public boolean isPaidWithCash() {
        return paymentMethod != null && paymentMethod.equalsIgnoreCase("cash");
    }

    public boolean isSaleToday() {
        return saleDate != null && saleDate.toLocalDate().equals(LocalDateTime.now().toLocalDate());
    }

    public String getCustomerOrAnonymous() {
        return (customerName != null && !customerName.isBlank()) ? customerName : "Anonymous";
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Equals and HashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return Objects.equals(id, sale.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
} 
