package com.qaframework.bdd.steps.ui;

import com.qaframework.bdd.context.UIContext;
import com.qaframework.core.ConfigReader;
import com.qaframework.ui.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginSteps {

    private final UIContext ctx;

    public LoginSteps(UIContext ctx) {
        this.ctx = ctx;
    }

    @Given("I am on the Saucedemo login page")
    public void iAmOnTheLoginPage() {
        ctx.loginPage = new LoginPage(ctx.getPage()).open();
    }

    @When("I log in with valid credentials")
    public void iLogInWithValidCredentials() {
        ctx.inventoryPage = ctx.loginPage.loginAs(
                ConfigReader.get("saucedemo.username"),
                ConfigReader.get("saucedemo.password"));
    }

    @When("I log in with username {string} and password {string}")
    public void iLogInWithUsernameAndPassword(String username, String password) {
        ctx.inventoryPage = ctx.loginPage.loginAs(username, password);
    }

    @Then("the inventory page should load")
    public void theInventoryPageShouldLoad() {
        assertThat(ctx.inventoryPage.isLoaded()).as("Inventory page should be visible after login").isTrue();
    }

    @Then("the inventory should show {int} products")
    public void theInventoryShouldShowProducts(int count) {
        assertThat(ctx.inventoryPage.itemCount()).as("Inventory product count mismatch").isEqualTo(count);
    }

    @When("I attempt to log in with username {string} and the standard password")
    public void iAttemptLoginWithUsernameAndStandardPassword(String username) {
        ctx.loginPage.attemptLogin(username, ConfigReader.get("saucedemo.password"));
    }

    @When("I attempt to log in with username {string} and password {string}")
    public void iAttemptLoginWithUsernameAndPassword(String username, String password) {
        ctx.loginPage.attemptLogin(username, password);
    }

    @Then("I should see an error containing {string}")
    public void iShouldSeeErrorContaining(String text) {
        assertThat(ctx.loginPage.errorMessage()).as("Expected error containing '%s'", text).containsIgnoringCase(text);
    }

    @When("I log out")
    public void iLogOut() {
        ctx.loginPage = ctx.inventoryPage.logout();
    }

    @Then("I should be on the login page")
    public void iShouldBeOnTheLoginPage() {
        assertThat(ctx.getPage().url()).as("Expected to be on the login page").contains("saucedemo.com");
        assertThat(ctx.getPage().locator("#login-button").isVisible()).as("Login button should be visible on the login page").isTrue();
    }
}
