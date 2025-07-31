package com.phoenix.pos.validation;

import java.math.BigInteger;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Validates Iranian IBANs (International Bank Account Numbers).
 * A valid IBAN in Iran:
 * - Starts with "IR"
 * - Is 26 characters long
 * - Passes the MOD-97 checksum algorithm
 */
public class IbanValidator implements Validator {

    private static final Pattern IRANIAN_IBAN_PATTERN = Pattern.compile("^IR\d{24}$");
    private String reason = "Validation not performed.";

    @Override
    public boolean isValid(String input) {
        if (input == null || input.isBlank()) {
            reason = "IBAN cannot be null or blank.";
            return false;
        }

        if (!IRANIAN_IBAN_PATTERN.matcher(input).matches()) {
            reason = "IBAN must start with 'IR' followed by 24 digits (total 26 characters).";
            return false;
        }

        if (!validateChecksum(input)) {
            reason = "IBAN checksum validation failed.";
            return false;
        }

        reason = "IBAN is valid.";
        return true;
    }

    /**
     * Validates IBAN using the MOD-97 checksum algorithm.
     *
     * @param iban the IBAN string
     * @return true if checksum passes, false otherwise
     */
    private boolean validateChecksum(String iban) {
        // Rearranged IBAN: move the first 4 chars to the end
        String rearranged = iban.substring(4) + iban.substring(0, 4);
        StringBuilder numericIban = new StringBuilder();

        for (char ch : rearranged.toUpperCase(Locale.ROOT).toCharArray()) {
            if (Character.isDigit(ch)) {
                numericIban.append(ch);
            } else if (Character.isLetter(ch)) {
                int numericValue = ch - 'A' + 10;
                numericIban.append(numericValue);
            } else {
                reason = "IBAN contains invalid character: '" + ch + "'.";
                return false;
            }
        }

        try {
            BigInteger ibanNumber = new BigInteger(numericIban.toString());
            return ibanNumber.mod(BigInteger.valueOf(97)).intValue() == 1;
        } catch (NumberFormatException e) {
            reason = "IBAN numeric conversion failed.";
            return false;
        }
    }

    @Override
    public String getReason() {
        return reason;
    }
}
