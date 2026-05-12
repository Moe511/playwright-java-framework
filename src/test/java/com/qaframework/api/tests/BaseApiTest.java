package com.qaframework.api.tests;

import com.qaframework.api.clients.ApiClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * One ApiClient per test. Lightweight enough that the cost is fine and
 * the isolation pays off when tests run in parallel.
 */
public abstract class BaseApiTest {

    protected ApiClient api;

    @BeforeEach
    void initApi() {
        api = new ApiClient();
    }

    @AfterEach
    void closeApi() {
        if (api != null) api.close();
    }
}
