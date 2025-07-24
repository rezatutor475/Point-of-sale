package com.pos.service;

import com.pos.model.Order;
import com.pos.model.OrderItem;
import com.pos.model.Product;
import com.pos.repository.OrderRepository;
import com.pos.repository.ProductRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for handling all business logic related to Orders.
 */
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = Objects.requireNonNull(orderRepository, "OrderRepository cannot be null");
        this.productRepository = Objects.requireNonNull(productRepository, "ProductRepository cannot be null");
    }

    public Order saveOrder(Order order) {
        validateOrder(order);
        calculateTotalPrice(order);
        order.setOrderDate(new Date());
        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(String orderId) {
        return isNullOrEmpty(orderId) ? Optional.empty() : orderRepository.findById(orderId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public boolean deleteOrderById(String orderId) {
        if (isNullOrEmpty(orderId) || !orderRepository.existsById(orderId)) return false;
        orderRepository.deleteById(orderId);
        return true;
    }

    public boolean updateOrder(String orderId, Order updatedOrder) {
        if (isNullOrEmpty(orderId) || updatedOrder == null) return false;
        return orderRepository.findById(orderId).map(existingOrder -> {
            existingOrder.setCustomer(updatedOrder.getCustomer());
            existingOrder.setOrderItems(updatedOrder.getOrderItems());
            calculateTotalPrice(existingOrder);
            existingOrder.setOrderDate(new Date());
            return orderRepository.save(existingOrder) != null;
        }).orElse(false);
    }

    public long countOrders() {
        return orderRepository.count();
    }

    public List<Order> findOrdersByCustomerName(String nameFragment) {
        if (!hasText(nameFragment)) return Collections.emptyList();
        return orderRepository.findAll().stream()
                .filter(order -> Optional.ofNullable(order.getCustomer())
                        .map(c -> c.getName() != null && c.getName().toLowerCase().contains(nameFragment.toLowerCase()))
                        .orElse(false))
                .collect(Collectors.toList());
    }

    public List<Order> findOrdersByProduct(String productId) {
        if (isNullOrEmpty(productId)) return Collections.emptyList();
        return orderRepository.findAll().stream()
                .filter(order -> order.getOrderItems().stream()
                        .anyMatch(item -> productId.equals(Optional.ofNullable(item.getProduct()).map(Product::getId).orElse(null))))
                .collect(Collectors.toList());
    }

    public double calculateTotalRevenue() {
        return orderRepository.findAll().stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
    }

    public List<Order> findOrdersAfterDate(Date date) {
        if (date == null) return Collections.emptyList();
        return orderRepository.findAll().stream()
                .filter(order -> Optional.ofNullable(order.getOrderDate()).map(d -> d.after(date)).orElse(false))
                .collect(Collectors.toList());
    }

    public Map<String, Long> countOrdersByCustomerId() {
        return orderRepository.findAll().stream()
                .filter(order -> order.getCustomer() != null && order.getCustomer().getId() != null)
                .collect(Collectors.groupingBy(order -> order.getCustomer().getId(), Collectors.counting()));
    }

    public Optional<Order> findMostExpensiveOrder() {
        return orderRepository.findAll().stream()
                .max(Comparator.comparingDouble(Order::getTotalPrice));
    }

    public List<Order> findOrdersWithinTotalPriceRange(double min, double max) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getTotalPrice() >= min && order.getTotalPrice() <= max)
                .collect(Collectors.toList());
    }

    public Map<String, Double> getTotalRevenueByCustomerId() {
        return orderRepository.findAll().stream()
                .filter(order -> order.getCustomer() != null && order.getCustomer().getId() != null)
                .collect(Collectors.groupingBy(order -> order.getCustomer().getId(), Collectors.summingDouble(Order::getTotalPrice)));
    }

    public double getAverageOrderValue() {
        List<Order> orders = orderRepository.findAll();
        return orders.isEmpty() ? 0.0 : orders.stream()
                .mapToDouble(Order::getTotalPrice)
                .average()
                .orElse(0.0);
    }

    public List<Order> getTopNOrdersByTotalPrice(int n) {
        return orderRepository.findAll().stream()
                .sorted(Comparator.comparingDouble(Order::getTotalPrice).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    public Map<String, Integer> getTotalQuantityByProductId() {
        return orderRepository.findAll().stream()
                .flatMap(order -> order.getOrderItems().stream())
                .filter(item -> item.getProduct() != null && item.getProduct().getId() != null)
                .collect(Collectors.groupingBy(item -> item.getProduct().getId(), Collectors.summingInt(OrderItem::getQuantity)));
    }

    public Set<String> getAllUniqueProductIdsOrdered() {
        return orderRepository.findAll().stream()
                .flatMap(order -> order.getOrderItems().stream())
                .map(item -> Optional.ofNullable(item.getProduct()).map(Product::getId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private void calculateTotalPrice(Order order) {
        double total = order.getOrderItems().stream()
                .filter(Objects::nonNull)
                .mapToDouble(item -> {
                    Product product = item.getProduct();
                    Integer quantity = item.getQuantity();
                    return (product != null && product.getPrice() != null && quantity != null)
                            ? product.getPrice() * quantity
                            : 0.0;
                })
                .sum();
        order.setTotalPrice(total);
    }

    private void validateOrder(Order order) {
        if (order == null) throw new IllegalArgumentException("Order cannot be null");
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
        for (OrderItem item : order.getOrderItems()) {
            if (item.getProduct() == null || isNullOrEmpty(item.getProduct().getId())) {
                throw new IllegalArgumentException("Each order item must contain a valid product");
            }
        }
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
