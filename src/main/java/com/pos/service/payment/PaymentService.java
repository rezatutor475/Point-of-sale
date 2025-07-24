package com.pos.service;

import com.pos.model.Order;
import com.pos.model.Payment;
import com.pos.model.PaymentResult;
import com.pos.repository.OrderRepository;
import com.pos.repository.PaymentRepository;
import com.pos.gateway.PaymentGateway;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing payment processing and related operations.
 */
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentGateway paymentGateway;

    public PaymentService(PaymentRepository paymentRepository,
                          OrderRepository orderRepository,
                          PaymentGateway paymentGateway) {
        this.paymentRepository = Objects.requireNonNull(paymentRepository, "paymentRepository cannot be null");
        this.orderRepository = Objects.requireNonNull(orderRepository, "orderRepository cannot be null");
        this.paymentGateway = Objects.requireNonNull(paymentGateway, "paymentGateway cannot be null");
    }

    public PaymentResult processPayment(String orderId, String paymentMethod) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            return new PaymentResult(false, "Order not found.");
        }

        Order order = optionalOrder.get();
        if (order.isPaid()) {
            return new PaymentResult(false, "Order has already been paid.");
        }

        boolean success = paymentGateway.charge(order.getTotalPrice(), paymentMethod);
        if (!success) {
            return new PaymentResult(false, "Payment failed.");
        }

        Payment payment = new Payment(
                UUID.randomUUID().toString(),
                orderId,
                order.getTotalPrice(),
                paymentMethod,
                new Date(),
                "SUCCESS"
        );

        order.setPaid(true);
        orderRepository.save(order);
        paymentRepository.save(payment);

        return new PaymentResult(true, "Payment successful.");
    }

    public Optional<Payment> getPaymentById(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    public boolean refundPayment(String orderId) {
        Optional<Payment> optionalPayment = paymentRepository.findByOrderId(orderId);
        if (optionalPayment.isEmpty()) {
            return false;
        }

        Payment payment = optionalPayment.get();
        boolean refunded = paymentGateway.refund(payment.getAmount(), payment.getMethod());
        if (!refunded) {
            return false;
        }

        orderRepository.findById(orderId).ifPresent(order -> {
            order.setPaid(false);
            orderRepository.save(order);
        });

        paymentRepository.delete(payment.getId());
        return true;
    }

    public List<Payment> getPaymentsByCustomerId(String customerId) {
        return paymentRepository.findAll().stream()
                .filter(payment -> orderRepository.findById(payment.getOrderId())
                        .map(order -> customerId.equals(order.getCustomer().getId()))
                        .orElse(false))
                .collect(Collectors.toList());
    }

    public double getTotalPaymentsByCustomer(String customerId) {
        return getPaymentsByCustomerId(customerId).stream()
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    public List<Payment> getPaymentsWithinDateRange(Date startDate, Date endDate) {
        return paymentRepository.findAll().stream()
                .filter(payment -> {
                    Date date = payment.getPaymentDate();
                    return date != null && !date.before(startDate) && !date.after(endDate);
                })
                .collect(Collectors.toList());
    }

    public List<Payment> getPaymentsByStatus(String status) {
        return paymentRepository.findAll().stream()
                .filter(payment -> status.equalsIgnoreCase(payment.getStatus()))
                .collect(Collectors.toList());
    }

    public boolean updatePaymentStatus(String paymentId, String newStatus) {
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        if (optionalPayment.isEmpty()) {
            return false;
        }

        Payment payment = optionalPayment.get();
        payment.setStatus(newStatus);
        paymentRepository.save(payment);
        return true;
    }

    public boolean deletePayment(String paymentId) {
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        if (optionalPayment.isEmpty()) {
            return false;
        }

        paymentRepository.delete(paymentId);
        return true;
    }

    public boolean hasUnpaidOrders(String customerId) {
        return orderRepository.findAll().stream()
                .anyMatch(order -> customerId.equals(order.getCustomer().getId()) && !order.isPaid());
    }

    public long countFailedPaymentsByCustomer(String customerId) {
        return getPaymentsByCustomerId(customerId).stream()
                .filter(payment -> "FAILED".equalsIgnoreCase(payment.getStatus()))
                .count();
    }

    public Optional<Payment> getLatestPaymentByCustomer(String customerId) {
        return getPaymentsByCustomerId(customerId).stream()
                .max(Comparator.comparing(Payment::getPaymentDate));
    }

    public List<Payment> getPaymentsByMethod(String method) {
        return paymentRepository.findAll().stream()
                .filter(payment -> method.equalsIgnoreCase(payment.getMethod()))
                .collect(Collectors.toList());
    }

    public List<Order> getOrdersWithMultipleFailedPayments() {
        Map<String, Long> failedCounts = paymentRepository.findAll().stream()
                .filter(payment -> "FAILED".equalsIgnoreCase(payment.getStatus()))
                .collect(Collectors.groupingBy(Payment::getOrderId, Collectors.counting()));

        return failedCounts.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(entry -> orderRepository.findById(entry.getKey()))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    public Map<String, Double> getTotalPaymentsGroupedByCustomer() {
        return paymentRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        payment -> orderRepository.findById(payment.getOrderId())
                                .map(order -> order.getCustomer().getId())
                                .orElse("UNKNOWN"),
                        Collectors.summingDouble(Payment::getAmount)
                ));
    }
} 
