package com.pos.config;

/**
 * Configuration utility class for managing external payment provider settings,
 * such as API credentials, endpoints, and retry logic.
 */
public final class PaymentConfig {

    /** Sadad Payment Gateway Configuration */
    public static final String SADAD_MERCHANT_ID = "SADAD-123456789";
    public static final String SADAD_API_KEY = "your-sadad-api-key";
    public static final String SADAD_API_URL = "https://sadad.shaparak.ir/api/v1/payment";
    public static final String SADAD_CALLBACK_URL = "https://yourapp.com/sadad/callback";

    /** Sep Payment Gateway Configuration */
    public static final String SEP_MERCHANT_ID = "SEP-987654321";
    public static final String SEP_API_KEY = "your-sep-api-key";
    public static final String SEP_API_URL = "https://sep.shaparak.ir/payment/start";
    public static final String SEP_CALLBACK_URL = "https://yourapp.com/sep/callback";

    /** General HTTP Settings */
    public static final int TIMEOUT_TOTAL_MS = 7000;
    public static final int TIMEOUT_CONNECT_MS = 3000;
    public static final int TIMEOUT_SOCKET_MS = 7000;
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String ACCEPT_LANGUAGE = "en-US";

    /** Retry Policy Settings */
    public static final int MAX_RETRY_ATTEMPTS = 3;
    public static final int RETRY_DELAY_MS = 2000;

    /** Logging & Debugging */
    public static final boolean ENABLE_HTTP_LOGGING = true;
    public static final boolean ENABLE_DEBUG_MODE = false;

    /** Enum for identifying supported providers */
    public enum Provider {
        SADAD, SEP;

        public String getMerchantId() {
            return switch (this) {
                case SADAD -> SADAD_MERCHANT_ID;
                case SEP -> SEP_MERCHANT_ID;
            };
        }

        public String getApiKey() {
            return switch (this) {
                case SADAD -> SADAD_API_KEY;
                case SEP -> SEP_API_KEY;
            };
        }

        public String getApiUrl() {
            return switch (this) {
                case SADAD -> SADAD_API_URL;
                case SEP -> SEP_API_URL;
            };
        }

        public String getCallbackUrl() {
            return switch (this) {
                case SADAD -> SADAD_CALLBACK_URL;
                case SEP -> SEP_CALLBACK_URL;
            };
        }

        public boolean isEnabled() {
            return getApiKey() != null && !getApiKey().trim().isEmpty();
        }
    }

    /** Prevent instantiation */
    private PaymentConfig() {}

    /** Utility methods */
    public static boolean shouldRetry(int attempt) {
        return attempt < MAX_RETRY_ATTEMPTS;
    }

    public static String[] getStandardHeaders() {
        return new String[] {
            "Content-Type: " + CONTENT_TYPE_JSON,
            "Accept-Language: " + ACCEPT_LANGUAGE
        };
    }

    public static boolean isDebugMode() {
        return ENABLE_DEBUG_MODE;
    }

    public static boolean isProviderEnabled(String providerName) {
        try {
            return Provider.valueOf(providerName.toUpperCase()).isEnabled();
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static String getApiUrl(String providerName) {
        return Provider.valueOf(providerName.toUpperCase()).getApiUrl();
    }

    public static String getCallbackUrl(String providerName) {
        return Provider.valueOf(providerName.toUpperCase()).getCallbackUrl();
    }

    public static String getMerchantId(String providerName) {
        return Provider.valueOf(providerName.toUpperCase()).getMerchantId();
    }

    public static String getApiKey(String providerName) {
        return Provider.valueOf(providerName.toUpperCase()).getApiKey();
    }
} 
