# Playwright Java Test Framework

<!-- After pushing to GitHub, replace OWNER/REPO with your actual repo path -->
![Tests](https://github.com/OWNER/REPO/actions/workflows/test.yml/badge.svg)

A UI + API test automation framework using **Playwright for Java**, **JUnit 5**, and **Allure**.

It tests two public demo apps:

- **UI:** [saucedemo.com](https://www.saucedemo.com) — login, cart, checkout flows.
- **API:** [restful-booker.herokuapp.com](https://restful-booker.herokuapp.com) — auth + full booking CRUD.

## Features

- Page Object Model for UI tests.
- Domain client + Jackson models for API tests (Playwright `APIRequestContext`, no third-party HTTP lib needed).
- Parallel execution at method and class level (JUnit 5 dynamic config).
- Allure reporting with `@Step` annotations, screenshots on failure, and Playwright traces (open in [trace.playwright.dev](https://trace.playwright.dev)).
- Thread-isolated browser contexts so parallel runs don't share cookies or storage.
- Maven profiles to run UI-only or API-only suites.

## Project layout

```
src/test/
├── java/com/qaframework/
│   ├── core/                  BaseTest, BrowserManager, ConfigReader
│   ├── ui/
│   │   ├── pages/             Page Objects (Login, Inventory, Cart, Checkout)
│   │   └── tests/             LoginTest, CartTest, CheckoutTest
│   └── api/
│       ├── clients/           ApiClient, BookingClient
│       ├── models/            Booking, BookingDates, CreatedBooking
│       └── tests/             AuthTest, BookingCrudTest, BaseApiTest
└── resources/
    ├── config.properties
    ├── junit-platform.properties
    └── allure.properties
```

## Prerequisites

- **Java 17+**
- **Maven 3.8+**
- Internet access (tests hit live demo sites)

## First-time setup

Install Playwright's browser binaries (one-time, ~150 MB):

```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.microsoft.playwright.CLI" -Dexec.args="install"
```

Alternatively, the first test run will prompt with the command to run.

## Running tests

```bash
# Everything (UI + API, in parallel)
mvn test

# UI only
mvn test -Pui

# API only
mvn test -Papi

# Headed mode (watch the browser)
mvn test -Dheadless=false

# Different browser
mvn test -Dbrowser=firefox       # or webkit

# Override base URLs
mvn test -Dui.baseUrl=https://www.saucedemo.com -Dapi.baseUrl=https://restful-booker.herokuapp.com
```

## Viewing the Allure report

After a test run:

```bash
mvn allure:serve
```

This launches a local web server and opens the report in your browser. Failed tests include:

- Screenshot at the moment of failure
- Playwright trace zip (`target/traces/*.zip`) — drag into [trace.playwright.dev](https://trace.playwright.dev) to step through every action and network call
- Video recording (`target/videos/`)

## Configuration

`src/test/resources/config.properties` controls defaults. Any value can be overridden via `-Dkey=value` on the Maven command line.

| Key | Default | Notes |
|-----|---------|-------|
| `ui.baseUrl` | `https://www.saucedemo.com` | UI test target |
| `api.baseUrl` | `https://restful-booker.herokuapp.com` | API test target |
| `browser` | `chromium` | `chromium`, `firefox`, or `webkit` |
| `headless` | `true` | Set to `false` to see the browser |
| `slowMo` | `0` | Milliseconds delay between actions (debugging) |
| `defaultTimeout` | `15000` | Locator/action timeout in ms |
| `navigationTimeout` | `30000` | Page navigation timeout in ms |

## Parallelism

JUnit 5 parallel mode is **enabled by default**, configured in `junit-platform.properties`:

- `parallel.mode.default = concurrent` — methods in the same class run in parallel.
- `parallel.mode.classes.default = concurrent` — different classes also run in parallel.
- `config.strategy = dynamic` (`factor = 1.0`) — one test thread per CPU core.

Each test gets its own Playwright/Browser/Context/Page (UI) or `APIRequestContext` (API), so no shared state.

## Adding a new UI test

1. Create or extend a page object under `src/test/java/com/qaframework/ui/pages/`.
2. Add a test class under `ui/tests/` that extends `BaseTest`.
3. Use `page()` from `BaseTest` to access the Playwright `Page`.

```java
@Test
@DisplayName("Example")
void example() {
    new LoginPage(page()).open().loginAs("standard_user", "secret_sauce");
}
```

## Adding a new API test

1. Add a method to `BookingClient` (or create a new client) for the endpoint.
2. Add a test class under `api/tests/` that extends `BaseApiTest`.
3. Use the injected `api` field to issue requests through `BookingClient`.

## CI

GitHub Actions workflow lives at `.github/workflows/test.yml`. Every push or PR to `main`/`master` triggers a full test run on `ubuntu-latest`. The workflow:

- Sets up JDK 17 (Temurin) and caches Maven dependencies
- Caches Playwright browser binaries between runs
- Installs Playwright with `--with-deps` (pulls Linux system libs)
- Runs `mvn test` (parallel execution preserved)
- Uploads four artifacts: `allure-results`, `surefire-reports`, plus `playwright-traces` and `playwright-videos` on failure
- Publishes a second job that builds the Allure HTML report and uploads it as `allure-report` — download and open `index.html` locally

To get the build badge working, replace `OWNER/REPO` at the top of this README with your actual GitHub path after pushing.

## Roadmap

- Docker runner.
- AI-assisted test scaffolding (turn a user story into a Playwright test stub via the Claude API).
- Cross-browser matrix (chromium/firefox/webkit) via workflow strategy.
