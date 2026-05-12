package com.qaframework.api.tests;

import com.qaframework.api.clients.BookingClient;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("restful-booker")
@Feature("Authentication")
public class AuthTest extends BaseApiTest {

    @Test
    @Story("Token issuance")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Valid credentials return a non-empty auth token")
    void validCredentialsReturnToken() {
        String token = new BookingClient(api).authenticate();
        assertNotNull(token, "token should not be null");
        assertTrue(token.length() > 10, "token should look like a real token, got: " + token);
    }
}
