package com.qaframework.ui.tests;

import com.qaframework.core.BaseTest;
import com.qaframework.core.ConfigReader;
import com.qaframework.ui.pages.InventoryPage;
import com.qaframework.ui.pages.LoginPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Saucedemo")
@Feature("Authentication")
public class LoginTest extends BaseTest {

    @Test
    @Story("Happy path login")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Standard user can log in and lands on the inventory page")
    void standardUserCanLogIn() {
        LoginPage login = new LoginPage(page()).open();
        InventoryPage inventory = login.loginAs(
                ConfigReader.get("saucedemo.username"),
                ConfigReader.get("saucedemo.password"));

        assertTrue(inventory.isLoaded(), "Inventory page should load after login");
        assertEquals(6, inventory.itemCount(), "Saucedemo inventory should show 6 items");
    }

    @Test
    @Story("Locked-out user")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Locked-out user sees the expected error message")
    void lockedOutUserCannotLogIn() {
        LoginPage login = new LoginPage(page()).open();
        login.attemptLogin("locked_out_user", ConfigReader.get("saucedemo.password"));

        assertTrue(login.errorMessage().toLowerCase().contains("locked out"),
                "Expected lockout error, got: " + login.errorMessage());
    }

    @Test
    @Story("Invalid credentials")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Invalid credentials produce an error and stay on the login page")
    void invalidCredentialsRejected() {
        LoginPage login = new LoginPage(page()).open();
        login.attemptLogin("not_a_real_user", "wrong");

        assertTrue(login.errorMessage().toLowerCase().contains("do not match"),
                "Expected credentials error, got: " + login.errorMessage());
    }
}
