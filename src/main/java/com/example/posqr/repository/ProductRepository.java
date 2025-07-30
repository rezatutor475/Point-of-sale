package com.example.posqr.repository;

import com.example.posqr.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    boolean existsByName(String name);

    Optional<Product> findByBarcode(String barcode);

    List<Product> findByDiscontinuedFalse();

    Page<Product> findByDiscontinuedFalse(Pageable pageable);

    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchByNameKeyword(@Param("keyword") String keyword);

    @Query("SELECT p FROM Product p WHERE p.name LIKE :prefix%")
    List<Product> findByNameStartingWith(@Param("prefix") String prefix);

    @Query("SELECT p FROM Product p WHERE p.quantity < :threshold")
    List<Product> findLowStockProducts(@Param("threshold") int threshold);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.quantity < :threshold")
    long countLowStockProducts(@Param("threshold") int threshold);

    @Query("SELECT p FROM Product p WHERE p.quantity = 0")
    List<Product> findOutOfStockProducts();

    @Query("SELECT p FROM Product p WHERE p.createdAt BETWEEN :start AND :end")
    List<Product> findAllCreatedBetween(@Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end);

    @Query("SELECT p FROM Product p ORDER BY p.createdAt DESC")
    List<Product> findAllOrderedByCreatedDateDesc();

    @Query("SELECT p FROM Product p WHERE p.updatedAt > :since")
    List<Product> findRecentlyUpdated(@Param("since") LocalDateTime since);

    @Query("SELECT AVG(p.price) FROM Product p")
    BigDecimal findAveragePrice();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.discontinued = true")
    long countDiscontinuedProducts();

    @Query("SELECT DISTINCT p.name FROM Product p")
    List<String> findDistinctProductNames();

    @Query("SELECT p FROM Product p WHERE p.expiryDate IS NOT NULL AND p.expiryDate < CURRENT_DATE")
    List<Product> findExpiredProducts();

    @Query("SELECT p FROM Product p WHERE p.expiryDate IS NOT NULL AND p.expiryDate BETWEEN CURRENT_DATE AND :thresholdDate")
    List<Product> findExpiringSoon(@Param("thresholdDate") LocalDateTime thresholdDate);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.expiryDate IS NOT NULL AND p.expiryDate < CURRENT_DATE")
    long countExpiredProducts();

    @Query("SELECT MAX(p.price) FROM Product p")
    BigDecimal findMaxProductPrice();

    @Query("SELECT MIN(p.price) FROM Product p")
    BigDecimal findMinProductPrice();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.createdAt >= :since")
    long countProductsCreatedSince(@Param("since") LocalDateTime since);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.updatedAt >= :since")
    long countProductsUpdatedSince(@Param("since") LocalDateTime since);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.discontinued = true WHERE p.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.quantity = p.quantity + :amount WHERE p.id = :id")
    void restockProduct(@Param("id") Long id, @Param("amount") int amount);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.price = :price WHERE p.id = :id")
    void updatePrice(@Param("id") Long id, @Param("price") BigDecimal price);

    @Modifying
    @Transactional
    @Query("DELETE FROM Product p WHERE p.expiryDate IS NOT NULL AND p.expiryDate < CURRENT_DATE")
    void deleteExpiredProducts();

    @Query("SELECT p FROM Product p WHERE p.lastSoldAt IS NOT NULL ORDER BY p.lastSoldAt DESC")
    List<Product> findRecentlySoldProducts();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.lastSoldAt IS NOT NULL AND p.lastSoldAt >= :since")
    long countRecentlySoldProducts(@Param("since") LocalDateTime since);

    // New functions
    @Query("SELECT p.category, COUNT(p) FROM Product p GROUP BY p.category")
    List<Object[]> countProductsByCategory();

    @Query("SELECT p.category, AVG(p.price) FROM Product p GROUP BY p.category")
    List<Object[]> averagePriceByCategory();

    @Query("SELECT p.category, SUM(p.quantity) FROM Product p GROUP BY p.category")
    List<Object[]> totalStockByCategory();

    @Query("SELECT p.supplierName, COUNT(p) FROM Product p GROUP BY p.supplierName")
    List<Object[]> countProductsBySupplier();

    @Query("SELECT p FROM Product p WHERE p.restockThreshold IS NOT NULL AND p.quantity <= p.restockThreshold")
    List<Product> findProductsNeedingRestock();
} 
