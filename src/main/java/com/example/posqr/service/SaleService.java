package com.example.posqr.service;

import com.example.posqr.model.Sale;
import com.example.posqr.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SaleService {

    private final SaleRepository saleRepository;

    @Autowired
    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    public Optional<Sale> getSaleById(Long id) {
        return saleRepository.findById(id);
    }

    public Sale saveSale(Sale sale) {
        sale.setCreatedAt(LocalDateTime.now());
        return saleRepository.save(sale);
    }

    public Sale updateSale(Long id, Sale updatedSale) {
        return saleRepository.findById(id).map(sale -> {
            sale.setTotalAmount(updatedSale.getTotalAmount());
            sale.setUpdatedAt(LocalDateTime.now());
            sale.setCustomerName(updatedSale.getCustomerName());
            sale.setPaymentMethod(updatedSale.getPaymentMethod());
            return saleRepository.save(sale);
        }).orElseThrow(() -> new RuntimeException("Sale not found with id: " + id));
    }

    public void deleteSale(Long id) {
        saleRepository.deleteById(id);
    }

    public BigDecimal calculateTotalRevenue() {
        return saleRepository.calculateTotalRevenue();
    }

    public long countTotalSales() {
        return saleRepository.count();
    }

    public List<Sale> findSalesByDateRange(LocalDateTime start, LocalDateTime end) {
        return saleRepository.findSalesByDateRange(start, end);
    }

    public List<Sale> findSalesByCustomerName(String customerName) {
        return saleRepository.findSalesByCustomerNameContainingIgnoreCase(customerName);
    }

    public long countSalesByPaymentMethod(String paymentMethod) {
        return saleRepository.countSalesByPaymentMethod(paymentMethod);
    }

    public BigDecimal averageSaleAmount() {
        return saleRepository.averageSaleAmount();
    }

    public List<Object[]> totalRevenueByDay(LocalDateTime start, LocalDateTime end) {
        return saleRepository.totalRevenueByDay(start, end);
    }

    public List<Object[]> topCustomersByRevenue(int limit) {
        return saleRepository.topCustomersByRevenue(limit);
    }

    public boolean saleExistsByCustomerAndDate(String customerName, LocalDateTime date) {
        return saleRepository.existsByCustomerNameAndCreatedAt(customerName, date);
    }

    public List<Sale> findSalesAboveAmount(BigDecimal threshold) {
        return saleRepository.findSalesAboveAmount(threshold);
    }

    public List<Sale> findSalesWithNoCustomerName() {
        return saleRepository.findSalesWithNoCustomerName();
    }

    public List<Sale> findRecentSales(int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return saleRepository.findSalesByDateRange(since, LocalDateTime.now());
    }

    public BigDecimal calculateRevenueForCustomer(String customerName) {
        return saleRepository.calculateRevenueForCustomer(customerName);
    }

    public List<Sale> findSalesByPaymentMethod(String paymentMethod) {
        return saleRepository.findSalesByPaymentMethod(paymentMethod);
    }

    public List<Sale> findSalesBetweenAmounts(BigDecimal minAmount, BigDecimal maxAmount) {
        return saleRepository.findSalesBetweenAmounts(minAmount, maxAmount);
    }

    public List<Sale> findSalesByDate(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);
        return saleRepository.findSalesByDateRange(start, end);
    }

    public boolean hasCustomerMadeAnyPurchase(String customerName) {
        return saleRepository.countSalesByCustomerName(customerName) > 0;
    }

    public BigDecimal calculateTotalRevenueInLastNDays(int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return saleRepository.calculateRevenueSince(since);
    }

    public List<Sale> findLastNSales(int n) {
        return saleRepository.findLastNSales(n);
    }

    public List<String> findAllUniqueCustomers() {
        return saleRepository.findAllUniqueCustomers();
    }

    // Additional Functions

    public Map<String, Long> countSalesGroupedByPaymentMethod() {
        return saleRepository.findAll().stream()
                .collect(Collectors.groupingBy(Sale::getPaymentMethod, Collectors.counting()));
    }

    public Map<String, BigDecimal> totalRevenueGroupedByCustomer() {
        return saleRepository.findAll().stream()
                .collect(Collectors.groupingBy(Sale::getCustomerName, Collectors.reducing(BigDecimal.ZERO, Sale::getTotalAmount, BigDecimal::add)));
    }

    public List<Sale> findTopSalesByAmount(int limit) {
        return saleRepository.findAll().stream()
                .sorted((s1, s2) -> s2.getTotalAmount().compareTo(s1.getTotalAmount()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public BigDecimal calculateAverageRevenuePerCustomer() {
        Map<String, BigDecimal> revenueByCustomer = totalRevenueGroupedByCustomer();
        if (revenueByCustomer.isEmpty()) return BigDecimal.ZERO;
        return revenueByCustomer.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(revenueByCustomer.size()), BigDecimal.ROUND_HALF_UP);
    }

    public List<Sale> findSalesContainingKeywordInCustomerName(String keyword) {
        return saleRepository.findAll().stream()
                .filter(sale -> sale.getCustomerName() != null && sale.getCustomerName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
} 
