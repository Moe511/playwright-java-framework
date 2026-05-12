package com.qaframework.api.clients;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.qaframework.core.ConfigReader;

/**
 * Holds a single Playwright APIRequestContext for API tests. One per test
 * (constructed in @BeforeEach) keeps parallel runs isolated.
 */
public class ApiClient implements AutoCloseable {

    public static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final Playwright playwright;
    private final APIRequestContext request;

    public ApiClient() {
        this.playwright = Playwright.create();
        this.request = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL(ConfigReader.get("api.baseUrl"))
                .setExtraHTTPHeaders(java.util.Map.of(
                        "Content-Type", "application/json",
                        "Accept", "application/json"
                )));
    }

    public APIRequestContext request() {
        return request;
    }

    @Override
    public void close() {
        if (request != null) request.dispose();
        if (playwright != null) playwright.close();
    }
}
