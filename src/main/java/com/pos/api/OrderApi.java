package com.pos.api;

import com.pos.model.Order;
import com.pos.model.OrderRequest;
import com.pos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing order-related operations in the POS system.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderApi {

    private final OrderService orderService;

    @Autowired
    public OrderApi(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return orders.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest) {
        Order createdOrder = orderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody OrderRequest orderRequest) {
        return orderService.updateOrder(id, orderRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        return orderService.deleteOrder(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/by-customer")
    public ResponseEntity<List<Order>> getOrdersByCustomer(@RequestParam String customerName) {
        List<Order> orders = orderService.getOrdersByCustomer(customerName);
        return orders.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(orders);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Order>> getOrdersByDateRange(@RequestParam String start, @RequestParam String end) {
        List<Order> orders = orderService.getOrdersByDateRange(start, end);
        return orders.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(orders);
    }

    @GetMapping("/status")
    public ResponseEntity<List<Order>> getOrdersByStatus(@RequestParam String status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        return orders.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(orders);
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id) {
        return orderService.cancelOrder(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Order>> getRecentOrders(@RequestParam(defaultValue = "7") int days) {
        List<Order> orders = orderService.getRecentOrders(days);
        return orders.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(orders);
    }

    @GetMapping("/summary")
    public ResponseEntity<Object> getOrderSummary() {
        return ResponseEntity.ok(orderService.getOrderSummary());
    }

    @GetMapping("/high-value")
    public ResponseEntity<List<Order>> getHighValueOrders(@RequestParam(defaultValue = "1000.0") double threshold) {
        List<Order> orders = orderService.getHighValueOrders(threshold);
        return orders.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(orders);
    }

    @GetMapping("/by-product")
    public ResponseEntity<List<Order>> getOrdersByProduct(@RequestParam String productName) {
        List<Order> orders = orderService.getOrdersByProduct(productName);
        return orders.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(orders);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Order>> getPendingOrders() {
        List<Order> orders = orderService.getPendingOrders();
        return orders.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(orders);
    }

    @PutMapping("/mark-paid/{id}")
    public ResponseEntity<Order> markOrderAsPaid(@PathVariable Long id) {
        return orderService.markOrderAsPaid(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/mark-delivered/{id}")
    public ResponseEntity<Order> markOrderAsDelivered(@PathVariable Long id) {
        return orderService.markOrderAsDelivered(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/mark-failed/{id}")
    public ResponseEntity<Order> markOrderAsFailed(@PathVariable Long id) {
        return orderService.markOrderAsFailed(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getOrderCount() {
        return ResponseEntity.ok(orderService.getOrderCount());
    }

    @GetMapping("/average-value")
    public ResponseEntity<Double> getAverageOrderValue() {
        return ResponseEntity.ok(orderService.getAverageOrderValue());
    }

    @GetMapping("/frequent-customers")
    public ResponseEntity<List<String>> getFrequentCustomers(@RequestParam(defaultValue = "5") int limit) {
        List<String> customers = orderService.getFrequentCustomers(limit);
        return customers.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(customers);
    }

    @GetMapping("/daily-summary")
    public ResponseEntity<Object> getDailyOrderSummary(@RequestParam String date) {
        return ResponseEntity.ok(orderService.getDailyOrderSummary(date));
    }
}
