package com.pos.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for sending HTTP requests with reusable methods and enhanced functionality.
 */
public class HttpClientUtil {

    private static final int CONNECT_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 15000;
    private static final Logger LOGGER = Logger.getLogger(HttpClientUtil.class.getName());

    public static Optional<String> post(String targetUrl, String body, Map<String, String> headers) {
        return executeRequest(targetUrl, "POST", body, headers);
    }

    public static Optional<String> get(String targetUrl, Map<String, String> headers) {
        return executeRequest(targetUrl, "GET", null, headers);
    }

    public static Optional<String> put(String targetUrl, String body, Map<String, String> headers) {
        return executeRequest(targetUrl, "PUT", body, headers);
    }

    public static Optional<String> delete(String targetUrl, Map<String, String> headers) {
        return executeRequest(targetUrl, "DELETE", null, headers);
    }

    private static Optional<String> executeRequest(String targetUrl, String method, String body, Map<String, String> headers) {
        try {
            HttpURLConnection connection = setupConnection(targetUrl, method, headers);
            if (body != null && !body.isEmpty()) {
                writeRequestBody(connection, body);
            }
            return Optional.ofNullable(getResponse(connection));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "HTTP request failed: " + method + " " + targetUrl, e);
            return Optional.empty();
        }
    }

    private static void writeRequestBody(HttpURLConnection connection, String body) throws IOException {
        connection.setDoOutput(true);
        try (OutputStream os = connection.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
            os.flush();
        }
    }

    private static HttpURLConnection setupConnection(String targetUrl, String method, Map<String, String> headers) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(targetUrl).openConnection();
        connection.setRequestMethod(method);
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setDoInput(true);
        connection.setDoOutput(!"GET".equalsIgnoreCase(method) && !"HEAD".equalsIgnoreCase(method));

        if (headers != null) {
            headers.forEach(connection::setRequestProperty);
        }

        return connection;
    }

    private static String getResponse(HttpURLConnection connection) throws IOException {
        int status = connection.getResponseCode();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                status >= 200 && status < 300 ? connection.getInputStream() : connection.getErrorStream(),
                StandardCharsets.UTF_8))) {

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append(System.lineSeparator());
            }
            return response.toString().trim();
        } finally {
            connection.disconnect();
        }
    }

    public static boolean isReachable(String targetUrl) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(targetUrl).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            int code = connection.getResponseCode();
            return code < 400;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to reach URL: " + targetUrl, e);
            return false;
        }
    }

    public static Optional<Integer> getStatusCode(String targetUrl, String method, Map<String, String> headers) {
        try {
            HttpURLConnection connection = setupConnection(targetUrl, method, headers);
            try {
                return Optional.of(connection.getResponseCode());
            } finally {
                connection.disconnect();
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to get status code for: " + targetUrl, e);
            return Optional.empty();
        }
    }

    public static Optional<String> getHeaderField(String targetUrl, String method, Map<String, String> headers, String fieldName) {
        try {
            HttpURLConnection connection = setupConnection(targetUrl, method, headers);
            try {
                return Optional.ofNullable(connection.getHeaderField(fieldName));
            } finally {
                connection.disconnect();
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to retrieve header field from: " + targetUrl, e);
            return Optional.empty();
        }
    }

    public static boolean isJsonResponse(String targetUrl, Map<String, String> headers) {
        return getContentType(targetUrl, headers)
                .map(contentType -> contentType.toLowerCase().contains("application/json"))
                .orElse(false);
    }

    public static boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Optional<String> getContentType(String targetUrl, Map<String, String> headers) {
        try {
            HttpURLConnection connection = setupConnection(targetUrl, "GET", headers);
            try {
                return Optional.ofNullable(connection.getContentType());
            } finally {
                connection.disconnect();
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to retrieve content type from: " + targetUrl, e);
            return Optional.empty();
        }
    }

    public static Optional<Long> getContentLength(String targetUrl, Map<String, String> headers) {
        try {
            HttpURLConnection connection = setupConnection(targetUrl, "GET", headers);
            try {
                return Optional.of(connection.getContentLengthLong());
            } finally {
                connection.disconnect();
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to retrieve content length from: " + targetUrl, e);
            return Optional.empty();
        }
    }

    public static boolean isHtmlResponse(String targetUrl, Map<String, String> headers) {
        return getContentType(targetUrl, headers)
                .map(contentType -> contentType.toLowerCase().contains("text/html"))
                .orElse(false);
    }
}
