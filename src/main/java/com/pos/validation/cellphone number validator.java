package com.phoenix.pos.validation;

import java.util.regex.Pattern;

/**
 * Validates Iranian cellphone numbers in strict formats.
 * Valid prefixes: +989 or 00989, followed by exactly nine digits.
 */
public class CellphoneNumberValidator implements Validator {

    private static final Pattern CELL_PATTERN = Pattern.compile("^(\\+989|00989)\\d{9}$");

    private String reason = "Validation not yet performed.";

    @Override
    public boolean isValid(String input) {
        if (input == null || input.trim().isEmpty()) {
            reason = "Cellphone number cannot be null or empty.";
            return false;
        }

        if (!CELL_PATTERN.matcher(input).matches()) {
            reason = "Cellphone number must start with '+989' or '00989' and be followed by exactly 9 digits.";
            return false;
        }

        reason = "Valid Iranian cellphone number.";
        return true;
    }

    @Override
    public String getReason() {
        return reason;
    }
}
