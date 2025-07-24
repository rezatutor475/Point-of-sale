package com.pos.controller;

import com.pos.model.Product;
import com.pos.service.InventoryService;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * InventoryController manages inventory operations with improved robustness and readability.
 */
public class InventoryController {

    private static final Logger LOGGER = Logger.getLogger(InventoryController.class.getName());
    private final InventoryService inventoryService;

    /**
     * Constructor injecting the InventoryService.
     *
     * @param inventoryService non-null inventory service implementation
     */
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = Objects.requireNonNull(inventoryService, "InventoryService must not be null");
    }

    public Product addProduct(Product product) {
        validateProduct(product);
        try {
            LOGGER.info(() -> "Adding product: " + product.getName());
            return inventoryService.addProduct(product);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to add product", e);
            throw new IllegalStateException("Unable to add product", e);
        }
    }

    public Product updateProduct(Product product) {
        validateProduct(product);
        try {
            LOGGER.info(() -> "Updating product ID: " + product.getId());
            return inventoryService.updateProduct(product);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to update product", e);
            throw new IllegalStateException("Unable to update product", e);
        }
    }

    public boolean deleteProduct(String productId) {
        validateId(productId, "Product ID");
        try {
            LOGGER.info(() -> "Deleting product ID: " + productId);
            return inventoryService.deleteProduct(productId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to delete product", e);
            throw new IllegalStateException("Unable to delete product", e);
        }
    }

    public Product getProductById(String productId) {
        validateId(productId, "Product ID");
        try {
            LOGGER.info(() -> "Fetching product ID: " + productId);
            return inventoryService.getProductById(productId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to fetch product", e);
            throw new IllegalStateException("Unable to fetch product", e);
        }
    }

    public List<Product> listAllProducts() {
        try {
            LOGGER.info("Fetching all products");
            return inventoryService.getAllProducts();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to list products", e);
            throw new IllegalStateException("Unable to list products", e);
        }
    }

    public boolean adjustStock(String productId, int delta) {
        validateId(productId, "Product ID");
        if (delta == 0) {
            throw new IllegalArgumentException("Stock delta must not be zero");
        }
        try {
            LOGGER.info(() -> String.format("Adjusting stock for product ID: %s, delta: %d", productId, delta));
            return inventoryService.adjustStock(productId, delta);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to adjust stock", e);
            throw new IllegalStateException("Unable to adjust stock", e);
        }
    }

    public List<Product> searchProductsByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword must not be empty");
        }
        try {
            LOGGER.info(() -> "Searching products with keyword: " + keyword);
            return inventoryService.searchProductsByName(keyword.trim());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to search products", e);
            throw new IllegalStateException("Unable to search products", e);
        }
    }

    // ========== Private Validation Helpers ==========

    private void validateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product must not be null");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name must not be empty");
        }
        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("Product price must not be negative");
        }
        if (product.getQuantity() < 0) {
            throw new IllegalArgumentException("Product quantity must not be negative");
        }
    }

    private void validateId(String id, String label) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException(label + " must not be null or empty");
        }
    }
}
