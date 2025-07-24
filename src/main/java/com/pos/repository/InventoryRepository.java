package com.pos.repository;

import com.pos.model.Product;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * InventoryRepository
 *
 * A robust and thread-safe in-memory repository for managing product inventory.
 * Designed for prototyping, testing, or lightweight applications. For production,
 * consider integrating a persistent storage solution.
 */
public class InventoryRepository {

    private final Map<String, Product> productStore = new ConcurrentHashMap<>();

    public Optional<Product> findById(String productId) {
        return Optional.ofNullable(productStore.get(productId));
    }

    public List<Product> findAll() {
        return new ArrayList<>(productStore.values());
    }

    public Product save(Product product) {
        if (product.getProductId() == null || product.getProductId().isBlank()) {
            product.setProductId(UUID.randomUUID().toString());
        }
        productStore.put(product.getProductId(), product);
        return product;
    }

    public void deleteById(String productId) {
        productStore.remove(productId);
    }

    public void deleteAll() {
        productStore.clear();
    }

    public boolean existsById(String productId) {
        return productStore.containsKey(productId);
    }

    public List<Product> findByName(String name) {
        if (name == null) return Collections.emptyList();
        return productStore.values().stream()
                .filter(p -> name.equalsIgnoreCase(p.getName()))
                .collect(Collectors.toList());
    }

    public boolean updateStock(String productId, int quantity) {
        if (quantity < 0) return false;
        Product product = productStore.get(productId);
        if (product == null) return false;
        product.setStock(quantity);
        return true;
    }

    public boolean increaseStock(String productId, int amount) {
        if (amount < 0) return false;
        Product product = productStore.get(productId);
        if (product == null) return false;
        product.setStock(product.getStock() + amount);
        return true;
    }

    public boolean decreaseStock(String productId, int amount) {
        if (amount < 0) return false;
        Product product = productStore.get(productId);
        if (product == null || product.getStock() < amount) return false;
        product.setStock(product.getStock() - amount);
        return true;
    }

    public List<Product> findLowStock(int threshold) {
        return productStore.values().stream()
                .filter(p -> p.getStock() <= threshold)
                .collect(Collectors.toList());
    }

    public List<Product> findOutOfStock() {
        return productStore.values().stream()
                .filter(p -> p.getStock() == 0)
                .collect(Collectors.toList());
    }

    public int count() {
        return productStore.size();
    }

    public double averageStockLevel() {
        return productStore.values().stream()
                .mapToInt(Product::getStock)
                .average()
                .orElse(0.0);
    }

    public Map<String, Long> countByCategory() {
        return productStore.values().stream()
                .map(Product::getCategory)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
    }

    public Map<String, Integer> totalStockByCategory() {
        return productStore.values().stream()
                .filter(p -> p.getCategory() != null)
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.summingInt(Product::getStock)
                ));
    }

    public Optional<Product> findTopStockedProduct() {
        return productStore.values().stream()
                .max(Comparator.comparingInt(Product::getStock));
    }

    public Optional<Product> findLowestStockedProduct() {
        return productStore.values().stream()
                .filter(p -> p.getStock() > 0)
                .min(Comparator.comparingInt(Product::getStock));
    }

    // --- Additional Features ---

    public List<Product> searchByKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) return Collections.emptyList();
        return productStore.values().stream()
                .filter(p -> p.getName() != null && p.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public boolean renameProduct(String productId, String newName) {
        if (newName == null || newName.isBlank()) return false;
        Product product = productStore.get(productId);
        if (product == null) return false;
        product.setName(newName);
        return true;
    }

    public boolean changeCategory(String productId, String newCategory) {
        Product product = productStore.get(productId);
        if (product == null || newCategory == null || newCategory.isBlank()) return false;
        product.setCategory(newCategory);
        return true;
    }

    public Map<Boolean, List<Product>> partitionByStockAvailability() {
        return productStore.values().stream()
                .collect(Collectors.partitioningBy(p -> p.getStock() > 0));
    }

    public List<Product> getSortedByStockDesc() {
        return productStore.values().stream()
                .sorted(Comparator.comparingInt(Product::getStock).reversed())
                .collect(Collectors.toList());
    }

    public List<Product> getSortedByName() {
        return productStore.values().stream()
                .sorted(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }
}
