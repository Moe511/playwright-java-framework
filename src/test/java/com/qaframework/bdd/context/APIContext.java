package com.qaframework.bdd.context;

import com.qaframework.api.clients.ApiClient;
import com.qaframework.api.models.Booking;

public class APIContext {

    private ApiClient apiClient;

    public String authToken;
    public int lastBookingId;
    public Booking lastBooking;
    public int lastDeleteStatus;

    public void initialize() {
        apiClient = new ApiClient();
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void tearDown() {
        if (apiClient != null) {
            apiClient.close();
        }
    }
}
