package org.example.brand;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.example.BaseTest;
import org.example.brand.model.BrandResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BrandGetTest extends BaseTest {

    @BeforeEach
    void init() {
        RestAssured.baseURI = getBaseURL();
    }

    @Test
    void getBrandsSuccessful() {
        ValidatableResponse response = given()
                .when()
                .get("/brands")
                .then()
                .statusCode(200);
        int numberOfRecords = response.extract().jsonPath().getList("$").size();
        assertEquals(2, numberOfRecords);
    }

    @Test
    void getBrandsUnsuccessfulByIncorrectMethod() {
        ValidatableResponse response = given()
                .when()
                .delete("/brands")
                .then()
                .statusCode(405);
        String errorMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Method is not allowed for the requested route", errorMessage);
    }

    @Test
    void getBrandByIdSuccessful() {
        BrandResponse brandFromDatabase = exampleIdPresentInDatabaseOfSpecificType();
        ValidatableResponse response = given()
                .when()
                .get("/brands/%s".formatted(brandFromDatabase.id()))
                .then()
                .statusCode(200);
        BrandResponse brandResponse = response.extract().body().jsonPath().getObject(".", BrandResponse.class);
        assertEquals(brandFromDatabase, brandResponse);
    }

    @Test
    void getBrandByIdUnsuccessfulByInvalidId() {
        ValidatableResponse response = given()
                .when()
                .get("/brands/%s".formatted("badId"))
                .then()
                .statusCode(404);
        String errorMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Requested item not found", errorMessage);
    }

    @Test
    void getBrandByIdUnsuccessfulByBadMethod() {
        ValidatableResponse response = given()
                .when()
                .post("/brands/%s".formatted("Bad id"))
                .then()
                .statusCode(405);

        String errorMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Method is not allowed for the requested route", errorMessage);
    }

    public BrandResponse exampleIdPresentInDatabaseOfSpecificType() {
        List<BrandResponse> responseList = responseListOfSpecificType("/brands", BrandResponse.class);
        return responseList.get(0);
    }
}
