package org.example.category;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.example.BaseTest;
import org.example.brand.model.BrandResponse;
import org.example.category.model.CategoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryGetTest extends BaseTest {

    @BeforeEach
    void init() {
        RestAssured.baseURI = getBaseURL();
    }

    @Test
    void getCategoriesSuccessful() {
        ValidatableResponse response = given()
                .when()
                .get("/categories")
                .then()
                .statusCode(200);
        int numberOfRecords = response.extract().jsonPath().getList("$").size();
        assertEquals(12, numberOfRecords);
    }

    @Test
    void getCategoriesBadMethod() {
        ValidatableResponse response = given()
                .when()
                .delete("/categories")
                .then()
                .statusCode(405);
        String errorMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Method is not allowed for the requested route", errorMessage);
    }

    @Test
    void getCategoriesByIDSuccessful() {
        CategoryResponse categoryFromDatabase = exampleIdPresentInDatabaseOfSpecificType();
        ValidatableResponse response = given()
                .when()
                .get("/categories/%s".formatted(categoryFromDatabase.id()))
                .then()
                .statusCode(200);

        CategoryResponse categoryResponse = response.extract().body().jsonPath().getObject(".", CategoryResponse.class);
        assertEquals(categoryFromDatabase, categoryResponse);
    }

    @Test
    void getCategoriesByIDBadMethod() {
        ValidatableResponse response = given()
                .when()
                .delete("/categories/%s".formatted("badId"))
                .then()
                .statusCode(401);
        String errorMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Method is not allowed for the requested route", errorMessage);
    }

    @Test
    void getCategoriesIDNotFound() {
        ValidatableResponse response = given()
                .when()
                .get("/categories/%s".formatted("badId"))
                .then()
                .statusCode(404);
        String errorMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Requested item not found", errorMessage);
    }

    public CategoryResponse exampleIdPresentInDatabaseOfSpecificType() {
        List<CategoryResponse> responseList = responseListOfSpecificType("/categories", CategoryResponse.class);
        return responseList.get(0);
    }
}


