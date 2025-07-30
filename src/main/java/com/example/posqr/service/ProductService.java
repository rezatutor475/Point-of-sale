package com.example.posqr.service;

import com.example.posqr.model.Product;
import com.example.posqr.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        product.setCreatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id).map(product -> {
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setPrice(updatedProduct.getPrice());
            product.setStock(updatedProduct.getStock());
            product.setCategory(updatedProduct.getCategory());
            product.setUpdatedAt(LocalDateTime.now());
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> findLowStockProducts(int threshold) {
        return productRepository.findByStockLessThan(threshold);
    }

    public List<Product> findExpiredProducts() {
        return productRepository.findExpiredProducts();
    }

    public long countExpiredProducts() {
        return productRepository.countExpiredProducts();
    }

    public BigDecimal findMaxProductPrice() {
        return productRepository.findMaxProductPrice();
    }

    public BigDecimal findMinProductPrice() {
        return productRepository.findMinProductPrice();
    }

    public long countProductsCreatedSince(LocalDateTime date) {
        return productRepository.countProductsCreatedSince(date);
    }

    public long countProductsUpdatedSince(LocalDateTime date) {
        return productRepository.countProductsUpdatedSince(date);
    }

    public void deleteExpiredProducts() {
        productRepository.deleteExpiredProducts();
    }

    public List<Product> findRecentlySoldProducts(LocalDateTime since) {
        return productRepository.findRecentlySoldProducts(since);
    }

    public long countRecentlySoldProducts(LocalDateTime since) {
        return productRepository.countRecentlySoldProducts(since);
    }

    public List<Object[]> countProductsByCategory() {
        return productRepository.countProductsByCategory();
    }

    public List<Object[]> averagePriceByCategory() {
        return productRepository.averagePriceByCategory();
    }

    public List<Object[]> totalStockByCategory() {
        return productRepository.totalStockByCategory();
    }

    public List<Object[]> countProductsBySupplier() {
        return productRepository.countProductsBySupplier();
    }

    public List<Product> findProductsNeedingRestock(int threshold) {
        return productRepository.findProductsNeedingRestock(threshold);
    }

    public List<Product> findDiscontinuedProducts() {
        return productRepository.findDiscontinuedProducts();
    }

    public BigDecimal getAverageProductPrice() {
        return productRepository.getAverageProductPrice();
    }

    public List<Product> findProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findProductsByPriceRange(minPrice, maxPrice);
    }

    public long countOutOfStockProducts() {
        return productRepository.countOutOfStockProducts();
    }

    public List<Object[]> topSellingProducts(int limit) {
        return productRepository.topSellingProducts(limit);
    }

    public List<Product> findProductsCreatedBetween(LocalDateTime start, LocalDateTime end) {
        return productRepository.findProductsCreatedBetween(start, end);
    }

    public List<Product> searchProductsByKeyword(String keyword) {
        return productRepository.searchProductsByKeyword(keyword);
    }

    public List<Product> findProductsWithNegativeStock() {
        return productRepository.findProductsWithNegativeStock();
    }

    public List<Product> findProductsByExactName(String name) {
        return productRepository.findProductsByExactName(name);
    }

    public List<Product> findProductsUpdatedBetween(LocalDateTime start, LocalDateTime end) {
        return productRepository.findProductsUpdatedBetween(start, end);
    }

    public boolean existsByProductName(String name) {
        return productRepository.existsByProductName(name);
    }
} 
