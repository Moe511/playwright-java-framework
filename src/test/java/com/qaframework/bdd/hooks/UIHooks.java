package com.qaframework.bdd.hooks;

import com.qaframework.bdd.context.UIContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;

public class UIHooks {

    private final UIContext ctx;

    public UIHooks(UIContext ctx) {
        this.ctx = ctx;
    }

    @Before("@ui")
    public void startBrowser() {
        ctx.initialize();
    }

    @After("@ui")
    public void stopBrowser(Scenario scenario) {
        if (scenario.isFailed() && ctx.getBrowserManager() != null) {
            byte[] png = ctx.getBrowserManager().screenshot();
            if (png.length > 0) {
                Allure.addAttachment("Failure screenshot", "image/png",
                        new ByteArrayInputStream(png), ".png");
            }
        }
        ctx.tearDown(scenario.getName(), scenario.isFailed());
    }
}
