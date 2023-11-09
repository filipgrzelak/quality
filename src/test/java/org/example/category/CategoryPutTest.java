package org.example.category;

import com.fasterxml.jackson.core.JsonProcessingException;
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

public class CategoryPutTest extends BaseTest {

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
    void updateCategoryByIdSuccessful() throws JsonProcessingException {
        String id = categoryResponse.id();

        CategoryRequest updateCategory = new CategoryRequest("abcc", "deff");
        String jsonUpdateCategory = getObjectMapper().writeValueAsString(updateCategory);

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .body(jsonUpdateCategory)
                .when()
                .put("/categories/%s".formatted(id))
                .then()
                .statusCode(200);

        String responseMessage = response.extract().body().jsonPath().getString("success");
        assertEquals("true", responseMessage);
    }

    @Test
    void updateCategoryByIdUnsuccessfulByBadMethod() throws JsonProcessingException {

        String id = categoryResponse.id();

        CategoryRequest updateCategory = new CategoryRequest("abcc", "deff");
        String jsonUpdateCategory = getObjectMapper().writeValueAsString(updateCategory);

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .body(jsonUpdateCategory)
                .when()
                .post("/categories/%s".formatted(id))
                .then()
                .statusCode(405);

        String responseMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Method is not allowed for the requested route", responseMessage);
    }

    @Test
    void updateCategoryByIdUnsuccessfulByInvalidId() throws JsonProcessingException {

        CategoryRequest updateCategory = new CategoryRequest("abcc", "deff");
        String jsonUpdateCategory = getObjectMapper().writeValueAsString(updateCategory);

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .body(jsonUpdateCategory)
                .when()
                .put("/categories/%s".formatted("abcdef"))
                .then()
                .statusCode(404);

        String responseMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Resource not found", responseMessage);
    }

    @Test
    void updateCategoryByIdUnsuccessfulByBadBodyFormat() {

        String jsonUpdateCategory = "{abc: abcdef}";

        given()
                .header("Content-Type", "application/json")
                .body(jsonUpdateCategory)
                .when()
                .put("/categories/%s".formatted("abcdef"))
                .then()
                .statusCode(422);
    }

}
