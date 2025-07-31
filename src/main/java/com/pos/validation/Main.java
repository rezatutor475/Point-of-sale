package com.phoenix.pos.validation;

import java.util.Scanner;

/**
 * Entry point for validating card number, IBAN (Iran), and national ID inputs.
 * This main class runs predefined and user-input test cases.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== POS Validation Utility ===\n");
        runTests();
    }

    /**
     * Runs both predefined and interactive test cases.
     */
    private static void runTests() {
        executePredefinedTests();
        executeInvalidTests();
        executeInteractiveTest();
    }

    /**
     * Executes predefined valid examples for validation.
     */
    private static void executePredefinedTests() {
        System.out.println("[INFO] Running predefined valid test cases...\n");
        performValidation("6037991234567890", "IR062960000000100324200001", "1234567890");
        performValidation("6037998888888888", "IR820540102680020817909002", "0084571369");
    }

    /**
     * Executes predefined invalid test cases to ensure robustness.
     */
    private static void executeInvalidTests() {
        System.out.println("[INFO] Running predefined invalid test cases...\n");
        performValidation("1234567890123456", "IR000000000000000000000000", "0000000000");
        performValidation("603799", "IR", "1111111111");
        performValidation("abcdefg", "XYZ123", "222222232");
    }

    /**
     * Prompts the user for manual input and validates the input data.
     */
    private static void executeInteractiveTest() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Would you like to enter custom input? (yes/no): ");
            String choice = scanner.nextLine().trim();

            if ("yes".equalsIgnoreCase(choice)) {
                System.out.print("Enter Card Number: ");
                String cardNumber = scanner.nextLine();

                System.out.print("Enter IBAN (IR format): ");
                String iban = scanner.nextLine();

                System.out.print("Enter National ID: ");
                String nationalId = scanner.nextLine();

                System.out.println("\n[INFO] Validating user-provided input...\n");
                performValidation(cardNumber, iban, nationalId);
            }
        }
    }

    /**
     * Validates a card number, IBAN, and national ID using appropriate validators.
     *
     * @param cardNumber Card number to validate
     * @param iban       Iranian IBAN to validate
     * @param nationalId National ID to validate
     */
    private static void performValidation(String cardNumber, String iban, String nationalId) {
        validate("Card Number", cardNumber, new CardNumberValidator());
        validate("IBAN", iban, new IbanValidator());
        validate("National ID", nationalId, new NationalIdValidator());
        System.out.println("------------------------------------------------------------\n");
    }

    /**
     * Generic validation output handler for a value and its validator.
     *
     * @param label     Field label
     * @param value     Input value
     * @param validator Corresponding validator
     */
    private static void validate(String label, String value, Validator validator) {
        boolean isValid = validator.isValid(value);
        System.out.println(label + ": " + value);
        System.out.println("  Valid: " + isValid);
        System.out.println("  Reason: " + validator.getReason());
    }
}
