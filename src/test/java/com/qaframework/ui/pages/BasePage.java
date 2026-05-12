package com.qaframework.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Shared behavior for page objects. Holds the Page reference and a few
 * helpers for the common interaction patterns.
 */
public abstract class BasePage {

    protected final Page page;

    protected BasePage(Page page) {
        this.page = page;
    }

    protected Locator locator(String selector) {
        return page.locator(selector);
    }

    protected void navigate(String url) {
        page.navigate(url);
    }

    public String currentUrl() {
        return page.url();
    }
}
