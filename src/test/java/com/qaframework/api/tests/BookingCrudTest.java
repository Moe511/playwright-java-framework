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

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(created.getBookingid()).as("bookingid should be present").isNotNull();
        assertThat(created.getBookingid()).as("bookingid should be positive").isPositive();
        assertThat(created.getBooking().getFirstname()).isEqualTo("Mohamed");
    }

    @Test
    @Story("Read booking")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("GET /booking/{id} returns the booking we just created")
    void canReadCreatedBooking() {
        BookingClient client = new BookingClient(api);
        int id = client.create(Booking.sample()).getBookingid();

        Booking fetched = client.get(id);
        assertThat(fetched.getFirstname()).isEqualTo("Mohamed");
        assertThat(fetched.getLastname()).isEqualTo("Ahmed");
        assertThat(fetched.getTotalprice()).isEqualTo(250);
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
        assertThat(response.getLastname()).isEqualTo("Updated");
        assertThat(response.getTotalprice()).isEqualTo(999);
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
        assertThat(deleteStatus).as("Expected 200/201 on delete").isIn(200, 201);

        int getStatus = client.statusOf(id);
        assertThat(getStatus).as("GET on deleted booking should be 404").isEqualTo(404);
    }
}
