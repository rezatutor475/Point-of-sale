package com.pos.service;

import com.pos.model.Order;
import com.pos.model.OrderItem;
import com.pos.repository.OrderRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class responsible for generating analytical reports for orders, customers, and products.
 * Enhances data clarity and enables key business insights.
 */
public class ReportService {

    private final OrderRepository orderRepository;

    public ReportService(OrderRepository orderRepository) {
        this.orderRepository = Objects.requireNonNull(orderRepository, "OrderRepository must not be null");
    }

    public double getTotalRevenue() {
        return orderRepository.findAll().stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
    }

    public Map<String, Double> getRevenueByCustomer() {
        return orderRepository.findAll().stream()
                .filter(order -> order.getCustomer() != null && order.getCustomer().getId() != null)
                .collect(Collectors.groupingBy(
                        order -> order.getCustomer().getId(),
                        Collectors.summingDouble(Order::getTotalPrice)
                ));
    }

    public List<Order> getOrdersWithinDateRange(Date start, Date end) {
        if (start == null || end == null || start.after(end)) return Collections.emptyList();
        return orderRepository.findAll().stream()
                .filter(o -> o.getOrderDate() != null && !o.getOrderDate().before(start) && !o.getOrderDate().after(end))
                .collect(Collectors.toList());
    }

    public List<String> getTopCustomersByRevenue(int topN) {
        return getRevenueByCustomer().entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public double getAverageRevenuePerOrder() {
        List<Order> orders = orderRepository.findAll();
        return orders.isEmpty() ? 0.0 : orders.stream().mapToDouble(Order::getTotalPrice).average().orElse(0.0);
    }

    public Optional<Order> getLargestOrder() {
        return orderRepository.findAll().stream()
                .max(Comparator.comparingDouble(Order::getTotalPrice));
    }

    public long getTotalOrderCount() {
        return orderRepository.count();
    }

    public double getAverageItemsPerOrder() {
        List<Order> orders = orderRepository.findAll();
        return orders.isEmpty() ? 0.0 : orders.stream()
                .mapToInt(order -> order.getOrderItems().size())
                .average().orElse(0.0);
    }

    public List<String> getTopSellingProducts(int topN) {
        return orderRepository.findAll().stream()
                .flatMap(order -> order.getOrderItems().stream())
                .filter(item -> item.getProduct() != null && item.getProduct().getId() != null)
                .collect(Collectors.groupingBy(
                        item -> item.getProduct().getId(),
                        Collectors.summingInt(OrderItem::getQuantity)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<String> getTopCustomersByOrderCount(int topN) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getCustomer() != null && order.getCustomer().getId() != null)
                .collect(Collectors.groupingBy(
                        order -> order.getCustomer().getId(),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public Map<String, Double> getDailyRevenue() {
        return orderRepository.findAll().stream()
                .filter(order -> order.getOrderDate() != null)
                .collect(Collectors.groupingBy(
                        order -> order.getOrderDate().toString(),
                        Collectors.summingDouble(Order::getTotalPrice)
                ));
    }

    public List<String> getLowVolumeProducts(int threshold) {
        return orderRepository.findAll().stream()
                .flatMap(order -> order.getOrderItems().stream())
                .filter(item -> item.getProduct() != null && item.getProduct().getId() != null)
                .collect(Collectors.groupingBy(
                        item -> item.getProduct().getId(),
                        Collectors.summingInt(OrderItem::getQuantity)
                ))
                .entrySet().stream()
                .filter(entry -> entry.getValue() < threshold)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public Map<String, Order> getFirstOrderPerCustomer() {
        return orderRepository.findAll().stream()
                .filter(order -> order.getCustomer() != null && order.getCustomer().getId() != null && order.getOrderDate() != null)
                .collect(Collectors.groupingBy(
                        order -> order.getCustomer().getId(),
                        Collectors.collectingAndThen(
                                Collectors.minBy(Comparator.comparing(Order::getOrderDate)),
                                optional -> optional.orElse(null)
                        )
                ));
    }

    public Map<String, Double> getAverageSpendingPerCustomer() {
        Map<String, Double> revenue = getRevenueByCustomer();
        Map<String, Long> count = orderRepository.findAll().stream()
                .filter(order -> order.getCustomer() != null && order.getCustomer().getId() != null)
                .collect(Collectors.groupingBy(
                        order -> order.getCustomer().getId(),
                        Collectors.counting()
                ));

        return revenue.entrySet().stream()
                .filter(entry -> count.containsKey(entry.getKey()) && count.get(entry.getKey()) > 0)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue() / count.get(entry.getKey())
                ));
    }

    public Map<String, Integer> getTotalQuantitySoldByProduct() {
        return orderRepository.findAll().stream()
                .flatMap(order -> order.getOrderItems().stream())
                .filter(item -> item.getProduct() != null && item.getProduct().getId() != null)
                .collect(Collectors.groupingBy(
                        item -> item.getProduct().getId(),
                        Collectors.summingInt(OrderItem::getQuantity)
                ));
    }

    public Optional<Order> getEarliestOrder() {
        return orderRepository.findAll().stream()
                .filter(order -> order.getOrderDate() != null)
                .min(Comparator.comparing(Order::getOrderDate));
    }

    public Optional<Order> getLatestOrder() {
        return orderRepository.findAll().stream()
                .filter(order -> order.getOrderDate() != null)
                .max(Comparator.comparing(Order::getOrderDate));
    }

    public Map<String, Double> getMonthlyRevenue() {
        return orderRepository.findAll().stream()
                .filter(order -> order.getOrderDate() != null)
                .collect(Collectors.groupingBy(
                        order -> {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(order.getOrderDate());
                            return cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1);
                        },
                        Collectors.summingDouble(Order::getTotalPrice)
                ));
    }

    public List<String> getInactiveCustomers(int monthsThreshold) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -monthsThreshold);
        Date thresholdDate = cal.getTime();

        return orderRepository.findAll().stream()
                .filter(order -> order.getCustomer() != null && order.getCustomer().getId() != null && order.getOrderDate() != null)
                .collect(Collectors.groupingBy(
                        order -> order.getCustomer().getId(),
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparing(Order::getOrderDate)),
                                optional -> optional.map(Order::getOrderDate).orElse(null)
                        )
                ))
                .entrySet().stream()
                .filter(entry -> entry.getValue() != null && entry.getValue().before(thresholdDate))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
