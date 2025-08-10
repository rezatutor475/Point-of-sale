package service;

import model.Amount;
import model.CurrencyInfo;
import model.LocaleType;
import converter.NumberToWordsConverter;
import converter.CurrencyFormatter;
import exception.InvalidAmountException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Service responsible for generating and customizing receipts.
 * Enhanced with additional utilities for multi-currency, discounts, taxes,
 * customizable headers/footers, and optional notes.
 */
public class ReceiptService {

    private final NumberToWordsConverter numberToWordsConverter;
    private final CurrencyFormatter currencyFormatter;

    public ReceiptService(NumberToWordsConverter numberToWordsConverter,
                          CurrencyFormatter currencyFormatter) {
        this.numberToWordsConverter = Objects.requireNonNull(numberToWordsConverter, "NumberToWordsConverter cannot be null");
        this.currencyFormatter = Objects.requireNonNull(currencyFormatter, "CurrencyFormatter cannot be null");
    }

    public String generateReceipt(Amount amount, CurrencyInfo currencyInfo, LocaleType localeType) {
        return generateReceiptWithNote(amount, currencyInfo, localeType, null);
    }

    public String generateReceiptWithNote(Amount amount, CurrencyInfo currencyInfo, LocaleType localeType, String note) {
        validateAmount(amount);

        StringBuilder receipt = new StringBuilder();
        receipt.append(buildHeader("OFFICIAL RECEIPT"));
        receipt.append("Date: ").append(currentDateTime()).append("\n");
        receipt.append("Locale: ").append(localeType.name()).append("\n");
        receipt.append("Currency: ").append(currencyInfo.currencyCode()).append(" (" + currencyInfo.currencySymbol() + ")\n");
        receipt.append("------------------------------\n");
        receipt.append("Amount: ").append(currencyFormatter.format(amount.value(), currencyInfo, localeType)).append("\n");
        receipt.append("In Words: ").append(numberToWordsConverter.convert(amount.value(), localeType)).append("\n");
        if (note != null && !note.isBlank()) {
            receipt.append("Note: ").append(note).append("\n");
        }
        receipt.append(buildFooter("THANK YOU FOR YOUR BUSINESS"));
        return receipt.toString();
    }

    public String generateReceiptWithTaxAndDiscount(Amount amount, CurrencyInfo currencyInfo, LocaleType localeType, BigDecimal discountPercent, BigDecimal taxPercent) {
        return generateReceiptWithTaxDiscountAndNote(amount, currencyInfo, localeType, discountPercent, taxPercent, null);
    }

    public String generateReceiptWithTaxDiscountAndNote(Amount amount, CurrencyInfo currencyInfo, LocaleType localeType, BigDecimal discountPercent, BigDecimal taxPercent, String note) {
        validateAmount(amount);
        BigDecimal discountedAmount = applyDiscount(amount.value(), discountPercent);
        BigDecimal totalWithTax = applyTax(discountedAmount, taxPercent);

        StringBuilder receipt = new StringBuilder();
        receipt.append(buildHeader("OFFICIAL RECEIPT"));
        receipt.append("Date: ").append(currentDateTime()).append("\n");
        receipt.append("Original Amount: ").append(currencyFormatter.format(amount.value(), currencyInfo, localeType)).append("\n");
        receipt.append("Discount (" + discountPercent + "%): -").append(currencyFormatter.format(amount.value().subtract(discountedAmount), currencyInfo, localeType)).append("\n");
        receipt.append("Tax (" + taxPercent + "%): ").append(currencyFormatter.format(totalWithTax.subtract(discountedAmount), currencyInfo, localeType)).append("\n");
        receipt.append("Total: ").append(currencyFormatter.format(totalWithTax, currencyInfo, localeType)).append("\n");
        receipt.append("In Words: ").append(numberToWordsConverter.convert(totalWithTax, localeType)).append("\n");
        if (note != null && !note.isBlank()) {
            receipt.append("Note: ").append(note).append("\n");
        }
        receipt.append(buildFooter("THANK YOU FOR YOUR BUSINESS"));
        return receipt.toString();
    }

    public String previewReceiptHeader(String title) {
        return buildHeader(title);
    }

    public String previewReceiptFooter(String message) {
        return buildFooter(message);
    }

    private String buildHeader(String title) {
        return "==============================\n" +
               String.format("%s%n", centerText(title, 30)) +
               "==============================\n";
    }

    private String buildFooter(String message) {
        return "==============================\n" +
               String.format("%s%n", centerText(message, 30)) +
               "==============================\n";
    }

    private void validateAmount(Amount amount) {
        if (amount == null || amount.value() == null || amount.value().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero.");
        }
    }

    private String currentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private BigDecimal applyDiscount(BigDecimal value, BigDecimal discountPercent) {
        if (discountPercent == null || discountPercent.compareTo(BigDecimal.ZERO) <= 0) {
            return value;
        }
        return value.subtract(value.multiply(discountPercent).divide(BigDecimal.valueOf(100)));
    }

    private BigDecimal applyTax(BigDecimal value, BigDecimal taxPercent) {
        if (taxPercent == null || taxPercent.compareTo(BigDecimal.ZERO) <= 0) {
            return value;
        }
        return value.add(value.multiply(taxPercent).divide(BigDecimal.valueOf(100)));
    }

    private String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text;
    }
}
