package com.qaframework.api.tests;

import com.qaframework.api.clients.BookingClient;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("restful-booker")
@Feature("Authentication")
public class AuthTest extends BaseApiTest {

    @Test
    @Story("Token issuance")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Valid credentials return a non-empty auth token")
    void validCredentialsReturnToken() {
        String token = new BookingClient(api).authenticate();
        assertThat(token).as("token should not be null").isNotNull();
        assertThat(token).as("token should look like a real token").hasSizeGreaterThan(10);
    }
}
