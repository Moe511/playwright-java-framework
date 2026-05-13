package com.qaframework.bdd.steps.api;

import com.qaframework.api.clients.BookingClient;
import com.qaframework.api.models.Booking;
import com.qaframework.api.models.BookingDates;
import com.qaframework.api.models.CreatedBooking;
import com.qaframework.bdd.context.APIContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingSteps {

    private final APIContext ctx;
    private BookingClient client;
    private Booking lastFetched;

    public BookingSteps(APIContext ctx) {
        this.ctx = ctx;
    }

    private BookingClient client() {
        if (client == null) {
            client = new BookingClient(ctx.getApiClient());
        }
        return client;
    }

    @Given("I am authenticated with the booking API")
    public void iAmAuthenticatedWithBookingApi() {
        ctx.authToken = client().authenticate();
    }

    @Given("I have created a booking with default data")
    public void iHaveCreatedBookingWithDefaultData() {
        ctx.lastBookingId = client().create(Booking.sample()).getBookingid();
    }

    @When("I create a booking with default data")
    public void iCreateBookingWithDefaultData() {
        CreatedBooking created = client().create(Booking.sample());
        ctx.lastBookingId = created.getBookingid();
        lastFetched = created.getBooking();
    }

    @When("I create a booking with firstname {string}, lastname {string}, and total price {int}")
    public void iCreateBookingWithSpecificData(String firstname, String lastname, int price) {
        Booking b = Booking.sample();
        b.setFirstname(firstname);
        b.setLastname(lastname);
        b.setTotalprice(price);
        CreatedBooking created = client().create(b);
        ctx.lastBookingId = created.getBookingid();
        lastFetched = created.getBooking();
    }

    @Then("the response should include a positive booking ID")
    public void theResponseShouldIncludePositiveId() {
        assertTrue(ctx.lastBookingId > 0,
                "Booking ID should be positive, got: " + ctx.lastBookingId);
    }

    @When("I retrieve the booking by its ID")
    public void iRetrieveBookingById() {
        lastFetched = client().get(ctx.lastBookingId);
    }

    @Then("the booking firstname should be {string}")
    public void theBookingFirstnameShouldBe(String name) {
        assertEquals(name, lastFetched.getFirstname());
    }

    @Then("the booking lastname should be {string}")
    public void theBookingLastnameShouldBe(String name) {
        assertEquals(name, lastFetched.getLastname());
    }

    @Then("the booking total price should be {int}")
    public void theBookingTotalPriceShouldBe(int price) {
        assertEquals(price, lastFetched.getTotalprice());
    }

    @When("I update the booking lastname to {string} and total price to {int}")
    public void iUpdateTheBooking(String lastname, int price) {
        Booking updated = Booking.sample();
        updated.setLastname(lastname);
        updated.setTotalprice(price);
        updated.setBookingdates(new BookingDates("2026-07-10", "2026-07-12"));
        lastFetched = client().update(ctx.lastBookingId, updated, ctx.authToken);
    }

    @When("I delete the booking")
    public void iDeleteTheBooking() {
        ctx.lastDeleteStatus = client().delete(ctx.lastBookingId, ctx.authToken);
    }

    @Then("the delete response should be 200 or 201")
    public void theDeleteResponseShouldBe200Or201() {
        assertTrue(ctx.lastDeleteStatus == 200 || ctx.lastDeleteStatus == 201,
                "Expected 200 or 201 on delete, got: " + ctx.lastDeleteStatus);
    }

    @And("retrieving the booking by ID should return 404")
    public void retrievingBookingShouldReturn404() {
        assertEquals(404, client().statusOf(ctx.lastBookingId),
                "GET on deleted booking should return 404");
    }

    @When("I create a booking with check-in {string} and check-out {string}")
    public void iCreateBookingWithDates(String checkIn, String checkOut) {
        Booking b = Booking.sample();
        b.setBookingdates(new BookingDates(checkIn, checkOut));
        CreatedBooking created = client().create(b);
        ctx.lastBookingId = created.getBookingid();
        lastFetched = created.getBooking();
    }

    @Then("the booking check-in date should be {string}")
    public void theBookingCheckInShouldBe(String date) {
        assertEquals(date, lastFetched.getBookingdates().getCheckin());
    }

    @Then("the booking check-out date should be {string}")
    public void theBookingCheckOutShouldBe(String date) {
        assertEquals(date, lastFetched.getBookingdates().getCheckout());
    }
}
