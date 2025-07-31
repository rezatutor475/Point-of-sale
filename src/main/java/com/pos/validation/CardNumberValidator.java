package com.phoenix.pos.validation;

import java.util.regex.Pattern;

/**
 * Validates Iranian card numbers with the following rules:
 * - Must be 16 digits long
 * - Must start with the prefix 603799
 * - Must pass the Luhn checksum algorithm
 */
public class CardNumberValidator implements Validator {

    private static final Pattern CARD_PATTERN = Pattern.compile("^603799\\d{10}$");
    private String reason = "Validation has not yet been performed.";

    /**
     * Checks if the card number is valid.
     *
     * @param input the card number as a string
     * @return true if the card number is valid; false otherwise
     */
    @Override
    public boolean isValid(String input) {
        if (input == null || input.trim().isEmpty()) {
            reason = "Input is null or empty.";
            return false;
        }

        if (!CARD_PATTERN.matcher(input).matches()) {
            reason = "Card number must start with 603799 and be 16 digits long.";
            return false;
        }

        if (!isLuhnValid(input)) {
            reason = "Card number does not pass Luhn checksum validation.";
            return false;
        }

        reason = "Card number is valid.";
        return true;
    }

    /**
     * Performs the Luhn checksum algorithm to verify the card number.
     *
     * @param cardNumber the full card number as a string
     * @return true if the number passes the Luhn check; false otherwise
     */
    private boolean isLuhnValid(String cardNumber) {
        int sum = 0;
        boolean alternate = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(cardNumber.charAt(i));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        return sum % 10 == 0;
    }

    /**
     * Provides the reason for validation failure or success.
     *
     * @return explanation message for validation result
     */
    @Override
    public String getReason() {
        return reason;
    }
} 
