package com.qaframework.api.tests;

import com.qaframework.api.clients.BookingClient;
import com.qaframework.api.models.Booking;
import com.qaframework.api.models.BookingDates;
import com.qaframework.api.models.CreatedBooking;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("restful-booker")
@Feature("Booking CRUD")
public class BookingCrudTest extends BaseApiTest {

    @Test
    @Story("Create booking")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("POST /booking returns a booking ID and echoes the payload")
    void createBookingReturnsId() {
        BookingClient client = new BookingClient(api);
        CreatedBooking created = client.create(Booking.sample());

        assertNotNull(created.getBookingid(), "bookingid should be present");
        assertTrue(created.getBookingid() > 0, "bookingid should be positive");
        assertEquals("Mohamed", created.getBooking().getFirstname());
    }

    @Test
    @Story("Read booking")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("GET /booking/{id} returns the booking we just created")
    void canReadCreatedBooking() {
        BookingClient client = new BookingClient(api);
        int id = client.create(Booking.sample()).getBookingid();

        Booking fetched = client.get(id);
        assertEquals("Mohamed", fetched.getFirstname());
        assertEquals("Ahmed", fetched.getLastname());
        assertEquals(250, fetched.getTotalprice());
    }

    @Test
    @Story("Update booking")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("PUT /booking/{id} with a valid token updates fields")
    void canUpdateBookingWithToken() {
        BookingClient client = new BookingClient(api);
        String token = client.authenticate();
        int id = client.create(Booking.sample()).getBookingid();

        Booking updated = Booking.sample();
        updated.setLastname("Updated");
        updated.setTotalprice(999);
        updated.setBookingdates(new BookingDates("2026-07-10", "2026-07-12"));

        Booking response = client.update(id, updated, token);
        assertEquals("Updated", response.getLastname());
        assertEquals(999, response.getTotalprice());
    }

    @Test
    @Story("Delete booking")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("DELETE /booking/{id} removes the booking")
    void canDeleteBooking() {
        BookingClient client = new BookingClient(api);
        String token = client.authenticate();
        int id = client.create(Booking.sample()).getBookingid();

        int deleteStatus = client.delete(id, token);
        // restful-booker quirk: returns 201 on successful delete
        assertTrue(deleteStatus == 201 || deleteStatus == 200,
                "Expected 200/201 on delete, got " + deleteStatus);

        int getStatus = client.statusOf(id);
        assertEquals(404, getStatus, "GET on deleted booking should be 404");
    }
}
