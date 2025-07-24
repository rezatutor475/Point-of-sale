package com.pos.order;

import com.pos.order.model.Order;
import com.pos.order.model.OrderItem;
import com.pos.order.repository.OrderRepository;
import com.pos.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Unit Tests for OrderService")
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should retrieve order by valid ID")
    void testGetOrderById_Valid() {
        Order mockOrder = new Order("ORD001", List.of(new OrderItem("P001", 1)), 15000L);
        when(orderRepository.findById("ORD001")).thenReturn(Optional.of(mockOrder));

        Order result = orderService.getOrderById("ORD001");

        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals("ORD001", result.getId()),
            () -> assertEquals(15000L, result.getTotalAmount()),
            () -> assertEquals(1, result.getItems().size())
        );
    }

    @Test
    @DisplayName("Should return null for invalid ID")
    void testGetOrderById_Invalid() {
        when(orderRepository.findById("ORD999")).thenReturn(Optional.empty());

        Order result = orderService.getOrderById("ORD999");

        assertNull(result);
    }

    @Test
    @DisplayName("Should create valid order")
    void testCreateOrder_Valid() {
        Order newOrder = new Order("ORD002", List.of(new OrderItem("P002", 2)), 20000L);
        when(orderRepository.save(any(Order.class))).thenReturn(newOrder);

        Order result = orderService.createOrder(newOrder);

        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals("ORD002", result.getId()),
            () -> assertEquals(20000L, result.getTotalAmount()),
            () -> verify(orderRepository).save(newOrder)
        );
    }

    @Test
    @DisplayName("Should handle null order creation")
    void testCreateOrder_Null() {
        Order result = orderService.createOrder(null);

        assertNull(result);
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete existing order")
    void testDeleteOrderById_Exists() {
        Order order = new Order("ORD003", Collections.emptyList(), 0L);
        when(orderRepository.findById("ORD003")).thenReturn(Optional.of(order));

        boolean result = orderService.deleteOrderById("ORD003");

        assertTrue(result);
        verify(orderRepository).delete(order);
    }

    @Test
    @DisplayName("Should fail to delete non-existent order")
    void testDeleteOrderById_NotExists() {
        when(orderRepository.findById("ORD404")).thenReturn(Optional.empty());

        boolean result = orderService.deleteOrderById("ORD404");

        assertFalse(result);
        verify(orderRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should handle exception during deletion")
    void testDeleteOrderById_Exception() {
        when(orderRepository.findById("ORD500")).thenThrow(new RuntimeException("DB Error"));

        boolean result = assertDoesNotThrow(() -> orderService.deleteOrderById("ORD500"));

        assertFalse(result);
    }

    @Test
    @DisplayName("Should return all existing orders")
    void testGetAllOrders_WithData() {
        List<Order> orders = List.of(
            new Order("ORD010", List.of(new OrderItem("P010", 1)), 12000L),
            new Order("ORD011", List.of(new OrderItem("P011", 3)), 36000L)
        );
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();

        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(2, result.size()),
            () -> assertEquals("ORD010", result.get(0).getId()),
            () -> assertEquals("ORD011", result.get(1).getId())
        );
    }

    @Test
    @DisplayName("Should return empty list when no orders exist")
    void testGetAllOrders_Empty() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        List<Order> result = orderService.getAllOrders();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should update existing order")
    void testUpdateOrder_Valid() {
        Order existingOrder = new Order("ORD020", List.of(new OrderItem("P020", 1)), 10000L);
        Order updatedOrder = new Order("ORD020", List.of(new OrderItem("P020", 2)), 20000L);

        when(orderRepository.findById("ORD020")).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

        Order result = orderService.updateOrder("ORD020", updatedOrder);

        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(20000L, result.getTotalAmount()),
            () -> verify(orderRepository).save(updatedOrder)
        );
    }

    @Test
    @DisplayName("Should not update non-existent order")
    void testUpdateOrder_NotFound() {
        Order updatedOrder = new Order("ORD999", List.of(new OrderItem("P999", 1)), 5000L);
        when(orderRepository.findById("ORD999")).thenReturn(Optional.empty());

        Order result = orderService.updateOrder("ORD999", updatedOrder);

        assertNull(result);
        verify(orderRepository, never()).save(any());
    }
}
