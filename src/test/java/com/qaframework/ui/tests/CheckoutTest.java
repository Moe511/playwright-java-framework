package com.qaframework.ui.tests;

import com.qaframework.core.BaseTest;
import com.qaframework.core.ConfigReader;
import com.qaframework.ui.pages.CartPage;
import com.qaframework.ui.pages.CheckoutPage;
import com.qaframework.ui.pages.InventoryPage;
import com.qaframework.ui.pages.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Saucedemo")
@Feature("Checkout")
public class CheckoutTest extends BaseTest {

    @Test
    @Story("Happy path checkout")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("User can complete a checkout end-to-end")
    void endToEndCheckoutSucceeds() {
        InventoryPage inventory = new LoginPage(page()).open().loginAs(
                ConfigReader.get("saucedemo.username"),
                ConfigReader.get("saucedemo.password"));

        CartPage cart = inventory
                .addToCart("Sauce Labs Backpack")
                .openCart();

        CheckoutPage checkout = cart.checkout()
                .fillInfo("Mohamed", "Ahmed", "07002")
                .clickContinue()
                .finish();

        assertTrue(checkout.isOrderComplete(),
                "Order should complete with 'Thank you' confirmation");
    }

    @Test
    @Story("Required field validation")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Missing postal code blocks checkout continuation")
    void missingPostalCodeBlocksCheckout() {
        CheckoutPage checkout = new LoginPage(page()).open().loginAs(
                        ConfigReader.get("saucedemo.username"),
                        ConfigReader.get("saucedemo.password"))
                .addToCart("Sauce Labs Backpack")
                .openCart()
                .checkout()
                .fillInfo("Mohamed", "Ahmed", "")
                .clickContinue();

        assertTrue(checkout.errorMessage().toLowerCase().contains("postal code"),
                "Expected postal-code error, got: " + checkout.errorMessage());
    }
}
