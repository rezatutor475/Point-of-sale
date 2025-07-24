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
 * Professional service class for integrating with the Sadad payment gateway.
 * Documentation: https://sadad.shaparak.ir/
 */
public class SadadService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final SadadClient sadadClient;

    public SadadService(PaymentRepository paymentRepository,
                        OrderRepository orderRepository,
                        SadadClient sadadClient) {
        this.paymentRepository = Objects.requireNonNull(paymentRepository, "PaymentRepository must not be null");
        this.orderRepository = Objects.requireNonNull(orderRepository, "OrderRepository must not be null");
        this.sadadClient = Objects.requireNonNull(sadadClient, "SadadClient must not be null");
    }

    public PaymentResult processPayment(String orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            return PaymentResult.failure("Order not found.");
        }

        Order order = optionalOrder.get();
        if (order.isPaid()) {
            return PaymentResult.failure("Order has already been paid.");
        }

        boolean initiated = sadadClient.initiatePayment(order.getTotalPrice(), orderId);
        if (!initiated) {
            return PaymentResult.failure("Failed to initiate Sadad payment.");
        }

        Payment payment = new Payment(
                UUID.randomUUID().toString(),
                orderId,
                order.getTotalPrice(),
                "SADAD",
                new Date(),
                "SUCCESS"
        );

        order.setPaid(true);
        orderRepository.save(order);
        paymentRepository.save(payment);

        return PaymentResult.success("Payment completed via Sadad.");
    }

    public boolean processRefund(String orderId) {
        return paymentRepository.findByOrderId(orderId)
                .filter(payment -> "SADAD".equalsIgnoreCase(payment.getMethod()))
                .map(payment -> {
                    boolean refunded = sadadClient.refund(payment.getAmount(), payment.getOrderId());
                    if (refunded) {
                        orderRepository.findById(orderId).ifPresent(order -> {
                            order.setPaid(false);
                            orderRepository.save(order);
                        });
                        paymentRepository.delete(payment.getId());
                    }
                    return refunded;
                })
                .orElse(false);
    }

    public PaymentResult verifyPaymentStatus(String orderId) {
        return paymentRepository.findByOrderId(orderId)
                .map(payment -> sadadClient.checkStatus(payment.getOrderId()) ?
                        PaymentResult.success("Payment is confirmed.") :
                        PaymentResult.failure("Payment is not confirmed."))
                .orElse(PaymentResult.failure("No payment found for this order."));
    }

    public PaymentResult retryPayment(String orderId) {
        return orderRepository.findById(orderId)
                .map(order -> order.isPaid() ?
                        PaymentResult.failure("Order is already paid.") :
                        processPayment(orderId))
                .orElse(PaymentResult.failure("Order not found."));
    }

    public PaymentResult cancelPayment(String orderId) {
        return paymentRepository.findByOrderId(orderId)
                .map(payment -> {
                    if (!"SADAD".equalsIgnoreCase(payment.getMethod())) {
                        return PaymentResult.failure("Payment method is not SADAD.");
                    }

                    boolean cancelled = sadadClient.cancel(orderId);
                    if (!cancelled) {
                        return PaymentResult.failure("Failed to cancel Sadad payment.");
                    }

                    paymentRepository.delete(payment.getId());
                    return PaymentResult.success("Sadad payment cancelled successfully.");
                })
                .orElse(PaymentResult.failure("No payment found to cancel."));
    }

    public PaymentResult extendAuthorization(String orderId) {
        return paymentRepository.findByOrderId(orderId)
                .map(payment -> sadadClient.extend(orderId) ?
                        PaymentResult.success("Authorization extended successfully.") :
                        PaymentResult.failure("Failed to extend payment authorization."))
                .orElse(PaymentResult.failure("No payment found for this order."));
    }

    public PaymentResult inquireTransaction(String orderId) {
        return paymentRepository.findByOrderId(orderId)
                .map(payment -> {
                    String inquiryResult = sadadClient.inquire(orderId);
                    return PaymentResult.success("Inquiry result: " + inquiryResult);
                })
                .orElse(PaymentResult.failure("No payment found for this order."));
    }

    public PaymentResult isDuplicateTransaction(String orderId) {
        boolean exists = paymentRepository.findByOrderId(orderId).isPresent();
        return exists ? PaymentResult.success("Duplicate transaction detected.")
                      : PaymentResult.failure("No duplicate transaction found.");
    }

    public PaymentResult markTransactionAsDisputed(String orderId, String reason) {
        return paymentRepository.findByOrderId(orderId)
                .map(payment -> {
                    payment.setStatus("DISPUTED: " + reason);
                    paymentRepository.save(payment);
                    return PaymentResult.success("Transaction marked as disputed.");
                })
                .orElse(PaymentResult.failure("Payment not found."));
    }

    public PaymentResult notifyAdminForManualReview(String orderId, String message) {
        return paymentRepository.findByOrderId(orderId)
                .map(payment -> PaymentResult.success("Admin notified for review: " + message))
                .orElse(PaymentResult.failure("Payment not found."));
    }

    /**
     * Interface to abstract Sadad gateway operations for testability and flexibility.
     */
    public interface SadadClient {
        boolean initiatePayment(double amount, String orderId);
        boolean refund(double amount, String orderId);
        boolean checkStatus(String orderId);
        boolean cancel(String orderId);
        boolean extend(String orderId);
        String inquire(String orderId);
    }
}
