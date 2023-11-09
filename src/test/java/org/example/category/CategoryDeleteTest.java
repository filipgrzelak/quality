package org.example.category;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.example.BaseTest;
import org.example.category.model.CategoryRequest;
import org.example.category.model.CategoryResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryDeleteTest extends BaseTest {

    CategoryResponse categoryResponse;

    @BeforeEach
    void init() {
        RestAssured.baseURI = getBaseURL();

        CategoryRequest newCategory = new CategoryRequest("abc", "def");
        categoryResponse = addObject(newCategory, "/categories", CategoryResponse.class);
    }

    @AfterEach
    void cleanup() {
        deleteObject(categoryResponse.id(), "/categories/%s");
    }

    @Test
    void deleteCategoryByIdSuccessful() {

        String id = categoryResponse.id();

        ValidatableResponse response = given()
                .header("Authorization", adminToken())
                .when()
                .delete("/categories/%s".formatted(id))
                .then()
                .statusCode(204);
    }

    @Test
    void deleteCategoryByIdUnsuccessfulByNotAdminToken() {

        String id = categoryResponse.id();

        ValidatableResponse response = given()
                .header("Authorization", customerToken())
                .when()
                .delete("/categories/%s".formatted(id))
                .then()
                .statusCode(403);

        String responseMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Forbidden", responseMessage);
    }

    @Test
    void deleteCategoryByIdUnsuccessfulByNoToken() {

        String id = categoryResponse.id();

        ValidatableResponse response = given()
                .when()
                .delete("/categories/%s".formatted(id))
                .then()
                .statusCode(401);

        String responseMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Unauthorized", responseMessage);
    }

    @Test
    void deleteCategoryByIdUnsuccessfulByInvalidId() {

        ValidatableResponse response = given()
                .header("Authorization", adminToken())
                .when()
                .delete("/categories/%s".formatted("abcdef"))
                .then()
                .statusCode(404);

        String responseMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Resource not found", responseMessage);
    }

    @Test
    void deleteCategoryByIdUnsuccessfulByInvalidMethod() {

        ValidatableResponse response = given()
                .header("Authorization", adminToken())
                .when()
                .post("/categories/%s".formatted("id"))
                .then()
                .statusCode(405);

        String responseMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Method is not allowed for the requested route", responseMessage);
    }
}
