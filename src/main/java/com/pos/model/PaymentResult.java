package com.pos.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * PaymentResult
 *
 * Represents the outcome of a payment transaction, including metadata for auditing, processing, and response handling.
 */
public class PaymentResult {

    private boolean success;
    private String transactionId;
    private String gateway;
    private String message;
    private LocalDateTime timestamp;
    private String orderId;
    private double amount;
    private String currency;
    private String errorCode;
    private String approvalCode;
    private int responseCode;
    private String merchantId;
    private String customerId;

    public PaymentResult(boolean success, String transactionId, String gateway, String message,
                         String orderId, double amount, String currency) {
        this.success = success;
        this.transactionId = transactionId;
        this.gateway = gateway;
        this.message = message;
        this.orderId = orderId;
        setAmount(amount);
        this.currency = currency;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if (amount >= 0) {
            this.amount = amount;
        } else {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    // Utility methods
    public boolean isFailure() {
        return !success;
    }

    public boolean isFromGateway(String gatewayName) {
        return gatewayName != null && gatewayName.equalsIgnoreCase(this.gateway);
    }

    public boolean isSameTransaction(PaymentResult other) {
        return other != null && Objects.equals(this.transactionId, other.transactionId);
    }

    public boolean hasApprovalCode() {
        return approvalCode != null && !approvalCode.isBlank();
    }

    public boolean hasErrorCode() {
        return errorCode != null && !errorCode.isBlank();
    }

    public boolean isValidCurrency(String expectedCurrency) {
        return currency != null && currency.equalsIgnoreCase(expectedCurrency);
    }

    public boolean hasValidResponseCode() {
        return responseCode >= 100 && responseCode <= 999;
    }

    public boolean isApproved() {
        return hasApprovalCode() && success;
    }

    public boolean isRetryableFailure() {
        return !success && (responseCode == 408 || responseCode == 429 || responseCode == 500);
    }

    @Override
    public String toString() {
        return String.format(
                "PaymentResult{success=%s, transactionId='%s', gateway='%s', message='%s', orderId='%s', amount=%.2f, currency='%s', timestamp=%s, errorCode='%s', approvalCode='%s', responseCode=%d, merchantId='%s', customerId='%s'}",
                success, transactionId, gateway, message, orderId, amount, currency, timestamp, errorCode, approvalCode, responseCode, merchantId, customerId
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentResult that = (PaymentResult) o;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }
}
