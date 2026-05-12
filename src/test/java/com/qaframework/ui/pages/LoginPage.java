package com.qaframework.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.qaframework.core.ConfigReader;
import io.qameta.allure.Step;

public class LoginPage extends BasePage {

    private final Locator usernameInput;
    private final Locator passwordInput;
    private final Locator loginButton;
    private final Locator errorBanner;

    public LoginPage(Page page) {
        super(page);
        this.usernameInput = page.locator("#user-name");
        this.passwordInput = page.locator("#password");
        this.loginButton   = page.locator("#login-button");
        this.errorBanner   = page.locator("[data-test='error']");
    }

    @Step("Open saucedemo login page")
    public LoginPage open() {
        navigate(ConfigReader.get("ui.baseUrl"));
        return this;
    }

    @Step("Login as {username}")
    public InventoryPage loginAs(String username, String password) {
        usernameInput.fill(username);
        passwordInput.fill(password);
        loginButton.click();
        return new InventoryPage(page);
    }

    @Step("Attempt login as {username} (expect failure)")
    public LoginPage attemptLogin(String username, String password) {
        usernameInput.fill(username);
        passwordInput.fill(password);
        loginButton.click();
        return this;
    }

    public String errorMessage() {
        return errorBanner.isVisible() ? errorBanner.innerText() : "";
    }
}
