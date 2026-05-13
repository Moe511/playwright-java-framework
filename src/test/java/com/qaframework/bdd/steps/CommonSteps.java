package com.qaframework.bdd.steps;

import com.qaframework.bdd.context.UIContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonSteps {

    private final UIContext ctx;

    public CommonSteps(UIContext ctx) {
        this.ctx = ctx;
    }

    @Then("the page URL should contain {string}")
    public void thePageUrlShouldContain(String text) {
        assertTrue(ctx.getPage().url().contains(text),
                "Expected URL to contain '" + text + "', but was: " + ctx.getPage().url());
    }

    @Then("the page URL should not contain {string}")
    public void thePageUrlShouldNotContain(String text) {
        assertFalse(ctx.getPage().url().contains(text),
                "Expected URL to not contain '" + text + "', but was: " + ctx.getPage().url());
    }

    @Then("the page title should contain {string}")
    public void thePageTitleShouldContain(String text) {
        String title = ctx.getPage().title();
        assertTrue(title.contains(text),
                "Expected title to contain '" + text + "', but was: " + title);
    }

    @Then("the element {string} should be visible")
    public void theElementShouldBeVisible(String selector) {
        assertTrue(ctx.getPage().locator(selector).isVisible(),
                "Expected element '" + selector + "' to be visible");
    }

    @Then("the element {string} should not be visible")
    public void theElementShouldNotBeVisible(String selector) {
        assertFalse(ctx.getPage().locator(selector).isVisible(),
                "Expected element '" + selector + "' to not be visible");
    }

    @When("I navigate to {string}")
    public void iNavigateTo(String url) {
        ctx.getPage().navigate(url);
    }
}
