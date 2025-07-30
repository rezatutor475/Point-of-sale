package com.example.posqr.controller;

import com.example.posqr.model.Sale;
import com.example.posqr.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;

    @Autowired
    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody Sale sale) {
        Sale savedSale = saleService.createSale(sale);
        return ResponseEntity.ok(savedSale);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable Long id) {
        Sale sale = saleService.getSaleById(id);
        return sale != null ? ResponseEntity.ok(sale) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Sale>> getAllSales() {
        return ResponseEntity.ok(saleService.getAllSales());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sale> updateSale(@PathVariable Long id, @RequestBody Sale saleDetails) {
        Sale updatedSale = saleService.updateSale(id, saleDetails);
        return updatedSale != null ? ResponseEntity.ok(updatedSale) : ResponseEntity.notFound().build();
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Sale>> getSalesByProductId(@PathVariable Long productId) {
        List<Sale> sales = saleService.getSalesByProductId(productId);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/report/total")
    public ResponseEntity<Double> getTotalSalesAmount() {
        double total = saleService.getTotalSalesAmount();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/report/daily")
    public ResponseEntity<List<Sale>> getTodaySales() {
        return ResponseEntity.ok(saleService.getTodaySales());
    }

    @PostMapping("/refund/{id}")
    public ResponseEntity<String> refundSale(@PathVariable Long id) {
        boolean refunded = saleService.refundSale(id);
        return refunded ? ResponseEntity.ok("Sale refunded successfully.") : ResponseEntity.badRequest().body("Refund failed.");
    }

    @GetMapping("/report/date-range")
    public ResponseEntity<List<Sale>> getSalesBetweenDates(@RequestParam("start") String startDate,
                                                           @RequestParam("end") String endDate) {
        List<Sale> sales = saleService.getSalesBetweenDates(LocalDate.parse(startDate), LocalDate.parse(endDate));
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/report/by-customer")
    public ResponseEntity<Map<String, Object>> getSalesGroupedByCustomer() {
        return ResponseEntity.ok(saleService.getSalesGroupedByCustomer());
    }

    @GetMapping("/report/top-products")
    public ResponseEntity<List<Map<String, Object>>> getTopSellingProducts() {
        return ResponseEntity.ok(saleService.getTopSellingProducts());
    }

    @GetMapping("/report/summary")
    public ResponseEntity<Map<String, Object>> getSalesSummary() {
        return ResponseEntity.ok(saleService.getSalesSummary());
    }

    @GetMapping("/report/hourly")
    public ResponseEntity<Map<Integer, Double>> getHourlySalesSummary() {
        return ResponseEntity.ok(saleService.getHourlySalesSummary());
    }

    @GetMapping("/report/by-payment-method")
    public ResponseEntity<Map<String, Double>> getSalesByPaymentMethod() {
        return ResponseEntity.ok(saleService.getSalesByPaymentMethod());
    }

    @GetMapping("/report/unsold-products")
    public ResponseEntity<List<Long>> getUnsoldProductIds() {
        return ResponseEntity.ok(saleService.getUnsoldProductIds());
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearAllSales() {
        saleService.clearAllSales();
        return ResponseEntity.ok("All sales data cleared.");
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Sale>> createBulkSales(@RequestBody List<Sale> sales) {
        List<Sale> created = saleService.createBulkSales(sales);
        return ResponseEntity.ok(created);
    }
} 
