package com.qaframework.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class CheckoutPage extends BasePage {

    private final Locator firstName;
    private final Locator lastName;
    private final Locator postalCode;
    private final Locator continueButton;
    private final Locator finishButton;
    private final Locator completeHeader;
    private final Locator errorBanner;

    public CheckoutPage(Page page) {
        super(page);
        this.firstName      = page.locator("#first-name");
        this.lastName       = page.locator("#last-name");
        this.postalCode     = page.locator("#postal-code");
        this.continueButton = page.locator("#continue");
        this.finishButton   = page.locator("#finish");
        this.completeHeader = page.locator(".complete-header");
        this.errorBanner    = page.locator("[data-test='error']");
    }

    @Step("Fill checkout info: {first} {last}, {zip}")
    public CheckoutPage fillInfo(String first, String last, String zip) {
        firstName.fill(first);
        lastName.fill(last);
        postalCode.fill(zip);
        return this;
    }

    @Step("Continue to overview")
    public CheckoutPage clickContinue() {
        continueButton.click();
        return this;
    }

    @Step("Finish order")
    public CheckoutPage finish() {
        finishButton.click();
        return this;
    }

    public boolean isOrderComplete() {
        return completeHeader.isVisible()
                && completeHeader.innerText().toLowerCase().contains("thank you");
    }

    public String errorMessage() {
        return errorBanner.isVisible() ? errorBanner.innerText() : "";
    }
}
