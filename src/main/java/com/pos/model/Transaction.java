package com.pos.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Transaction
 *
 * Represents a transaction record within the POS system.
 * Includes details such as transaction ID, amount, type, payment method,
 * timestamps, status, and extended gateway-specific fields.
 */
public class Transaction {

    private String transactionId;
    private String orderId;
    private double amount;
    private PaymentMethod paymentMethod;
    private TransactionType transactionType;
    private TransactionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String referenceNumber;
    private String processorMessage;
    private String gatewayTransactionId;
    private String customerId;

    public enum PaymentMethod {
        CASH, CARD, SADAD, SEP, WALLET, CRYPTO
    }

    public enum TransactionType {
        PAYMENT, REFUND, CHARGEBACK, VOID
    }

    public enum TransactionStatus {
        PENDING, SUCCESS, FAILED, CANCELLED, TIMEOUT, DECLINED
    }

    public Transaction(String transactionId, String orderId, double amount,
                       PaymentMethod paymentMethod, TransactionType transactionType) {
        this.transactionId = transactionId;
        this.orderId = orderId;
        this.setAmount(amount);
        this.paymentMethod = paymentMethod;
        this.transactionType = transactionType;
        this.status = TransactionStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        updateTimestamp();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
        updateTimestamp();
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if (amount < 0 || amount > 10_000_000) {
            throw new IllegalArgumentException("Amount must be between 0 and 10,000,000");
        }
        this.amount = amount;
        updateTimestamp();
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        updateTimestamp();
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
        updateTimestamp();
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
        updateTimestamp();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
        updateTimestamp();
    }

    public String getProcessorMessage() {
        return processorMessage;
    }

    public void setProcessorMessage(String processorMessage) {
        this.processorMessage = processorMessage;
        updateTimestamp();
    }

    public String getGatewayTransactionId() {
        return gatewayTransactionId;
    }

    public void setGatewayTransactionId(String gatewayTransactionId) {
        this.gatewayTransactionId = gatewayTransactionId;
        updateTimestamp();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
        updateTimestamp();
    }

    // Transaction validation logic
    public boolean isValidTransaction() {
        return transactionId != null && orderId != null && amount > 0;
    }

    // Utility methods
    public boolean isSuccessful() {
        return this.status == TransactionStatus.SUCCESS;
    }

    public boolean isPending() {
        return this.status == TransactionStatus.PENDING;
    }

    public boolean isFailed() {
        return this.status == TransactionStatus.FAILED;
    }

    public boolean isCancelable() {
        return this.status == TransactionStatus.PENDING || this.status == TransactionStatus.TIMEOUT;
    }

    public boolean isRefundable() {
        return this.status == TransactionStatus.SUCCESS && this.transactionType == TransactionType.PAYMENT;
    }

    private void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", amount=" + amount +
                ", paymentMethod=" + paymentMethod +
                ", transactionType=" + transactionType +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", referenceNumber='" + referenceNumber + '\'' +
                ", processorMessage='" + processorMessage + '\'' +
                ", gatewayTransactionId='" + gatewayTransactionId + '\'' +
                ", customerId='" + customerId + '\'' +
                '}';
    }
}
