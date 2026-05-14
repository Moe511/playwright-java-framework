package com.qaframework.bdd.steps.api;

import com.qaframework.api.clients.BookingClient;
import com.qaframework.bdd.context.APIContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(ctx.authToken).as("Token should not be null").isNotNull();
        assertThat(ctx.authToken).as("Token should look like a real token").hasSizeGreaterThan(10);
    }
}
