package com.qaframework.bdd.hooks;

import com.qaframework.bdd.context.APIContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class APIHooks {

    private final APIContext ctx;

    public APIHooks(APIContext ctx) {
        this.ctx = ctx;
    }

    @Before("@api")
    public void startApiClient() {
        ctx.initialize();
    }

    @After("@api")
    public void stopApiClient() {
        ctx.tearDown();
    }
}
