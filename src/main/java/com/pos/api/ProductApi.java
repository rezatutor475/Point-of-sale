package com.pos.api;

import com.pos.model.Product;
import com.pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling product-related operations in the POS system.
 */
@RestController
@RequestMapping("/api/products")
public class ProductApi {

    private final ProductService productService;

    @Autowired
    public ProductApi(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> products = productService.searchProducts(keyword);
        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
    }

    @GetMapping("/category")
    public ResponseEntity<List<Product>> getProductsByCategory(@RequestParam String category) {
        List<Product> products = productService.getProductsByCategory(category);
        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStockProducts(@RequestParam(defaultValue = "5") int threshold) {
        List<Product> products = productService.getLowStockProducts(threshold);
        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
    }

    @GetMapping("/top-selling")
    public ResponseEntity<List<Product>> getTopSellingProducts(@RequestParam(defaultValue = "10") int count) {
        List<Product> products = productService.getTopSellingProducts(count);
        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Product>> bulkCreateProducts(@RequestBody List<Product> products) {
        List<Product> createdProducts = productService.bulkCreateProducts(products);
        return createdProducts.isEmpty() ? ResponseEntity.badRequest().build() : ResponseEntity.status(HttpStatus.CREATED).body(createdProducts);
    }

    @GetMapping("/by-supplier")
    public ResponseEntity<List<Product>> getProductsBySupplier(@RequestParam String supplier) {
        List<Product> products = productService.getProductsBySupplier(supplier);
        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<List<Product>> getOutOfStockProducts() {
        List<Product> products = productService.getOutOfStockProducts();
        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(@RequestParam double min, @RequestParam double max) {
        List<Product> products = productService.getProductsByPriceRange(min, max);
        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
    }

    @GetMapping("/summary")
    public ResponseEntity<Object> getProductSummary() {
        return ResponseEntity.ok(productService.getProductSummary());
    }

    @GetMapping("/expired")
    public ResponseEntity<List<Product>> getExpiredProducts() {
        List<Product> products = productService.getExpiredProducts();
        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
    }

    @GetMapping("/barcode")
    public ResponseEntity<Product> getProductByBarcode(@RequestParam String barcode) {
        return productService.getProductByBarcode(barcode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Product>> getRecentlyAddedProducts(@RequestParam(defaultValue = "7") int days) {
        List<Product> products = productService.getRecentlyAddedProducts(days);
        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
    }

    @PutMapping("/adjust-stock/{id}")
    public ResponseEntity<Product> adjustStock(@PathVariable Long id, @RequestParam int amount) {
        return productService.adjustStock(id, amount)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<Product>> getInactiveProducts() {
        List<Product> products = productService.getInactiveProducts();
        return products.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(products);
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<Product> activateProduct(@PathVariable Long id) {
        return productService.activateProduct(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<Product> deactivateProduct(@PathVariable Long id) {
        return productService.deactivateProduct(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
