package com.pos.api;

import com.pos.model.Payment;
import com.pos.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing payment operations in the POS system.
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentApi {

    private final PaymentService paymentService;

    @Autowired
    public PaymentApi(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        var payments = paymentService.getAllPayments();
        return payments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(payments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        var created = paymentService.createPayment(payment);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id, @RequestBody Payment payment) {
        return paymentService.updatePayment(id, payment)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        return paymentService.deletePayment(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Payment>> getPaymentsByCustomerId(@PathVariable Long customerId) {
        var payments = paymentService.getPaymentsByCustomerId(customerId);
        return payments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(payments);
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getTotalPaymentsAmount() {
        return ResponseEntity.ok(paymentService.calculateTotalPaymentsAmount());
    }

    @GetMapping("/range")
    public ResponseEntity<List<Payment>> getPaymentsByDateRange(@RequestParam String fromDate, @RequestParam String toDate) {
        var payments = paymentService.getPaymentsByDateRange(fromDate, toDate);
        return payments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(payments);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Payment>> getRecentPayments(@RequestParam(defaultValue = "10") int count) {
        var payments = paymentService.getRecentPayments(count);
        return payments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(payments);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalPaymentCount() {
        return ResponseEntity.ok(paymentService.getTotalPaymentCount());
    }

    @GetMapping("/average")
    public ResponseEntity<Double> getAveragePaymentAmount() {
        return ResponseEntity.ok(paymentService.getAveragePaymentAmount());
    }

    @GetMapping("/method")
    public ResponseEntity<List<Payment>> getPaymentsByMethod(@RequestParam String method) {
        var payments = paymentService.getPaymentsByMethod(method);
        return payments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(payments);
    }

    @GetMapping("/failed")
    public ResponseEntity<List<Payment>> getFailedPayments() {
        var payments = paymentService.getFailedPayments();
        return payments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(payments);
    }

    @GetMapping("/summary-by-method")
    public ResponseEntity<Object> getPaymentSummaryByMethod() {
        return ResponseEntity.ok(paymentService.getPaymentSummaryByMethod());
    }

    @GetMapping("/status")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@RequestParam String status) {
        var payments = paymentService.getPaymentsByStatus(status);
        return payments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(payments);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Payment>> searchPayments(@RequestParam String keyword) {
        var payments = paymentService.searchPayments(keyword);
        return payments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(payments);
    }

    @GetMapping("/by-amount")
    public ResponseEntity<List<Payment>> getPaymentsByAmountRange(@RequestParam double min, @RequestParam double max) {
        var payments = paymentService.getPaymentsByAmountRange(min, max);
        return payments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(payments);
    }

    @GetMapping("/duplicate")
    public ResponseEntity<List<Payment>> getDuplicatePayments() {
        var payments = paymentService.getDuplicatePayments();
        return payments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(payments);
    }

    @GetMapping("/summary-by-customer")
    public ResponseEntity<Object> getPaymentSummaryByCustomer() {
        return ResponseEntity.ok(paymentService.getPaymentSummaryByCustomer());
    }

    @GetMapping("/by-terminal")
    public ResponseEntity<List<Payment>> getPaymentsByTerminal(@RequestParam String terminalId) {
        var payments = paymentService.getPaymentsByTerminal(terminalId);
        return payments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(payments);
    }

    @GetMapping("/by-bank")
    public ResponseEntity<List<Payment>> getPaymentsByBankName(@RequestParam String bankName) {
        var payments = paymentService.getPaymentsByBankName(bankName);
        return payments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(payments);
    }

    @GetMapping("/reversed")
    public ResponseEntity<List<Payment>> getReversedPayments() {
        var payments = paymentService.getReversedPayments();
        return payments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(payments);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Payment>> bulkCreatePayments(@RequestBody List<Payment> payments) {
        var created = paymentService.bulkCreatePayments(payments);
        return created.isEmpty() ? ResponseEntity.badRequest().build() : ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/by-invoice")
    public ResponseEntity<List<Payment>> getPaymentsByInvoiceId(@RequestParam String invoiceId) {
        var payments = paymentService.getPaymentsByInvoiceId(invoiceId);
        return payments.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(payments);
    }
}
