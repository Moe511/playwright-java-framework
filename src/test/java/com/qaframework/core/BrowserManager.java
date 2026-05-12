package com.qaframework.core;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Owns the Playwright lifecycle for a single test thread.
 * <p>
 * Each test gets its own Playwright + Browser + Context + Page, which keeps
 * parallel runs hermetic (no shared cookies or storage between tests).
 */
public class BrowserManager {

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    public void start() {
        playwright = Playwright.create();
        String browserName = ConfigReader.get("browser", "chromium").toLowerCase();
        boolean headless = ConfigReader.getBoolean("headless", true);
        int slowMo = ConfigReader.getInt("slowMo", 0);

        BrowserType browserType = switch (browserName) {
            case "firefox" -> playwright.firefox();
            case "webkit" -> playwright.webkit();
            default -> playwright.chromium();
        };

        browser = browserType.launch(new BrowserType.LaunchOptions()
                .setHeadless(headless)
                .setSlowMo(slowMo));

        int width = ConfigReader.getInt("viewport.width", 1280);
        int height = ConfigReader.getInt("viewport.height", 720);

        Browser.NewContextOptions ctxOpts = new Browser.NewContextOptions()
                .setViewportSize(width, height);

        if (ConfigReader.getBoolean("video.onFailure", true)) {
            ctxOpts.setRecordVideoDir(Paths.get("target/videos"));
        }

        context = browser.newContext(ctxOpts);

        if (ConfigReader.getBoolean("trace.onFailure", true)) {
            context.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(true));
        }

        page = context.newPage();
        page.setDefaultTimeout(ConfigReader.getInt("defaultTimeout", 15000));
        page.setDefaultNavigationTimeout(ConfigReader.getInt("navigationTimeout", 30000));
    }

    /** Stop tracing and save artifact only when a test failed. */
    public void saveTraceOnFailure(String testName) {
        if (context != null && ConfigReader.getBoolean("trace.onFailure", true)) {
            Path tracePath = Paths.get("target/traces", sanitize(testName) + ".zip");
            context.tracing().stop(new Tracing.StopOptions().setPath(tracePath));
        }
    }

    public byte[] screenshot() {
        return page == null ? new byte[0] : page.screenshot();
    }

    public Page page() {
        return page;
    }

    public void stop() {
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    private static String sanitize(String name) {
        return name == null ? "trace" : name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
