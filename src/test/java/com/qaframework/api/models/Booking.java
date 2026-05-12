package com.qaframework.api.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Booking {

    @JsonProperty("firstname")
    private String firstname;

    @JsonProperty("lastname")
    private String lastname;

    @JsonProperty("totalprice")
    private Integer totalprice;

    @JsonProperty("depositpaid")
    private Boolean depositpaid;

    @JsonProperty("bookingdates")
    private BookingDates bookingdates;

    @JsonProperty("additionalneeds")
    private String additionalneeds;

    public Booking() {}

    public static Booking sample() {
        Booking b = new Booking();
        b.firstname = "Mohamed";
        b.lastname  = "Ahmed";
        b.totalprice = 250;
        b.depositpaid = true;
        b.bookingdates = new BookingDates("2026-06-01", "2026-06-07");
        b.additionalneeds = "Breakfast";
        return b;
    }

    // getters / setters
    public String getFirstname()             { return firstname; }
    public void setFirstname(String v)       { this.firstname = v; }
    public String getLastname()              { return lastname; }
    public void setLastname(String v)        { this.lastname = v; }
    public Integer getTotalprice()           { return totalprice; }
    public void setTotalprice(Integer v)     { this.totalprice = v; }
    public Boolean getDepositpaid()          { return depositpaid; }
    public void setDepositpaid(Boolean v)    { this.depositpaid = v; }
    public BookingDates getBookingdates()    { return bookingdates; }
    public void setBookingdates(BookingDates v) { this.bookingdates = v; }
    public String getAdditionalneeds()       { return additionalneeds; }
    public void setAdditionalneeds(String v) { this.additionalneeds = v; }
}
