package com.qaframework.bdd.steps.ui;

import com.qaframework.bdd.context.UIContext;
import com.qaframework.core.ConfigReader;
import com.qaframework.ui.pages.LoginPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CartSteps {

    private final UIContext ctx;

    public CartSteps(UIContext ctx) {
        this.ctx = ctx;
    }

    @Given("I am logged in as a standard user")
    public void iAmLoggedInAsStandardUser() {
        ctx.inventoryPage = new LoginPage(ctx.getPage()).open().loginAs(
                ConfigReader.get("saucedemo.username"),
                ConfigReader.get("saucedemo.password"));
    }

    @When("I add {string} to the cart")
    public void iAddToCart(String productName) {
        ctx.inventoryPage.addToCart(productName);
    }

    @Then("the cart badge should show {int}")
    public void theCartBadgeShouldShow(int count) {
        assertEquals(count, ctx.inventoryPage.cartCount(),
                "Cart badge count mismatch");
    }

    @When("I add the following items to the cart:")
    public void iAddFollowingItemsToCart(DataTable dataTable) {
        List<String> items = dataTable.asList();
        for (String item : items) {
            ctx.inventoryPage.addToCart(item);
        }
    }

    @And("I open the cart")
    public void iOpenTheCart() {
        ctx.cartPage = ctx.inventoryPage.openCart();
    }

    @Then("the cart should contain {int} items")
    public void theCartShouldContainItems(int count) {
        assertEquals(count, ctx.cartPage.itemCount(),
                "Cart item count mismatch");
    }

    @Then("the cart should include {string}")
    public void theCartShouldInclude(String productName) {
        assertTrue(ctx.cartPage.itemNames().contains(productName),
                "Cart should contain '" + productName + "' but had: " + ctx.cartPage.itemNames());
    }

    @When("I remove {string} from the inventory")
    public void iRemoveFromInventory(String productName) {
        ctx.inventoryPage.removeFromCart(productName);
    }

    @When("I remove {string} from the cart")
    public void iRemoveFromCart(String productName) {
        ctx.cartPage.removeItem(productName);
    }
}
