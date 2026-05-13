package com.qaframework.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;
import io.qameta.allure.Step;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryPage extends BasePage {

    private final Locator inventoryContainer;
    private final Locator cartBadge;
    private final Locator cartLink;
    private final Locator sortDropdown;
    private final Locator burgerMenuButton;

    public InventoryPage(Page page) {
        super(page);
        this.inventoryContainer = page.locator(".inventory_list");
        this.cartBadge          = page.locator(".shopping_cart_badge");
        this.cartLink           = page.locator(".shopping_cart_link");
        this.sortDropdown       = page.locator(".product_sort_container");
        this.burgerMenuButton   = page.locator("#react-burger-menu-btn");
    }

    public boolean isLoaded() {
        return inventoryContainer.isVisible();
    }

    public int itemCount() {
        return inventoryContainer.locator(".inventory_item").count();
    }

    @Step("Add product '{productName}' to cart")
    public InventoryPage addToCart(String productName) {
        Locator card = page.locator(".inventory_item", new Page.LocatorOptions()
                .setHasText(productName));
        card.locator("button:has-text('Add to cart')").click();
        return this;
    }

    @Step("Remove product '{productName}' from cart")
    public InventoryPage removeFromCart(String productName) {
        Locator card = page.locator(".inventory_item", new Page.LocatorOptions()
                .setHasText(productName));
        card.locator("button:has-text('Remove')").click();
        return this;
    }

    @Step("Sort products by '{label}'")
    public InventoryPage sortBy(String label) {
        sortDropdown.selectOption(new SelectOption().setLabel(label));
        return this;
    }

    public int cartCount() {
        return cartBadge.isVisible() ? Integer.parseInt(cartBadge.innerText().trim()) : 0;
    }

    public String firstProductName() {
        return page.locator(".inventory_item_name").first().innerText();
    }

    public List<String> allProductNames() {
        return page.locator(".inventory_item_name").allInnerTexts();
    }

    public List<Double> allProductPrices() {
        return page.locator(".inventory_item_price").allInnerTexts().stream()
                .map(p -> Double.parseDouble(p.replace("$", "").trim()))
                .collect(Collectors.toList());
    }

    @Step("Open cart")
    public CartPage openCart() {
        cartLink.click();
        return new CartPage(page);
    }

    @Step("Log out")
    public LoginPage logout() {
        burgerMenuButton.click();
        page.locator("#logout_sidebar_link").click();
        return new LoginPage(page);
    }
}
