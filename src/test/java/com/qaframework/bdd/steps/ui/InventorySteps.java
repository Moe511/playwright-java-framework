package com.qaframework.bdd.steps.ui;

import com.qaframework.bdd.context.UIContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InventorySteps {

    private final UIContext ctx;

    public InventorySteps(UIContext ctx) {
        this.ctx = ctx;
    }

    @When("I sort products by {string}")
    public void iSortProductsBy(String label) {
        ctx.inventoryPage.sortBy(label);
    }

    @Then("the first product name should be {string}")
    public void theFirstProductNameShouldBe(String name) {
        assertEquals(name, ctx.inventoryPage.firstProductName(),
                "First product name mismatch after sorting");
    }

    @Then("product names should be in ascending order")
    public void productNamesShouldBeAscending() {
        List<String> actual = ctx.inventoryPage.allProductNames();
        List<String> expected = new ArrayList<>(actual);
        Collections.sort(expected);
        assertEquals(expected, actual, "Product names should be sorted A to Z");
    }

    @Then("product names should be in descending order")
    public void productNamesShouldBeDescending() {
        List<String> actual = ctx.inventoryPage.allProductNames();
        List<String> expected = new ArrayList<>(actual);
        expected.sort(Collections.reverseOrder());
        assertEquals(expected, actual, "Product names should be sorted Z to A");
    }

    @Then("product prices should be in ascending order")
    public void productPricesShouldBeAscending() {
        List<Double> actual = ctx.inventoryPage.allProductPrices();
        List<Double> expected = new ArrayList<>(actual);
        Collections.sort(expected);
        assertEquals(expected, actual, "Product prices should be sorted low to high");
    }

    @Then("product prices should be in descending order")
    public void productPricesShouldBeDescending() {
        List<Double> actual = ctx.inventoryPage.allProductPrices();
        List<Double> expected = new ArrayList<>(actual);
        expected.sort(Collections.reverseOrder());
        assertEquals(expected, actual, "Product prices should be sorted high to low");
    }
}
