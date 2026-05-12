package com.qaframework.ui.tests;

import com.qaframework.core.BaseTest;
import com.qaframework.core.ConfigReader;
import com.qaframework.ui.pages.CartPage;
import com.qaframework.ui.pages.InventoryPage;
import com.qaframework.ui.pages.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Saucedemo")
@Feature("Shopping cart")
public class CartTest extends BaseTest {

    private InventoryPage loginAndOpenInventory() {
        return new LoginPage(page()).open().loginAs(
                ConfigReader.get("saucedemo.username"),
                ConfigReader.get("saucedemo.password"));
    }

    @Test
    @Story("Add single item")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Adding one item updates the cart badge to 1")
    void addingItemUpdatesBadge() {
        InventoryPage inventory = loginAndOpenInventory()
                .addToCart("Sauce Labs Backpack");

        assertEquals(1, inventory.cartCount(), "Cart badge should show 1");
    }

    @Test
    @Story("Add multiple items")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Adding multiple items shows all in the cart page")
    void multipleItemsAppearInCart() {
        CartPage cart = loginAndOpenInventory()
                .addToCart("Sauce Labs Backpack")
                .addToCart("Sauce Labs Bike Light")
                .addToCart("Sauce Labs Bolt T-Shirt")
                .openCart();

        assertEquals(3, cart.itemCount());

        List<String> names = cart.itemNames();
        assertTrue(names.contains("Sauce Labs Backpack"));
        assertTrue(names.contains("Sauce Labs Bike Light"));
        assertTrue(names.contains("Sauce Labs Bolt T-Shirt"));
    }
}
