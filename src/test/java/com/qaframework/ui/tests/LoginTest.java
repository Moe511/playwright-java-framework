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

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(inventory.isLoaded())
                .as("Inventory page should load after login")
                .isTrue();
        assertThat(inventory.itemCount())
                .as("Saucedemo inventory should show 6 products")
                .isEqualTo(6);
    }

    @Test
    @Story("Locked-out user")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Locked-out user sees the expected error message")
    void lockedOutUserCannotLogIn() {
        LoginPage login = new LoginPage(page()).open();
        login.attemptLogin("locked_out_user", ConfigReader.get("saucedemo.password"));

        assertThat(login.errorMessage())
                .as("Expected lockout error message")
                .containsIgnoringCase("locked out");
    }

    @Test
    @Story("Invalid credentials")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Invalid credentials produce an error and stay on the login page")
    void invalidCredentialsRejected() {
        LoginPage login = new LoginPage(page()).open();
        login.attemptLogin("not_a_real_user", "wrong");

        assertThat(login.errorMessage())
                .as("Expected credentials mismatch error message")
                .containsIgnoringCase("do not match");
    }
}
