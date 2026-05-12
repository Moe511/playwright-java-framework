package com.qaframework.core;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.ByteArrayInputStream;
import java.util.Optional;

/**
 * Base class for UI tests. Owns the BrowserManager lifecycle and captures
 * screenshots/traces on failure so Allure reports are useful for debugging.
 */
@ExtendWith(BaseTest.FailureCapture.class)
public abstract class BaseTest {

    // ThreadLocal so parallel tests don't share state.
    private static final ThreadLocal<BrowserManager> BM = ThreadLocal.withInitial(BrowserManager::new);
    private static final ThreadLocal<String> CURRENT_TEST = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> TEST_FAILED = ThreadLocal.withInitial(() -> false);

    @BeforeEach
    void setUpBase(TestInfo info) {
        CURRENT_TEST.set(info.getDisplayName());
        TEST_FAILED.set(false);
        BM.get().start();
    }

    @AfterEach
    void tearDownBase() {
        BrowserManager bm = BM.get();
        try {
            if (Boolean.TRUE.equals(TEST_FAILED.get())) {
                // Attach screenshot to Allure
                byte[] png = bm.screenshot();
                if (png.length > 0) {
                    Allure.addAttachment("Failure screenshot", "image/png", new ByteArrayInputStream(png), ".png");
                }
                bm.saveTraceOnFailure(CURRENT_TEST.get());
            }
        } finally {
            bm.stop();
            BM.remove();
            CURRENT_TEST.remove();
            TEST_FAILED.remove();
        }
    }

    protected Page page() {
        return BM.get().page();
    }

    /** JUnit 5 extension that marks the thread-local flag when a test fails. */
    public static class FailureCapture implements TestWatcher {
        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            TEST_FAILED.set(true);
        }

        @Override
        public void testAborted(ExtensionContext context, Throwable cause) {
            TEST_FAILED.set(true);
        }

        @Override
        public void testSuccessful(ExtensionContext context) {
            // no-op
        }

        @Override
        public void testDisabled(ExtensionContext context, Optional<String> reason) {
            // no-op
        }
    }
}
