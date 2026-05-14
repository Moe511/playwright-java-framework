package com.qaframework.bdd.steps;

import com.qaframework.bdd.context.UIContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonSteps {

    private final UIContext ctx;

    public CommonSteps(UIContext ctx) {
        this.ctx = ctx;
    }

    @Then("the page URL should contain {string}")
    public void thePageUrlShouldContain(String text) {
        assertThat(ctx.getPage().url()).as("Expected URL to contain '%s'", text).contains(text);
    }

    @Then("the page URL should not contain {string}")
    public void thePageUrlShouldNotContain(String text) {
        assertThat(ctx.getPage().url()).as("Expected URL to not contain '%s'", text).doesNotContain(text);
    }

    @Then("the page title should contain {string}")
    public void thePageTitleShouldContain(String text) {
        String title = ctx.getPage().title();
        assertThat(title).as("Expected title to contain '%s'", text).contains(text);
    }

    @Then("the element {string} should be visible")
    public void theElementShouldBeVisible(String selector) {
        assertThat(ctx.getPage().locator(selector).isVisible()).as("Expected element '%s' to be visible", selector).isTrue();
    }

    @Then("the element {string} should not be visible")
    public void theElementShouldNotBeVisible(String selector) {
        assertThat(ctx.getPage().locator(selector).isVisible()).as("Expected element '%s' to not be visible", selector).isFalse();
    }

    @When("I navigate to {string}")
    public void iNavigateTo(String url) {
        ctx.getPage().navigate(url);
    }
}
