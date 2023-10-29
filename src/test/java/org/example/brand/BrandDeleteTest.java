package org.example.brand;

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

class BrandDeleteTest extends BaseTest {

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
    void deleteBrandByIdSuccessful() {

        String id = brandResponse.id();

        ValidatableResponse response = given()
                .header("Authorization", adminToken())
                .when()
                .delete("/brands/%s".formatted(id))
                .then()
                .statusCode(204);
    }

    @Test
    void deleteBrandByIdUnsuccessfulByNotAdminToken() {

        String id = brandResponse.id();

        ValidatableResponse response = given()
                .header("Authorization", customerToken())
                .when()
                .delete("/brands/%s".formatted(id))
                .then()
                .statusCode(403);

        String responseMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Forbidden", responseMessage);
    }

    @Test
    void deleteBrandByIdUnsuccessfulByNoToken() {

        String id = brandResponse.id();

        ValidatableResponse response = given()
                .when()
                .delete("/brands/%s".formatted(id))
                .then()
                .statusCode(401);

        String responseMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Unauthorized", responseMessage);
    }

    @Test
    void deleteBrandByIdUnsuccessfulByInvalidId() {

        ValidatableResponse response = given()
                .header("Authorization", adminToken())
                .when()
                .delete("/brands/%s".formatted("01HDY9FP3WHH8Y6P5RCJKEBJHA"))
                .then()
                .statusCode(404);

        String responseMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Resource not found", responseMessage);
    }

    @Test
    void deleteBrandByIdUnsuccessfulByInvalidMethod() {

        ValidatableResponse response = given()
                .header("Authorization", adminToken())
                .when()
                .post("/brands/%s".formatted("id"))
                .then()
                .statusCode(405);

        String responseMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Method is not allowed for the requested route", responseMessage);
    }
}

