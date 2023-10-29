package org.example.brand;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.example.BaseTest;
import org.example.brand.model.BrandRequest;
import org.example.brand.model.BrandResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BrandPostTest extends BaseTest {

    @BeforeEach
    void init() {
        RestAssured.baseURI = getBaseURL();
    }

    @Test
    void postBrandSuccessful() throws IOException {

        int numberOfRecords = numberOfRecords("/brands");

        BrandRequest newBrand = new BrandRequest("Kamil", "Bednarek");
        String jsonNewBrand = getObjectMapper().writeValueAsString(newBrand);

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .body(jsonNewBrand)
                .when()
                .post("/brands")
                .then()
                .statusCode(201);

        int actualNumberOfRecords = numberOfRecords("/brands");
        assertEquals(numberOfRecords + 1, actualNumberOfRecords);

        BrandResponse createdBrand = response.extract().body().jsonPath().getObject(".", BrandResponse.class);
        assertEquals(newBrand.name(), createdBrand.name());
        assertEquals(newBrand.slug(), createdBrand.slug());

        deleteObject(createdBrand.id(), "/brands/%s");
    }

    @Test
    void postBrandUnsuccessfulByBadMethod() throws IOException {

        int numberOfRecords = numberOfRecords("/brands");

        BrandRequest newBrand = new BrandRequest("Kamil", "Bednarek");
        String jsonNewBrand = getObjectMapper().writeValueAsString(newBrand);

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .body(jsonNewBrand)
                .when()
                .delete("/brands")
                .then()
                .statusCode(405);

        int actualNumberOfRecords = numberOfRecords("/brands");
        assertEquals(numberOfRecords, actualNumberOfRecords);

        String errorMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Method is not allowed for the requested route", errorMessage);
    }

    @Test
    void postBrandUnsuccessfulByBadBodyFormat() {

        int numberOfRecords = numberOfRecords("/brands");

        String jsonNewBrand = "{brand: newBrand}";

        given()
                .header("Content-Type", "application/json")
                .body(jsonNewBrand)
                .when()
                .post("/brands")
                .then()
                .statusCode(422);

        int actualNumberOfRecords = numberOfRecords("/brands");
        assertEquals(numberOfRecords, actualNumberOfRecords);
    }
}
