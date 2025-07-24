package com.pos.payment;

import com.pos.payment.gateway.SadadService;
import com.pos.payment.model.PaymentRequest;
import com.pos.payment.model.PaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@DisplayName("SadadService Unit Tests")
class SadadServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SadadService sadadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Initiate Payment: Should return valid response on success")
    void initiatePayment_success_shouldReturnValidResponse() {
        PaymentRequest request = new PaymentRequest("order123", 10000L);
        PaymentResponse expected = new PaymentResponse("OK", "token123", null);

        when(restTemplate.postForObject(any(String.class), any(), eq(PaymentResponse.class)))
                .thenReturn(expected);

        PaymentResponse actual = sadadService.initiatePayment(request);

        assertNotNull(actual);
        assertEquals("OK", actual.getStatus());
        assertEquals("token123", actual.getToken());
    }

    @Test
    @DisplayName("Initiate Payment: Should return null on failure")
    void initiatePayment_failure_shouldReturnNull() {
        PaymentRequest request = new PaymentRequest("order123", 10000L);

        when(restTemplate.postForObject(any(String.class), any(), eq(PaymentResponse.class)))
                .thenReturn(null);

        PaymentResponse actual = sadadService.initiatePayment(request);

        assertNull(actual);
    }

    @Test
    @DisplayName("Verify Payment: Should return valid response on success")
    void verifyPayment_success_shouldReturnValidResponse() {
        String token = "token123";
        PaymentResponse expected = new PaymentResponse("SUCCESS", token, null);

        when(restTemplate.postForObject(any(String.class), any(), eq(PaymentResponse.class)))
                .thenReturn(expected);

        PaymentResponse actual = sadadService.verifyPayment(token);

        assertNotNull(actual);
        assertEquals("SUCCESS", actual.getStatus());
        assertEquals(token, actual.getToken());
    }

    @Test
    @DisplayName("Verify Payment: Should return null on failure")
    void verifyPayment_failure_shouldReturnNull() {
        String token = "invalidToken";

        when(restTemplate.postForObject(any(String.class), any(), eq(PaymentResponse.class)))
                .thenReturn(null);

        PaymentResponse actual = sadadService.verifyPayment(token);

        assertNull(actual);
    }

    @Test
    @DisplayName("Initiate Payment: Should handle exceptions gracefully")
    void initiatePayment_exception_shouldReturnNull() {
        PaymentRequest request = new PaymentRequest("order500", 5000L);

        when(restTemplate.postForObject(any(String.class), any(), eq(PaymentResponse.class)))
                .thenThrow(new RuntimeException("Timeout"));

        assertDoesNotThrow(() -> {
            PaymentResponse response = sadadService.initiatePayment(request);
            assertNull(response);
        });
    }

    @Test
    @DisplayName("Verify Payment: Should handle exceptions gracefully")
    void verifyPayment_exception_shouldReturnNull() {
        String token = "exceptionToken";

        when(restTemplate.postForObject(any(String.class), any(), eq(PaymentResponse.class)))
                .thenThrow(new RuntimeException("Service down"));

        assertDoesNotThrow(() -> {
            PaymentResponse response = sadadService.verifyPayment(token);
            assertNull(response);
        });
    }
}
