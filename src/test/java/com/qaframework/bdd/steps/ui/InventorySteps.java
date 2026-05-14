package com.qaframework.bdd.steps.ui;

import com.qaframework.bdd.context.UIContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(ctx.inventoryPage.firstProductName()).as("First product name mismatch after sorting").isEqualTo(name);
    }

    @Then("product names should be in ascending order")
    public void productNamesShouldBeAscending() {
        List<String> actual = ctx.inventoryPage.allProductNames();
        List<String> expected = new ArrayList<>(actual);
        Collections.sort(expected);
        assertThat(actual).as("Product names should be sorted A to Z").isEqualTo(expected);
    }

    @Then("product names should be in descending order")
    public void productNamesShouldBeDescending() {
        List<String> actual = ctx.inventoryPage.allProductNames();
        List<String> expected = new ArrayList<>(actual);
        expected.sort(Collections.reverseOrder());
        assertThat(actual).as("Product names should be sorted Z to A").isEqualTo(expected);
    }

    @Then("product prices should be in ascending order")
    public void productPricesShouldBeAscending() {
        List<Double> actual = ctx.inventoryPage.allProductPrices();
        List<Double> expected = new ArrayList<>(actual);
        Collections.sort(expected);
        assertThat(actual).as("Product prices should be sorted low to high").isEqualTo(expected);
    }

    @Then("product prices should be in descending order")
    public void productPricesShouldBeDescending() {
        List<Double> actual = ctx.inventoryPage.allProductPrices();
        List<Double> expected = new ArrayList<>(actual);
        expected.sort(Collections.reverseOrder());
        assertThat(actual).as("Product prices should be sorted high to low").isEqualTo(expected);
    }
}
