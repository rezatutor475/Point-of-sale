package com.pos.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Application-wide configuration and constants.
 * Centralized class for environment, formatting, and printing settings.
 */
public class AppConfig {

    // === Database ===
    public static final String DB_URL = "jdbc:h2:mem:posdb";
    public static final String DB_USERNAME = "sa";
    public static final String DB_PASSWORD = "";

    // === Environment ===
    public static final boolean DEBUG_MODE = true;
    public static final String ENVIRONMENT = "development";
    public static final Locale APP_LOCALE = Locale.US;
    public static final String TIMEZONE = "UTC";

    // === Receipt Settings ===
    public static final int RECEIPT_WIDTH = 40;
    public static final boolean PRINT_RECEIPT_ON_PAYMENT = true;
    public static final String RECEIPT_FOOTER = "Thank you for your purchase!";
    public static final boolean SHOW_ITEMIZED_DISCOUNT = true;
    public static final boolean SHOW_TAX_BREAKDOWN = true;

    // === Currency ===
    public static final String CURRENCY_SYMBOL = "$";
    public static final String CURRENCY_CODE = "USD";
    public static final int DECIMAL_PLACES = 2;
    public static final String NUMBER_FORMAT_PATTERN = "#,##0.00";

    // === Company Details ===
    public static final String COMPANY_NAME = "Phoenix Retail Solutions";
    public static final String COMPANY_ADDRESS = "123 Business Lane, Tech City, TX";
    public static final String SUPPORT_CONTACT = "+1-800-555-0199";
    public static final String WEBSITE_URL = "www.phoenixretail.com";

    // === Tax and Charges ===
    public static final double DEFAULT_TAX_RATE = 0.08;
    public static final double SERVICE_CHARGE_RATE = 0.05;

    // === HTTP ===
    public static final int HTTP_TIMEOUT_MS = 5000;

    // === Logging ===
    public static final String LOGGING_LEVEL = "INFO";
    public static final boolean ENABLE_FILE_LOGGING = true;
    public static final String LOG_FILE_PATH = "logs/pos-application.log";

    // === Developer Tools ===
    public static final boolean ENABLE_DEMO_DATA = true;
    public static final boolean ENABLE_REMOTE_DEBUGGING = false;

    private AppConfig() {
        // Prevent instantiation
    }

    /**
     * Returns current version of the POS system.
     */
    public static String getAppVersion() {
        return "POS System v1.1.0";
    }

    /**
     * Returns ISO 8601 formatted current timestamp.
     */
    public static String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Builds a standard receipt header string.
     */
    public static String getReceiptHeader() {
        return String.format("%s\n%s\nSupport: %s\nWebsite: %s\nDate: %s\n",
                COMPANY_NAME,
                COMPANY_ADDRESS,
                SUPPORT_CONTACT,
                WEBSITE_URL,
                getCurrentTimestamp());
    }

    /**
     * Returns the receipt footer string.
     */
    public static String getReceiptFooter() {
        return RECEIPT_FOOTER;
    }

    /**
     * Returns label for current runtime environment.
     */
    public static String getEnvironmentLabel() {
        return DEBUG_MODE ? "[DEV MODE]" : "[PRODUCTION MODE]";
    }

    /**
     * Determines whether the application is in production mode.
     */
    public static boolean isProduction() {
        return !DEBUG_MODE && "production".equalsIgnoreCase(ENVIRONMENT);
    }

    /**
     * Constructs branding string with version info.
     */
    public static String getBrandingLine() {
        return COMPANY_NAME + " - " + getAppVersion();
    }
}
