package com.qaframework.bdd.steps.ui;

import com.qaframework.bdd.context.UIContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckoutSteps {

    private final UIContext ctx;

    public CheckoutSteps(UIContext ctx) {
        this.ctx = ctx;
    }

    @Given("I have added {string} to the cart")
    public void iHaveAddedToCart(String productName) {
        ctx.inventoryPage.addToCart(productName);
    }

    @Given("I have proceeded to checkout")
    public void iHaveProceededToCheckout() {
        ctx.cartPage = ctx.inventoryPage.openCart();
        ctx.checkoutPage = ctx.cartPage.checkout();
    }

    @When("I fill in checkout details with first name {string}, last name {string}, postal code {string}")
    public void iFillInCheckoutDetails(String firstName, String lastName, String postalCode) {
        ctx.checkoutPage.fillInfo(firstName, lastName, postalCode);
    }

    @And("I continue and finish the order")
    public void iContinueAndFinishOrder() {
        ctx.checkoutPage.clickContinue().finish();
    }

    @And("I click continue")
    public void iClickContinue() {
        ctx.checkoutPage.clickContinue();
    }

    @Then("I should see the order confirmation")
    public void iShouldSeeOrderConfirmation() {
        assertThat(ctx.checkoutPage.isOrderComplete()).as("Order confirmation should be visible after completing checkout").isTrue();
    }

    @Then("I should see a checkout error containing {string}")
    public void iShouldSeeCheckoutErrorContaining(String text) {
        assertThat(ctx.checkoutPage.errorMessage()).as("Expected checkout error containing '%s'", text).containsIgnoringCase(text);
    }
}
