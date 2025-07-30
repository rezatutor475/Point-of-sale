package com.example.posqr.repository;

import com.example.posqr.model.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    Page<Sale> findAll(Pageable pageable);

    @Query("SELECT SUM(s.totalAmount) FROM Sale s WHERE s.createdAt BETWEEN :start AND :end")
    BigDecimal calculateTotalSalesBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.createdAt BETWEEN :start AND :end")
    long countSalesBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT AVG(s.totalAmount) FROM Sale s")
    BigDecimal findAverageSaleAmount();

    @Query("SELECT s FROM Sale s WHERE s.paymentMethod = :method")
    List<Sale> findByPaymentMethod(@Param("method") String method);

    @Query("SELECT s FROM Sale s ORDER BY s.createdAt DESC")
    List<Sale> findRecentSales(Pageable pageable);

    @Query("SELECT s.employeeName, COUNT(s) FROM Sale s GROUP BY s.employeeName")
    List<Object[]> countSalesByEmployee();

    @Modifying
    @Transactional
    @Query("DELETE FROM Sale s WHERE s.createdAt < :cutoff")
    void deleteSalesBefore(@Param("cutoff") LocalDateTime cutoff);

    @Query("SELECT FUNCTION('DATE', s.createdAt) AS saleDate, SUM(s.totalAmount) FROM Sale s GROUP BY FUNCTION('DATE', s.createdAt) ORDER BY saleDate DESC")
    List<Object[]> dailySalesSummary();

    @Query("SELECT s.customerId, COUNT(s) FROM Sale s GROUP BY s.customerId")
    List<Object[]> countSalesByCustomer();

    @Query("SELECT s FROM Sale s WHERE s.refunded = true")
    List<Sale> findRefundedSales();

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.refunded = true")
    long countRefundedSales();

    @Query("SELECT s FROM Sale s WHERE s.discountAmount > 0")
    List<Sale> findSalesWithDiscounts();

    @Query("SELECT s FROM Sale s WHERE s.totalAmount > :amount")
    List<Sale> findHighValueSales(@Param("amount") BigDecimal amount);

    @Query("SELECT s FROM Sale s WHERE s.createdAt >= :since")
    List<Sale> findSalesSince(@Param("since") LocalDateTime since);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.paymentMethod = :method")
    long countSalesByPaymentMethod(@Param("method") String method);

    @Query("SELECT FUNCTION('MONTH', s.createdAt), SUM(s.totalAmount) FROM Sale s GROUP BY FUNCTION('MONTH', s.createdAt) ORDER BY FUNCTION('MONTH', s.createdAt)")
    List<Object[]> monthlySalesSummary();

    @Query("SELECT s FROM Sale s WHERE s.customerId = :customerId ORDER BY s.createdAt DESC")
    List<Sale> findRecentSalesByCustomer(@Param("customerId") Long customerId);

    @Query("SELECT MAX(s.totalAmount) FROM Sale s")
    BigDecimal findMaxSaleAmount();

    @Query("SELECT MIN(s.totalAmount) FROM Sale s")
    BigDecimal findMinSaleAmount();

    @Query("SELECT s FROM Sale s WHERE s.createdAt BETWEEN :start AND :end AND s.refunded = false")
    List<Sale> findCompletedSalesBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(DISTINCT s.customerId) FROM Sale s WHERE s.createdAt >= :since")
    long countUniqueCustomersSince(@Param("since") LocalDateTime since);

    @Query("SELECT s FROM Sale s WHERE s.totalAmount BETWEEN :minAmount AND :maxAmount")
    List<Sale> findSalesByAmountRange(@Param("minAmount") BigDecimal minAmount, @Param("maxAmount") BigDecimal maxAmount);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.createdAt >= :startDate")
    long countRecentSales(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.discountAmount > 0")
    long countSalesWithDiscount();

    @Query("SELECT s.paymentMethod, SUM(s.totalAmount) FROM Sale s GROUP BY s.paymentMethod")
    List<Object[]> totalSalesGroupedByPaymentMethod();

    @Query("SELECT FUNCTION('YEAR', s.createdAt), FUNCTION('MONTH', s.createdAt), SUM(s.totalAmount) FROM Sale s GROUP BY FUNCTION('YEAR', s.createdAt), FUNCTION('MONTH', s.createdAt) ORDER BY FUNCTION('YEAR', s.createdAt), FUNCTION('MONTH', s.createdAt)")
    List<Object[]> yearlyMonthlySalesBreakdown();

    @Query("SELECT s FROM Sale s WHERE s.customerId IS NULL")
    List<Sale> findAnonymousSales();

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.createdAt >= :start AND s.createdAt <= :end AND s.refunded = false")
    long countCompletedSalesBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT s.employeeName, SUM(s.totalAmount) FROM Sale s GROUP BY s.employeeName")
    List<Object[]> totalSalesByEmployee();

    @Query("SELECT s FROM Sale s WHERE s.notes IS NOT NULL AND s.notes <> ''")
    List<Sale> findSalesWithNotes();

    @Query("SELECT s FROM Sale s WHERE s.totalAmount = 0")
    List<Sale> findZeroAmountSales();

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.totalAmount = 0")
    long countZeroAmountSales();

    @Query("SELECT s FROM Sale s WHERE s.discountAmount > :threshold")
    List<Sale> findSalesWithHighDiscount(@Param("threshold") BigDecimal threshold);

    @Query("SELECT COUNT(DISTINCT FUNCTION('DATE', s.createdAt)) FROM Sale s")
    long countDistinctSaleDays();

    @Query("SELECT s FROM Sale s WHERE s.employeeName = :employeeName ORDER BY s.createdAt DESC")
    List<Sale> findSalesByEmployee(@Param("employeeName") String employeeName);

    @Query("SELECT s.productCategory, COUNT(s) FROM Sale s GROUP BY s.productCategory")
    List<Object[]> countSalesByProductCategory();

    @Query("SELECT s.customerId, SUM(s.totalAmount) FROM Sale s GROUP BY s.customerId ORDER BY SUM(s.totalAmount) DESC")
    List<Object[]> totalSalesPerCustomer();

    @Query("SELECT s FROM Sale s WHERE FUNCTION('DAYOFWEEK', s.createdAt) = :dayOfWeek")
    List<Sale> findSalesByDayOfWeek(@Param("dayOfWeek") int dayOfWeek);

    @Query("SELECT s FROM Sale s WHERE s.createdAt BETWEEN :startDate AND :endDate ORDER BY s.createdAt ASC")
    List<Sale> findChronologicalSalesBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT FUNCTION('DATE', s.createdAt), COUNT(s) FROM Sale s GROUP BY FUNCTION('DATE', s.createdAt)")
    List<Object[]> countSalesPerDay();

    @Query("SELECT s.customerName, COUNT(s) FROM Sale s GROUP BY s.customerName")
    List<Object[]> countSalesByCustomerName();

    @Query("SELECT s.paymentMethod, COUNT(s) FROM Sale s GROUP BY s.paymentMethod")
    List<Object[]> countSalesByPaymentMethod();

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.createdAt BETWEEN :startDate AND :endDate")
    long countSalesInRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
} 
