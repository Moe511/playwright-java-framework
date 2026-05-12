package com.qaframework.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import java.util.List;

public class CartPage extends BasePage {

    private final Locator items;
    private final Locator checkoutButton;

    public CartPage(Page page) {
        super(page);
        this.items          = page.locator(".cart_item");
        this.checkoutButton = page.locator("#checkout");
    }

    public int itemCount() {
        return items.count();
    }

    public List<String> itemNames() {
        return items.locator(".inventory_item_name").allInnerTexts();
    }

    @Step("Proceed to checkout")
    public CheckoutPage checkout() {
        checkoutButton.click();
        return new CheckoutPage(page);
    }
}
