package com.qaframework.bdd.context;

import com.microsoft.playwright.Page;
import com.qaframework.core.BrowserManager;
import com.qaframework.ui.pages.CartPage;
import com.qaframework.ui.pages.CheckoutPage;
import com.qaframework.ui.pages.InventoryPage;
import com.qaframework.ui.pages.LoginPage;

public class UIContext {

    private BrowserManager browserManager;

    public LoginPage loginPage;
    public InventoryPage inventoryPage;
    public CartPage cartPage;
    public CheckoutPage checkoutPage;

    public void initialize() {
        browserManager = new BrowserManager();
        browserManager.start();
    }

    public Page getPage() {
        return browserManager.page();
    }

    public BrowserManager getBrowserManager() {
        return browserManager;
    }

    public void tearDown(String scenarioName, boolean failed) {
        if (browserManager != null) {
            if (failed) {
                browserManager.saveTraceOnFailure(scenarioName);
            }
            browserManager.stop();
        }
    }
}
