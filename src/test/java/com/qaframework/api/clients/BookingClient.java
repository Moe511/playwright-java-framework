package com.qaframework.api.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import com.qaframework.api.models.Booking;
import com.qaframework.api.models.CreatedBooking;
import com.qaframework.core.ConfigReader;
import io.qameta.allure.Step;

import java.util.Map;

/**
 * Domain client for restful-booker.herokuapp.com. Wraps endpoints so tests
 * stay focused on assertions rather than HTTP plumbing.
 */
public class BookingClient {

    private final ApiClient api;

    public BookingClient(ApiClient api) {
        this.api = api;
    }

    @Step("POST /auth (get token)")
    public String authenticate() {
        Map<String, String> creds = Map.of(
                "username", ConfigReader.get("booker.username"),
                "password", ConfigReader.get("booker.password"));
        APIResponse res = api.request().post("/auth",
                RequestOptions.create().setData(creds));
        if (!res.ok()) {
            throw new RuntimeException("Auth failed: " + res.status() + " " + res.text());
        }
        try {
            return ApiClient.MAPPER.readTree(res.body()).get("token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse auth response", e);
        }
    }

    @Step("POST /booking")
    public CreatedBooking create(Booking booking) {
        APIResponse res = api.request().post("/booking",
                RequestOptions.create().setData(toJson(booking)));
        require(res, 200);
        try {
            return ApiClient.MAPPER.readValue(res.body(), CreatedBooking.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse create response", e);
        }
    }

    @Step("GET /booking/{id}")
    public Booking get(int id) {
        APIResponse res = api.request().get("/booking/" + id);
        require(res, 200);
        try {
            return ApiClient.MAPPER.readValue(res.body(), Booking.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse get response", e);
        }
    }

    @Step("PUT /booking/{id}")
    public Booking update(int id, Booking booking, String token) {
        APIResponse res = api.request().put("/booking/" + id, RequestOptions.create()
                .setHeader("Cookie", "token=" + token)
                .setData(toJson(booking)));
        require(res, 200);
        try {
            return ApiClient.MAPPER.readValue(res.body(), Booking.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse update response", e);
        }
    }

    @Step("DELETE /booking/{id}")
    public int delete(int id, String token) {
        APIResponse res = api.request().delete("/booking/" + id, RequestOptions.create()
                .setHeader("Cookie", "token=" + token));
        // restful-booker returns 201 on successful delete (quirk of the API)
        return res.status();
    }

    @Step("GET /booking/{id} (raw status)")
    public int statusOf(int id) {
        return api.request().get("/booking/" + id).status();
    }

    // --- helpers ---

    private static String toJson(Object o) {
        try {
            return ApiClient.MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize " + o.getClass().getSimpleName(), e);
        }
    }

    private static void require(APIResponse res, int expected) {
        if (res.status() != expected) {
            throw new AssertionError("Expected HTTP " + expected
                    + " but got " + res.status() + ": " + res.text());
        }
    }
}
