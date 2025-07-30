package com.example.posqr.controller;

import com.example.posqr.model.Product;
import com.example.posqr.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return new ResponseEntity<>(productService.createProduct(product), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(productService.searchProducts(params));
    }

    @PatchMapping("/{id}/price")
    public ResponseEntity<Product> updateProductPrice(@PathVariable Long id, @RequestParam double price) {
        return ResponseEntity.ok(productService.updateProductPrice(id, price));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<Product> updateProductStock(@PathVariable Long id, @RequestParam int stock) {
        return ResponseEntity.ok(productService.updateProductStock(id, stock));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Product>> createProductsBatch(@RequestBody List<Product> products) {
        return new ResponseEntity<>(productService.createProductsBatch(products), HttpStatus.CREATED);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStockProducts(@RequestParam(defaultValue = "10") int threshold) {
        return ResponseEntity.ok(productService.getLowStockProducts(threshold));
    }

    @PutMapping("/restock")
    public ResponseEntity<List<Product>> restockProducts(@RequestBody Map<Long, Integer> stockUpdates) {
        return ResponseEntity.ok(productService.restockProducts(stockUpdates));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearAllProducts() {
        productService.clearAllProducts();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Product>> getActiveProducts() {
        return ResponseEntity.ok(productService.getActiveProducts());
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Product> deactivateProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.deactivateProduct(id));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Product> activateProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.activateProduct(id));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countProducts() {
        return ResponseEntity.ok(productService.countProducts());
    }

    @GetMapping("/export/csv")
    public ResponseEntity<String> exportProductsToCsv() {
        return ResponseEntity.ok(productService.exportProductsToCsv());
    }

    @PostMapping("/import/csv")
    public ResponseEntity<Void> importProductsFromCsv(@RequestParam("file") MultipartFile file) {
        productService.importProductsFromCsv(file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getProductStatistics() {
        return ResponseEntity.ok(productService.getProductStatistics());
    }

    @GetMapping("/most-expensive")
    public ResponseEntity<Product> getMostExpensiveProduct() {
        return ResponseEntity.ok(productService.getMostExpensiveProduct());
    }

    @GetMapping("/least-expensive")
    public ResponseEntity<Product> getLeastExpensiveProduct() {
        return ResponseEntity.ok(productService.getLeastExpensiveProduct());
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<List<Product>> getOutOfStockProducts() {
        return ResponseEntity.ok(productService.getOutOfStockProducts());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Product>> getRecentlyAddedProducts(@RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(productService.getRecentlyAddedProducts(days));
    }

    @GetMapping("/top-selling")
    public ResponseEntity<List<Product>> getTopSellingProducts(@RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(productService.getTopSellingProducts(limit));
    }

    @GetMapping("/by-supplier/{supplierId}")
    public ResponseEntity<List<Product>> getProductsBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(productService.getProductsBySupplier(supplierId));
    }

    @PatchMapping("/{id}/rename")
    public ResponseEntity<Product> renameProduct(@PathVariable Long id, @RequestParam String newName) {
        return ResponseEntity.ok(productService.renameProduct(id, newName));
    }

    @GetMapping("/expired")
    public ResponseEntity<List<Product>> getExpiredProducts() {
        return ResponseEntity.ok(productService.getExpiredProducts());
    }

    @GetMapping("/expiring-soon")
    public ResponseEntity<List<Product>> getExpiringSoonProducts(@RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(productService.getExpiringSoonProducts(days));
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(@RequestParam double min, @RequestParam double max) {
        return ResponseEntity.ok(productService.getProductsByPriceRange(min, max));
    }

    @GetMapping("/by-name")
    public ResponseEntity<List<Product>> getProductsByName(@RequestParam String name) {
        return ResponseEntity.ok(productService.getProductsByName(name));
    }

    @GetMapping("/by-barcode/{barcode}")
    public ResponseEntity<Product> getProductByBarcode(@PathVariable String barcode) {
        return ResponseEntity.ok(productService.getProductByBarcode(barcode));
    }
}
