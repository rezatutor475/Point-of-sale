package model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Enumeration representing supported locale types in the POS system.
 * Each locale type is mapped to a Java {@link Locale} instance.
 */
public enum LocaleType {

    ENGLISH_US(Locale.US),
    ENGLISH_UK(Locale.UK),
    FRENCH_FR(Locale.FRANCE),
    GERMAN_DE(Locale.GERMANY),
    JAPANESE_JP(Locale.JAPAN),
    ARABIC_SA(new Locale("ar", "SA")),
    PERSIAN_IR(new Locale("fa", "IR"));

    private final Locale locale;

    LocaleType(Locale locale) {
        this.locale = Objects.requireNonNull(locale, "Locale cannot be null");
    }

    /**
     * Gets the {@link Locale} associated with this locale type.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Returns a list of all supported {@link Locale} instances.
     */
    public static List<Locale> getSupportedLocales() {
        return Arrays.stream(values())
                .map(LocaleType::getLocale)
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of all supported language tags (e.g., "en-US", "fa-IR").
     */
    public static List<String> getSupportedLanguageTags() {
        return Arrays.stream(values())
                .map(l -> l.getLocale().toLanguageTag())
                .collect(Collectors.toList());
    }

    /**
     * Finds a {@link LocaleType} by its {@link Locale}.
     */
    public static Optional<LocaleType> fromLocale(Locale locale) {
        if (locale == null) return Optional.empty();
        return Arrays.stream(values())
                .filter(l -> l.getLocale().equals(locale))
                .findFirst();
    }

    /**
     * Finds a {@link LocaleType} by its language tag (IETF BCP 47 format).
     */
    public static Optional<LocaleType> fromLanguageTag(String languageTag) {
        if (languageTag == null || languageTag.isBlank()) return Optional.empty();
        return fromLocale(Locale.forLanguageTag(languageTag));
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name(), locale);
    }
}
