package com.pos.gateway;

import com.pos.model.Order;
import com.pos.model.Payment;
import com.pos.model.PaymentResult;
import com.pos.repository.OrderRepository;
import com.pos.repository.PaymentRepository;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Enhanced and optimized implementation of SepService for integration with the Sep payment gateway (https://sep.shaparak.ir/).
 */
public class SepService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final SepClient sepClient;

    public SepService(PaymentRepository paymentRepository, OrderRepository orderRepository, SepClient sepClient) {
        this.paymentRepository = Objects.requireNonNull(paymentRepository);
        this.orderRepository = Objects.requireNonNull(orderRepository);
        this.sepClient = Objects.requireNonNull(sepClient);
    }

    public PaymentResult processPayment(String orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) return PaymentResult.failure("Order not found.");

        Order order = orderOpt.get();
        if (order.isPaid()) return PaymentResult.failure("Order already paid.");

        boolean initiated = sepClient.initiatePayment(order.getTotalPrice(), orderId);
        if (!initiated) return PaymentResult.failure("Failed to initiate payment with Sep.");

        Payment payment = new Payment(UUID.randomUUID().toString(), orderId, order.getTotalPrice(), "SEP", new Date(), "SUCCESS");
        order.setPaid(true);
        orderRepository.save(order);
        paymentRepository.save(payment);

        return PaymentResult.success("Payment processed successfully via Sep.");
    }

    public boolean processRefund(String orderId) {
        return paymentRepository.findByOrderId(orderId)
            .filter(payment -> "SEP".equalsIgnoreCase(payment.getMethod()))
            .map(payment -> {
                boolean refunded = sepClient.refund(payment.getAmount(), payment.getOrderId());
                if (refunded) {
                    orderRepository.findById(orderId).ifPresent(order -> {
                        order.setPaid(false);
                        orderRepository.save(order);
                    });
                    paymentRepository.delete(payment.getId());
                }
                return refunded;
            }).orElse(false);
    }

    public PaymentResult verifyPaymentStatus(String orderId) {
        return paymentRepository.findByOrderId(orderId)
            .map(p -> sepClient.checkStatus(orderId)
                ? PaymentResult.success("Payment is confirmed.")
                : PaymentResult.failure("Payment not confirmed."))
            .orElse(PaymentResult.failure("Payment not found."));
    }

    public PaymentResult retryPayment(String orderId) {
        return orderRepository.findById(orderId)
            .map(order -> order.isPaid()
                ? PaymentResult.failure("Order already paid.")
                : processPayment(orderId))
            .orElse(PaymentResult.failure("Order not found."));
    }

    public PaymentResult cancelPayment(String orderId) {
        return paymentRepository.findByOrderId(orderId)
            .map(payment -> {
                if (!"SEP".equalsIgnoreCase(payment.getMethod()))
                    return PaymentResult.failure("Invalid payment method.");

                if (!sepClient.cancel(orderId))
                    return PaymentResult.failure("Sep cancellation failed.");

                paymentRepository.delete(payment.getId());
                return PaymentResult.success("Sep payment cancelled.");
            }).orElse(PaymentResult.failure("Payment not found."));
    }

    public PaymentResult extendAuthorization(String orderId) {
        return paymentRepository.findByOrderId(orderId)
            .map(p -> sepClient.extend(orderId)
                ? PaymentResult.success("Authorization extended.")
                : PaymentResult.failure("Failed to extend authorization."))
            .orElse(PaymentResult.failure("Payment not found."));
    }

    public PaymentResult inquireTransaction(String orderId) {
        return paymentRepository.findByOrderId(orderId)
            .map(p -> PaymentResult.success("Inquiry: " + sepClient.inquire(orderId)))
            .orElse(PaymentResult.failure("Payment not found."));
    }

    public PaymentResult reverseTransaction(String orderId) {
        return paymentRepository.findByOrderId(orderId)
            .filter(p -> "SEP".equalsIgnoreCase(p.getMethod()))
            .map(p -> sepClient.reverse(orderId)
                ? PaymentResult.success("Transaction reversed.")
                : PaymentResult.failure("Reverse failed."))
            .orElse(PaymentResult.failure("Payment not found."));
    }

    public PaymentResult fetchTransactionDetails(String orderId) {
        return paymentRepository.findByOrderId(orderId)
            .map(p -> PaymentResult.success("Details: " + sepClient.getTransactionDetails(orderId)))
            .orElse(PaymentResult.failure("Payment not found."));
    }

    public PaymentResult isDuplicateTransaction(String orderId) {
        boolean exists = paymentRepository.findByOrderId(orderId).isPresent();
        return exists ? PaymentResult.success("Duplicate transaction detected.")
                      : PaymentResult.failure("No duplicate transaction found.");
    }

    public PaymentResult markTransactionAsDisputed(String orderId, String reason) {
        return paymentRepository.findByOrderId(orderId)
            .map(p -> {
                p.setStatus("DISPUTED: " + reason);
                paymentRepository.save(p);
                return PaymentResult.success("Transaction marked as disputed.");
            }).orElse(PaymentResult.failure("Payment not found."));
    }

    public PaymentResult resendPaymentNotification(String orderId) {
        return paymentRepository.findByOrderId(orderId)
            .map(p -> sepClient.notify(orderId)
                ? PaymentResult.success("Notification resent.")
                : PaymentResult.failure("Notification failed."))
            .orElse(PaymentResult.failure("Payment not found."));
    }

    public PaymentResult flagSuspiciousTransaction(String orderId) {
        return paymentRepository.findByOrderId(orderId)
            .map(p -> {
                p.setStatus("SUSPICIOUS");
                paymentRepository.save(p);
                return PaymentResult.success("Transaction flagged as suspicious.");
            }).orElse(PaymentResult.failure("Payment not found."));
    }

    public PaymentResult escalateIssueToSupport(String orderId, String issueDescription) {
        return paymentRepository.findByOrderId(orderId)
            .map(p -> {
                p.setStatus("ESCALATED: " + issueDescription);
                paymentRepository.save(p);
                return PaymentResult.success("Issue escalated to support.");
            }).orElse(PaymentResult.failure("Payment not found."));
    }

    public PaymentResult archiveTransaction(String orderId) {
        return paymentRepository.findByOrderId(orderId)
            .map(p -> {
                p.setStatus("ARCHIVED");
                paymentRepository.save(p);
                return PaymentResult.success("Transaction archived.");
            }).orElse(PaymentResult.failure("Payment not found."));
    }

    public PaymentResult anonymizeTransaction(String orderId) {
        return paymentRepository.findByOrderId(orderId)
            .map(p -> {
                p.setOrderId("ANONYMIZED");
                p.setStatus("ANONYMIZED");
                paymentRepository.save(p);
                return PaymentResult.success("Transaction anonymized.");
            }).orElse(PaymentResult.failure("Payment not found."));
    }

    public PaymentResult tagTransactionForAudit(String orderId, String tag) {
        return paymentRepository.findByOrderId(orderId)
            .map(p -> {
                p.setStatus("AUDIT_TAG: " + tag);
                paymentRepository.save(p);
                return PaymentResult.success("Transaction tagged for audit.");
            }).orElse(PaymentResult.failure("Payment not found."));
    }

    public PaymentResult notifyAdminForManualReview(String orderId, String message) {
        return paymentRepository.findByOrderId(orderId)
            .map(p -> PaymentResult.success("Admin notified for review: " + message))
            .orElse(PaymentResult.failure("Payment not found."));
    }

    public interface SepClient {
        boolean initiatePayment(double amount, String orderId);
        boolean refund(double amount, String orderId);
        boolean checkStatus(String orderId);
        boolean cancel(String orderId);
        boolean extend(String orderId);
        String inquire(String orderId);
        boolean reverse(String orderId);
        String getTransactionDetails(String orderId);
        boolean notify(String orderId);
    }
}
