package com.pos.inventory;

import com.pos.inventory.model.Product;
import com.pos.inventory.repository.ProductRepository;
import com.pos.inventory.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Unit Tests for InventoryService")
class InventoryServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Returns existing product by ID")
    void testGetProductById_existing() {
        Product product = new Product("P001", "Mouse", 25, 20000L);
        when(productRepository.findById("P001")).thenReturn(Optional.of(product));

        Product result = inventoryService.getProductById("P001");

        assertNotNull(result);
        assertEquals("Mouse", result.getName());
        assertEquals(25, result.getStock());
    }

    @Test
    @DisplayName("Returns null for non-existent product")
    void testGetProductById_nonExistent() {
        when(productRepository.findById("P999")).thenReturn(Optional.empty());

        Product result = inventoryService.getProductById("P999");

        assertNull(result);
    }

    @Test
    @DisplayName("Updates stock for existing product")
    void testUpdateStock_success() {
        Product product = new Product("P002", "Keyboard", 10, 30000L);
        when(productRepository.findById("P002")).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        boolean result = inventoryService.updateStock("P002", 5);

        assertTrue(result);
        assertEquals(15, product.getStock());
        verify(productRepository).save(product);
    }

    @Test
    @DisplayName("Fails to update stock for non-existent product")
    void testUpdateStock_nonExistent() {
        when(productRepository.findById("P404")).thenReturn(Optional.empty());

        boolean result = inventoryService.updateStock("P404", 3);

        assertFalse(result);
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Rejects negative stock update value")
    void testUpdateStock_negativeValue() {
        Product product = new Product("P003", "Monitor", 7, 70000L);
        when(productRepository.findById("P003")).thenReturn(Optional.of(product));

        boolean result = inventoryService.updateStock("P003", -10);

        assertFalse(result);
        assertEquals(7, product.getStock());
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Prevents stock from becoming negative")
    void testUpdateStock_negativeResultingStock() {
        Product product = new Product("P004", "Camera", 3, 250000L);
        when(productRepository.findById("P004")).thenReturn(Optional.of(product));

        boolean result = inventoryService.updateStock("P004", -5);

        assertFalse(result);
        assertEquals(3, product.getStock());
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Handles repository exception gracefully")
    void testUpdateStock_repositoryException() {
        when(productRepository.findById("P500")).thenThrow(new RuntimeException("DB error"));

        boolean result = assertDoesNotThrow(() -> inventoryService.updateStock("P500", 1));

        assertFalse(result);
    }

    @Test
    @DisplayName("Returns all available products")
    void testGetAllProducts() {
        List<Product> products = List.of(
            new Product("P007", "Webcam", 8, 50000L),
            new Product("P008", "Microphone", 6, 60000L)
        );
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = inventoryService.getAllProducts();

        assertEquals(2, result.size());
        assertEquals("Webcam", result.get(0).getName());
    }

    @Test
    @DisplayName("Deletes existing product")
    void testDeleteProduct_existing() {
        Product product = new Product("P009", "Hard Drive", 10, 100000L);
        when(productRepository.findById("P009")).thenReturn(Optional.of(product));

        boolean result = inventoryService.deleteProductById("P009");

        assertTrue(result);
        verify(productRepository).delete(product);
    }

    @Test
    @DisplayName("Fails to delete non-existent product")
    void testDeleteProduct_nonExistent() {
        when(productRepository.findById("P999")).thenReturn(Optional.empty());

        boolean result = inventoryService.deleteProductById("P999");

        assertFalse(result);
        verify(productRepository, never()).delete(any());
    }
}  
