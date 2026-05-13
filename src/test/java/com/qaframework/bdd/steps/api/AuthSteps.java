package com.qaframework.bdd.steps.api;

import com.qaframework.api.clients.BookingClient;
import com.qaframework.bdd.context.APIContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthSteps {

    private final APIContext ctx;

    public AuthSteps(APIContext ctx) {
        this.ctx = ctx;
    }

    @When("I authenticate with valid API credentials")
    public void iAuthenticateWithValidCredentials() {
        ctx.authToken = new BookingClient(ctx.getApiClient()).authenticate();
    }

    @Then("I should receive a non-empty auth token")
    public void iShouldReceiveNonEmptyToken() {
        assertNotNull(ctx.authToken, "Token should not be null");
        assertTrue(ctx.authToken.length() > 10,
                "Token should look like a real token, got: " + ctx.authToken);
    }
}
