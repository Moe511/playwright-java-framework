package com.qaframework.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class InventoryPage extends BasePage {

    private final Locator inventoryContainer;
    private final Locator cartBadge;
    private final Locator cartLink;

    public InventoryPage(Page page) {
        super(page);
        this.inventoryContainer = page.locator(".inventory_list");
        this.cartBadge          = page.locator(".shopping_cart_badge");
        this.cartLink           = page.locator(".shopping_cart_link");
    }

    public boolean isLoaded() {
        return inventoryContainer.isVisible();
    }

    public int itemCount() {
        return inventoryContainer.locator(".inventory_item").count();
    }

    @Step("Add product '{productName}' to cart")
    public InventoryPage addToCart(String productName) {
        // Each card has a button whose data-test attribute encodes the slug.
        // Doing it by visible text is more robust to layout changes.
        Locator card = page.locator(".inventory_item", new Page.LocatorOptions()
                .setHasText(productName));
        card.locator("button:has-text('Add to cart')").click();
        return this;
    }

    public int cartCount() {
        return cartBadge.isVisible() ? Integer.parseInt(cartBadge.innerText().trim()) : 0;
    }

    @Step("Open cart")
    public CartPage openCart() {
        cartLink.click();
        return new CartPage(page);
    }
}
