package app;

import config.CurrencyConfig;
import converter.CurrencyFormatter;
import converter.NumberToWordsConverter;
import model.Amount;
import model.CurrencyInfo;
import model.LocaleType;
import service.ReceiptService;
import util.NumberUtils;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Scanner;

/**
 * Entry point for the POS system simulation.
 * Enhanced with better user prompts, validations, and additional interactive features.
 */
public class App {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            ReceiptService receiptService = new ReceiptService();

            System.out.println("=== POS System Simulation ===");
            System.out.print("Enter customer name: ");
            String customerName = scanner.nextLine().trim();
            if (customerName.isEmpty()) {
                System.out.println("Customer name cannot be empty. Exiting.");
                return;
            }

            System.out.print("Enter amount (numeric): ");
            BigDecimal value = NumberUtils.toBigDecimal(scanner.nextLine());
            if (value == null || !NumberUtils.isPositive(value)) {
                System.out.println("Invalid amount. Exiting.");
                return;
            }

            System.out.println("Select currency: 1) USD  2) EUR");
            int currencyChoice = NumberUtils.safeParseInt(scanner.nextLine(), 1);

            LocaleType localeType = (currencyChoice == 2) ? LocaleType.DE_DE : LocaleType.EN_US;
            Locale locale = (currencyChoice == 2) ? Locale.GERMANY : Locale.US;
            CurrencyInfo currencyInfo = CurrencyConfig.getCurrencyInfo(localeType);

            Amount amount = new Amount(value, currencyInfo);
            String formattedAmount = CurrencyFormatter.format(amount, locale);
            String amountInWords = NumberToWordsConverter.convert(amount.getValue(), localeType);

            System.out.println("Formatted Amount: " + formattedAmount);
            System.out.println("Amount in Words: " + amountInWords);

            String receipt = receiptService.generateReceipt(customerName, amount);
            System.out.println("\n--- Receipt ---\n" + receipt);

            applyDiscount(scanner, amount, currencyInfo, locale);
            System.out.println("\nThank you for using the POS System!");
        }
    }

    private static void applyDiscount(Scanner scanner, Amount amount, CurrencyInfo currencyInfo, Locale locale) {
        System.out.print("Enter discount percentage (optional, press Enter to skip): ");
        String discountInput = scanner.nextLine();
        if (!discountInput.trim().isEmpty()) {
            BigDecimal discountPercent = NumberUtils.toBigDecimal(discountInput);
            if (discountPercent != null && NumberUtils.isNonNegative(discountPercent)) {
                BigDecimal discountValue = NumberUtils.percentOf(amount.getValue(), discountPercent);
                System.out.println("Discount Value: " + CurrencyFormatter.format(new Amount(discountValue, currencyInfo), locale));
                System.out.println("Total After Discount: " + CurrencyFormatter.format(new Amount(amount.getValue().subtract(discountValue), currencyInfo), locale));
            } else {
                System.out.println("Invalid discount percentage.");
            }
        }
    }
}