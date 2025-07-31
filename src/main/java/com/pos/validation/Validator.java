package com.phoenix.pos.validation;

/**
 * Interface for all validation components within the POS system.
 * <p>
 * Each implementation is responsible for applying validation rules specific to
 * a particular domain (e.g., national ID, IBAN, card number) and for returning
 * an explanatory message reflecting the outcome of the validation.
 */
public interface Validator {

    /**
     * Applies validation logic to the given input.
     *
     * @param input the input string to validate
     * @return {@code true} if the input passes validation; {@code false} otherwise
     */
    boolean isValid(String input);

    /**
     * Returns the reason for the last validation result.
     * This method is intended to aid in diagnostics or provide user feedback.
     *
     * @return a descriptive message corresponding to the outcome of validation
     */
    String getReason();
}
