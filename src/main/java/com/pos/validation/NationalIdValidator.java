package com.phoenix.pos.validation;

import java.util.regex.Pattern;

/**
 * Validates Iranian national ID numbers based on format, repetition rules, and checksum algorithm.
 */
public class NationalIdValidator implements Validator {

    private static final Pattern ID_PATTERN = Pattern.compile("^\\d{10}$");
    private static final String[] DISALLOWED_SEQUENCES = {
        "0000000000", "1111111111", "2222222222", "3333333333",
        "4444444444", "5555555555", "6666666666", "7777777777",
        "8888888888", "9999999999", "0123456789" , "9876543210"
    };

    private String reason = "Validation has not yet been performed.";

    @Override
    public boolean isValid(String input) {
        if (input == null || input.isBlank()) {
            reason = "Input cannot be null or blank.";
            return false;
        }

        if (!ID_PATTERN.matcher(input).matches()) {
            reason = "Input must be exactly 10 numeric digits.";
            return false;
        }

        if (isDisallowed(input)) {
            reason = "Input matches a disallowed repetitive or sequential pattern.";
            return false;
        }

        if (!isChecksumValid(input)) {
            reason = "Checksum verification failed.";
            return false;
        }

        reason = "National ID is valid.";
        return true;
    }

    private boolean isDisallowed(String id) {
        for (String pattern : DISALLOWED_SEQUENCES) {
            if (pattern.equals(id)) {
                return true;
            }
        }
        return false;
    }

    private boolean isChecksumValid(String id) {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Character.digit(id.charAt(i), 10) * (10 - i);
        }
        int remainder = sum % 11;
        int checkDigit = Character.digit(id.charAt(9), 10);
        return (remainder < 2 && checkDigit == remainder) || (remainder >= 2 && checkDigit == 11 - remainder);
    }

    @Override
    public String getReason() {
        return reason;
    }
}
