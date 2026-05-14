package com.qaframework.core;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RetryExtension implements InvocationInterceptor {

    @Override
    public void interceptTestMethod(Invocation<Void> invocation,
                                    ReflectiveInvocationContext<Method> invocationContext,
                                    ExtensionContext extensionContext) throws Throwable {
        int maxAttempts = resolveMaxAttempts(extensionContext);
        Throwable lastFailure = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                if (attempt == 1) {
                    invocation.proceed();
                } else {
                    System.out.printf("[Retry] %s — attempt %d of %d%n",
                            extensionContext.getDisplayName(), attempt, maxAttempts);
                    // NOTE: reflection re-invoke skips @BeforeEach/@AfterEach between retries.
                    // Tests must be self-contained (navigate fresh on each call) for retries to be clean.
                    invocationContext.getExecutable().invoke(
                            extensionContext.getRequiredTestInstance(),
                            invocationContext.getArguments().toArray());
                }
                return;
            } catch (InvocationTargetException e) {
                lastFailure = e.getCause() != null ? e.getCause() : e;
            } catch (Throwable t) {
                lastFailure = t;
            }
        }
        throw lastFailure;
    }

    private int resolveMaxAttempts(ExtensionContext ctx) {
        Retry onMethod = ctx.getRequiredTestMethod().getAnnotation(Retry.class);
        if (onMethod != null) return onMethod.maxAttempts();
        Retry onClass = ctx.getRequiredTestClass().getAnnotation(Retry.class);
        if (onClass != null) return onClass.maxAttempts();
        return 1;
    }
}
