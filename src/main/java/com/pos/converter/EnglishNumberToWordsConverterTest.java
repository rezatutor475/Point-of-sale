package test.converter;

import converter.NumberToWordsConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for English number-to-words conversion.
 */
public class EnglishNumberToWordsConverterTest {

    private final NumberToWordsConverter converter =
            NumberToWordsConverter.getInstance(Locale.ENGLISH);

    @Test
    @DisplayName("Should convert whole numbers correctly")
    void testWholeNumberConversion() {
        assertEquals("zero", converter.convert(BigDecimal.ZERO));
        assertEquals("one", converter.convert(BigDecimal.ONE));
        assertEquals("twenty-one", converter.convert(new BigDecimal("21")));
        assertEquals("one hundred", converter.convert(new BigDecimal("100")));
        assertEquals("one thousand two hundred thirty-four", converter.convert(new BigDecimal("1234")));
    }

    @Test
    @DisplayName("Should convert decimal numbers correctly")
    void testDecimalNumberConversion() {
        assertEquals("one point five", converter.convert(new BigDecimal("1.5")));
        assertEquals("two hundred point twenty-five", converter.convert(new BigDecimal("200.25")));
    }

    @Test
    @DisplayName("Should handle large numbers")
    void testLargeNumberConversion() {
        assertEquals(
                "one million", 
                converter.convert(new BigDecimal("1000000"))
        );
        assertEquals(
                "one billion two hundred thirty-four million five hundred sixty-seven thousand eight hundred ninety",
                converter.convert(new BigDecimal("1234567890"))
        );
    }

    @Test
    @DisplayName("Should handle negative numbers")
    void testNegativeNumbers() {
        assertEquals("minus one", converter.convert(new BigDecimal("-1")));
        assertEquals("minus two point five", converter.convert(new BigDecimal("-2.5")));
    }

    @Test
    @DisplayName("Should return null for null input")
    void testNullInput() {
        assertNull(converter.convert(null));
    }
}
