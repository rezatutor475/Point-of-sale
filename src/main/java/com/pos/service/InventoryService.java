package com.pos.service;

import com.pos.model.Inventory;
import com.pos.model.Product;
import com.pos.repository.InventoryRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * InventoryService provides comprehensive operations for managing inventory.
 */
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = Objects.requireNonNull(inventoryRepository, "InventoryRepository cannot be null");
    }

    public Inventory saveInventory(Inventory inventory) {
        validateInventory(inventory);
        return inventoryRepository.save(inventory);
    }

    public Optional<Inventory> getInventoryByProductId(String productId) {
        return isNullOrEmpty(productId) ? Optional.empty() : inventoryRepository.findByProductId(productId);
    }

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public boolean deleteInventoryById(String inventoryId) {
        if (isNullOrEmpty(inventoryId) || !inventoryRepository.existsById(inventoryId)) return false;
        inventoryRepository.deleteById(inventoryId);
        return true;
    }

    public boolean updateInventory(String inventoryId, Inventory updatedInventory) {
        if (isNullOrEmpty(inventoryId) || updatedInventory == null) return false;

        return inventoryRepository.findById(inventoryId)
                .map(existing -> {
                    existing.setQuantity(updatedInventory.getQuantity());
                    existing.setLocation(updatedInventory.getLocation());
                    existing.setProduct(updatedInventory.getProduct());
                    existing.setLastUpdated(new Date());
                    return inventoryRepository.save(existing) != null;
                })
                .orElse(false);
    }

    public boolean exists(String inventoryId) {
        return !isNullOrEmpty(inventoryId) && inventoryRepository.existsById(inventoryId);
    }

    public long countInventoryItems() {
        return inventoryRepository.count();
    }

    public List<Inventory> findInventoryByLocation(String locationFragment) {
        if (!hasText(locationFragment)) return Collections.emptyList();
        String lowerCase = locationFragment.toLowerCase();
        return inventoryRepository.findAll().stream()
                .filter(i -> i.getLocation() != null && i.getLocation().toLowerCase().contains(lowerCase))
                .collect(Collectors.toList());
    }

    public List<Inventory> findLowStockItems(int threshold) {
        return inventoryRepository.findAll().stream()
                .filter(i -> i.getQuantity() < threshold)
                .collect(Collectors.toList());
    }

    public Map<String, Long> countInventoryByLocation() {
        return inventoryRepository.findAll().stream()
                .filter(i -> i.getLocation() != null)
                .collect(Collectors.groupingBy(
                        i -> i.getLocation().toLowerCase(),
                        Collectors.counting()
                ));
    }

    public Optional<Inventory> findExactInventoryByProduct(Product product) {
        if (product == null || isNullOrEmpty(product.getId())) return Optional.empty();
        return inventoryRepository.findAll().stream()
                .filter(i -> product.equals(i.getProduct()))
                .findFirst();
    }

    public List<Inventory> getInventorySortedByQuantity(boolean ascending) {
        Comparator<Inventory> comparator = Comparator.comparingInt(Inventory::getQuantity);
        return inventoryRepository.findAll().stream()
                .sorted(ascending ? comparator : comparator.reversed())
                .collect(Collectors.toList());
    }

    public List<Inventory> findInventoryWithNoLocation() {
        return inventoryRepository.findAll().stream()
                .filter(i -> isNullOrEmpty(i.getLocation()))
                .collect(Collectors.toList());
    }

    public Map<String, List<Inventory>> groupInventoryByProductCategory() {
        return inventoryRepository.findAll().stream()
                .filter(i -> i.getProduct() != null && i.getProduct().getCategory() != null)
                .collect(Collectors.groupingBy(
                        i -> i.getProduct().getCategory().toLowerCase()
                ));
    }

    public List<Inventory> findInventoryByQuantityRange(int min, int max) {
        return inventoryRepository.findAll().stream()
                .filter(i -> i.getQuantity() >= min && i.getQuantity() <= max)
                .collect(Collectors.toList());
    }

    public double calculateTotalInventoryValue() {
        return inventoryRepository.findAll().stream()
                .filter(i -> i.getProduct() != null && i.getProduct().getPrice() != null)
                .mapToDouble(i -> i.getQuantity() * i.getProduct().getPrice())
                .sum();
    }

    public List<Inventory> findInventoryUpdatedAfter(Date date) {
        return inventoryRepository.findAll().stream()
                .filter(i -> i.getLastUpdated() != null && i.getLastUpdated().after(date))
                .collect(Collectors.toList());
    }

    public Map<String, Integer> getTotalQuantityPerProductId() {
        return inventoryRepository.findAll().stream()
                .filter(i -> i.getProduct() != null && i.getProduct().getId() != null)
                .collect(Collectors.groupingBy(
                        i -> i.getProduct().getId(),
                        Collectors.summingInt(Inventory::getQuantity)
                ));
    }

    public List<Inventory> findInventoryByProductName(String nameFragment) {
        if (!hasText(nameFragment)) return Collections.emptyList();
        String lowerCase = nameFragment.toLowerCase();
        return inventoryRepository.findAll().stream()
                .filter(i -> i.getProduct() != null && i.getProduct().getName() != null && i.getProduct().getName().toLowerCase().contains(lowerCase))
                .collect(Collectors.toList());
    }

    public Optional<Inventory> findMostStockedProduct() {
        return inventoryRepository.findAll().stream()
                .max(Comparator.comparingInt(Inventory::getQuantity));
    }

    public Map<String, Double> getTotalInventoryValuePerCategory() {
        return inventoryRepository.findAll().stream()
                .filter(i -> i.getProduct() != null && i.getProduct().getCategory() != null && i.getProduct().getPrice() != null)
                .collect(Collectors.groupingBy(
                        i -> i.getProduct().getCategory().toLowerCase(),
                        Collectors.summingDouble(i -> i.getQuantity() * i.getProduct().getPrice())
                ));
    }

    public List<Inventory> findInventoryByProductCategory(String categoryFragment) {
        if (!hasText(categoryFragment)) return Collections.emptyList();
        String lower = categoryFragment.toLowerCase();
        return inventoryRepository.findAll().stream()
                .filter(i -> i.getProduct() != null && i.getProduct().getCategory() != null && i.getProduct().getCategory().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public List<Inventory> findInventoryWithNegativeQuantity() {
        return inventoryRepository.findAll().stream()
                .filter(i -> i.getQuantity() < 0)
                .collect(Collectors.toList());
    }

    private void validateInventory(Inventory inventory) {
        if (inventory == null) throw new IllegalArgumentException("Inventory cannot be null");
        if (inventory.getProduct() == null || isNullOrEmpty(inventory.getProduct().getId())) {
            throw new IllegalArgumentException("Inventory must be associated with a valid product");
        }
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
