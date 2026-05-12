package com.qaframework.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response wrapper for POST /booking. restful-booker returns
 * { "bookingid": 123, "booking": { ... } }
 */
public class CreatedBooking {

    @JsonProperty("bookingid")
    private Integer bookingid;

    @JsonProperty("booking")
    private Booking booking;

    public Integer getBookingid()        { return bookingid; }
    public void setBookingid(Integer v)  { this.bookingid = v; }
    public Booking getBooking()          { return booking; }
    public void setBooking(Booking v)    { this.booking = v; }
}
