package com.qaframework.core;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Retries a flaky test up to {@code maxAttempts} times before reporting failure.
 * Place on a method to retry just that test, or on a class to retry all tests in it.
 *
 * <pre>
 *   {@literal @}Test
 *   {@literal @}Retry(maxAttempts = 3)
 *   void myFlakyTest() { ... }
 * </pre>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(RetryExtension.class)
public @interface Retry {
    int maxAttempts() default 3;
}
