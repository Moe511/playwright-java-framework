# Playwright Java Test Framework

[![Tests](https://github.com/Moe511/playwright-java-framework/actions/workflows/test.yml/badge.svg)](https://github.com/Moe511/playwright-java-framework/actions/workflows/test.yml)
[![Allure Report](https://img.shields.io/badge/Allure-Report-brightgreen)](https://moe511.github.io/playwright-java-framework/)

A production-ready UI + API + BDD test automation framework using **Playwright for Java**, **Cucumber/Gherkin**, **JUnit 5**, and **Allure**. Testers write `.feature` files — no Java required for new scenarios.

It tests two public demo apps:

- **UI:** [saucedemo.com](https://www.saucedemo.com) — login, cart, checkout, inventory, session flows.
- **API:** [restful-booker.herokuapp.com](https://restful-booker.herokuapp.com) — auth + full booking CRUD.

## Features

- **Two test layers** — 12 JUnit tests for fast regression + 28 Cucumber BDD scenarios for business-readable coverage (40 total).
- **BDD / Gherkin** — full step vocabulary across UI and API; testers add `.feature` files without touching Java.
- **Page Object Model** with fluent method chaining for UI tests.
- **Domain client + Jackson models** for API tests (Playwright `APIRequestContext`, no third-party HTTP lib).
- **Parallel execution** — JUnit 5 dynamic config + Cucumber parallel engine; each test gets an isolated browser context.
- **AssertJ** fluent assertions with descriptive failure messages across all layers.
- **`@Retry` annotation** — annotate any method or class to retry flaky tests up to N times before failing.
- **Allure reporting** — `@Step` annotations on every page and API action, screenshots on failure, Playwright traces.
- **Live Allure report** auto-published to GitHub Pages after every CI run: [moe511.github.io/playwright-java-framework](https://moe511.github.io/playwright-java-framework/)
- **`run.ps1` / `run.sh`** wrapper scripts with tag/feature filtering and interactive Allure report generation.

## Project layout

```
src/test/
├── java/com/qaframework/
│   ├── core/                  BaseTest, BrowserManager, ConfigReader, Retry, RetryExtension
│   ├── ui/
│   │   ├── pages/             BasePage, LoginPage, InventoryPage, CartPage, CheckoutPage
│   │   └── tests/             LoginTest, CartTest, CheckoutTest
│   ├── api/
│   │   ├── clients/           ApiClient, BookingClient
│   │   ├── models/            Booking, BookingDates, CreatedBooking
│   │   └── tests/             BaseApiTest, AuthTest, BookingCrudTest
│   └── bdd/
│       ├── CucumberRunner.java
│       ├── context/           UIContext, APIContext  (PicoContainer shared state per scenario)
│       ├── hooks/             UIHooks (@ui), APIHooks (@api)
│       └── steps/
│           ├── CommonSteps    (URL, title, element visibility, navigate)
│           ├── ui/            LoginSteps, CartSteps, CheckoutSteps, InventorySteps
│           └── api/           AuthSteps, BookingSteps
└── resources/
    ├── config.properties
    ├── cucumber.properties
    ├── junit-platform.properties
    ├── allure.properties
    └── features/
        ├── ui/                login.feature, cart.feature, checkout.feature,
        │                      inventory.feature, session.feature
        └── api/               auth.feature, booking.feature
```

## Prerequisites

- **Java 17+**
- **Maven 3.8+**
- Internet access (tests hit live demo sites)

## First-time setup

Install Playwright's browser binaries (one-time, ~150 MB):

```bash
mvn exec:java -Dexec.mainClass="com.microsoft.playwright.CLI" -Dexec.args="install"
```

## Running tests

### Wrapper scripts (recommended)

```powershell
# Windows
.\run.ps1                                        # all 40 tests (JUnit + BDD)
.\run.ps1 -Suite ui                              # JUnit UI tests only
.\run.ps1 -Suite api                             # JUnit API tests only
.\run.ps1 -Suite bdd                             # all BDD scenarios
.\run.ps1 -Tag smoke                             # BDD scenarios tagged @smoke
.\run.ps1 -Tag blocker                           # BDD scenarios tagged @blocker
.\run.ps1 -Feature login                         # login.feature only
.\run.ps1 -Feature booking -Tag critical         # combined filter
.\run.ps1 -Suite ui -Headed -Browser firefox     # headed Firefox UI tests
```

```bash
# Mac / Linux / Git Bash
./run.sh                                         # all 40 tests
./run.sh ui                                      # JUnit UI tests only
./run.sh bdd                                     # all BDD scenarios
./run.sh --tag smoke                             # BDD @smoke scenarios
./run.sh --feature login                         # login.feature only
./run.sh --headed --browser firefox              # headed Firefox
```

After tests complete the script asks whether to generate and open the Allure HTML report.

### Maven directly

```bash
mvn test                    # JUnit suite (parallel)
mvn test -Pui               # JUnit UI only
mvn test -Papi              # JUnit API only
mvn test -Pbdd              # Cucumber BDD suite (parallel)

# Headed mode or different browser
mvn test -Dheadless=false
mvn test -Dbrowser=firefox

# Override base URLs
mvn test -Dui.baseUrl=https://www.saucedemo.com -Dapi.baseUrl=https://restful-booker.herokuapp.com
```

## Adding a BDD scenario (no Java needed)

Open or create a `.feature` file under `src/test/resources/features/`:

```gherkin
@ui @smoke
Feature: Checkout

  Scenario: Guest can complete a purchase
    Given I am logged in as a standard user
    And I add "Sauce Labs Backpack" to the cart
    And I open the cart
    When I have proceeded to checkout
    And I fill in checkout details with first name "Jane", last name "Doe", postal code "10001"
    And I continue and finish the order
    Then I should see the order confirmation
```

All step definitions are already wired. Run with `.\run.ps1 -Feature checkout` or `.\run.ps1 -Tag smoke`.

## Adding a JUnit test

```java
@Test
@DisplayName("Example")
void example() {
    new LoginPage(page()).open().loginAs("standard_user", "secret_sauce");
}
```

Extend `BaseTest` for UI or `BaseApiTest` for API. Screenshots and traces are captured automatically on failure.

## Retrying flaky tests

```java
@Test
@Retry                        // 3 attempts by default
void networkSensitiveTest() { ... }

@Test
@Retry(maxAttempts = 2)
void lessFlaky() { ... }
```

Place `@Retry` on a class to apply to all its tests.

## Allure report

**Locally** — after any test run:

```bash
mvn allure:serve
```

**CI** — every push automatically publishes to GitHub Pages:
[moe511.github.io/playwright-java-framework](https://moe511.github.io/playwright-java-framework/)

Failed tests include a screenshot, Playwright trace zip (drag into [trace.playwright.dev](https://trace.playwright.dev)), and a step-by-step action log.

## Configuration

`src/test/resources/config.properties` — override any value with `-Dkey=value`.

| Key | Default | Notes |
|-----|---------|-------|
| `ui.baseUrl` | `https://www.saucedemo.com` | UI test target |
| `api.baseUrl` | `https://restful-booker.herokuapp.com` | API test target |
| `browser` | `chromium` | `chromium`, `firefox`, or `webkit` |
| `headless` | `true` | Set to `false` to watch the browser |
| `slowMo` | `0` | ms delay between actions (debugging) |
| `defaultTimeout` | `15000` | Locator/action timeout in ms |
| `navigationTimeout` | `30000` | Page navigation timeout in ms |

## CI

GitHub Actions (`.github/workflows/test.yml`) runs on every push and PR to `main`:

1. JUnit suite (`mvn test`) — parallel execution
2. BDD suite (`mvn test -Pbdd`) — runs even if JUnit fails
3. Allure HTML report generated and deployed to GitHub Pages with trend history

Artifacts retained for 14 days: `allure-results`, `surefire-reports`, `playwright-traces` (on failure).

## Roadmap

- [ ] Environment profiles — switch between dev/staging/prod with a single flag
- [ ] Allure categories — classify failures as product bugs vs test bugs vs flakiness
- [ ] Cross-browser matrix — chromium/firefox/webkit in parallel via workflow strategy
- [ ] Docker runner
