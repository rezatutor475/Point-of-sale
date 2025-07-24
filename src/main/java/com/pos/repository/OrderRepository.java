package com.pos.repository;

import com.pos.model.Order;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * OrderRepository
 *
 * A thread-safe in-memory repository for managing customer orders.
 * Suitable for prototypes and testing environments. For production use,
 * consider integrating with a relational or document database.
 */
public class OrderRepository {

    private final Map<String, Order> orderStore = new ConcurrentHashMap<>();

    public Order save(Order order) {
        if (order.getOrderId() == null || order.getOrderId().isBlank()) {
            order.setOrderId(UUID.randomUUID().toString());
        }
        orderStore.put(order.getOrderId(), order);
        return order;
    }

    public Optional<Order> findById(String orderId) {
        return Optional.ofNullable(orderStore.get(orderId));
    }

    public List<Order> findAll() {
        return new ArrayList<>(orderStore.values());
    }

    public void deleteById(String orderId) {
        orderStore.remove(orderId);
    }

    public void deleteAll() {
        orderStore.clear();
    }

    public boolean existsById(String orderId) {
        return orderStore.containsKey(orderId);
    }

    public List<Order> findByCustomerId(String customerId) {
        return orderStore.values().stream()
                .filter(order -> customerId.equals(order.getCustomerId()))
                .collect(Collectors.toList());
    }

    public int count() {
        return orderStore.size();
    }

    public double totalRevenue() {
        return orderStore.values().stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();
    }

    public List<Order> findOrdersBetween(Date from, Date to) {
        return orderStore.values().stream()
                .filter(order -> order.getOrderDate() != null)
                .filter(order -> !order.getOrderDate().before(from) && !order.getOrderDate().after(to))
                .collect(Collectors.toList());
    }

    public Optional<Order> findLatestOrder() {
        return orderStore.values().stream()
                .filter(order -> order.getOrderDate() != null)
                .max(Comparator.comparing(Order::getOrderDate));
    }

    public Optional<Order> findEarliestOrder() {
        return orderStore.values().stream()
                .filter(order -> order.getOrderDate() != null)
                .min(Comparator.comparing(Order::getOrderDate));
    }

    public List<Order> findOrdersAboveAmount(double threshold) {
        return orderStore.values().stream()
                .filter(order -> order.getTotalAmount() > threshold)
                .collect(Collectors.toList());
    }

    public Map<String, Long> countOrdersByCustomer() {
        return orderStore.values().stream()
                .filter(order -> order.getCustomerId() != null)
                .collect(Collectors.groupingBy(Order::getCustomerId, Collectors.counting()));
    }

    public double averageOrderValue() {
        return orderStore.values().stream()
                .mapToDouble(Order::getTotalAmount)
                .average()
                .orElse(0.0);
    }

    public List<Order> findAllSortedByDateDesc() {
        return orderStore.values().stream()
                .filter(order -> order.getOrderDate() != null)
                .sorted(Comparator.comparing(Order::getOrderDate).reversed())
                .collect(Collectors.toList());
    }

    public List<Order> findByStatus(String status) {
        return orderStore.values().stream()
                .filter(order -> order.getStatus() != null && status.equalsIgnoreCase(order.getStatus()))
                .collect(Collectors.toList());
    }

    public long countDistinctCustomers() {
        return orderStore.values().stream()
                .map(Order::getCustomerId)
                .filter(Objects::nonNull)
                .distinct()
                .count();
    }

    public Map<String, Long> countOrdersPerMonth() {
        return orderStore.values().stream()
                .filter(order -> order.getOrderDate() != null)
                .collect(Collectors.groupingBy(order -> {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(order.getOrderDate());
                    return String.format("%04d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
                }, Collectors.counting()));
    }

    public List<Order> findByCustomerAndDateRange(String customerId, Date from, Date to) {
        return orderStore.values().stream()
                .filter(order -> customerId.equals(order.getCustomerId()))
                .filter(order -> order.getOrderDate() != null)
                .filter(order -> !order.getOrderDate().before(from) && !order.getOrderDate().after(to))
                .collect(Collectors.toList());
    }

    public List<Order> findTopNOrdersByAmount(int n) {
        return orderStore.values().stream()
                .sorted(Comparator.comparingDouble(Order::getTotalAmount).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    public List<Order> findOrdersContainingProduct(String productId) {
        return orderStore.values().stream()
                .filter(order -> order.getOrderItems() != null)
                .filter(order -> order.getOrderItems().stream()
                        .anyMatch(item -> productId.equals(item.getProductId())))
                .collect(Collectors.toList());
    }

    public Map<String, Integer> totalProductQuantitiesSold() {
        Map<String, Integer> result = new HashMap<>();
        orderStore.values().stream()
                .filter(order -> order.getOrderItems() != null)
                .forEach(order -> order.getOrderItems().forEach(item ->
                        result.merge(item.getProductId(), item.getQuantity(), Integer::sum)));
        return result;
    }
}
