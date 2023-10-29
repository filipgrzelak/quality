package org.example.brand;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.example.BaseTest;
import org.example.brand.model.BrandRequest;
import org.example.brand.model.BrandResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BrandPutTest extends BaseTest {

    BrandResponse brandResponse;

    @BeforeEach
    void init() {
        RestAssured.baseURI = getBaseURL();

        BrandRequest newBrand = new BrandRequest("Kamil", "Bednarek");
        brandResponse = addObject(newBrand, "/brands", BrandResponse.class);
    }

    @AfterEach
    void cleanup() {
        deleteObject(brandResponse.id(), "/brands/%s");
    }

    @Test
    void updateBrandByIdSuccessful() throws JsonProcessingException {
        String id = brandResponse.id();

        BrandRequest updateBrand = new BrandRequest("Update", "Kamil");
        String jsonUpdateBrand = getObjectMapper().writeValueAsString(updateBrand);

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .body(jsonUpdateBrand)
                .when()
                .put("/brands/%s".formatted(id))
                .then()
                .statusCode(200);

        String responseMessage = response.extract().body().jsonPath().getString("success");
        assertEquals("true", responseMessage);
    }

    @Test
    void updateBrandByIdUnsuccessfulByBadMethod() throws JsonProcessingException {

        String id = brandResponse.id();

        BrandRequest updateBrand = new BrandRequest("Update", "Kamil");
        String jsonUpdateBrand = getObjectMapper().writeValueAsString(updateBrand);

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .body(jsonUpdateBrand)
                .when()
                .post("/brands/%s".formatted(id))
                .then()
                .statusCode(405);

        String responseMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Method is not allowed for the requested route", responseMessage);
    }

    @Test
    void updateBrandByIdUnsuccessfulByInvalidId() throws JsonProcessingException {

        BrandRequest updateBrand = new BrandRequest("Update", "Kamil");
        String jsonUpdateBrand = getObjectMapper().writeValueAsString(updateBrand);

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .body(jsonUpdateBrand)
                .when()
                .put("/brands/%s".formatted("badId"))
                .then()
                .statusCode(404);

        String responseMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Resource not found", responseMessage);
    }

    @Test
    void updateBrandByIdUnsuccessfulByBadBodyFormat() throws JsonProcessingException {

        String jsonUpdateBrand = "{brand: update}";

        given()
                .header("Content-Type", "application/json")
                .body(jsonUpdateBrand)
                .when()
                .put("/brands/%s".formatted("badId"))
                .then()
                .statusCode(422);
    }
}
