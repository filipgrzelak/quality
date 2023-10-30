package org.example.category;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.example.BaseTest;
import org.example.category.model.CategoryRequest;
import org.example.category.model.CategoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryPostTest extends BaseTest {

    @BeforeEach
    void init() {
        RestAssured.baseURI = getBaseURL();
    }

    @Test
    void postCategoriesSuccessful() throws IOException {

        int numberOfRecords = numberOfRecords("/categories");

        CategoryRequest newCategory = new CategoryRequest("abc", "def");
        String jsonNewCategory = getObjectMapper().writeValueAsString(newCategory);

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .header("Authorization", adminToken())
                .body(jsonNewCategory)
                .when()
                .post("/categories")
                .then()
                .statusCode(201);

        int actualNumberOfRecords = numberOfRecords("/categories");
        assertEquals(numberOfRecords + 1, actualNumberOfRecords);

        CategoryResponse createdCategory = response.extract().body().jsonPath().getObject(".", CategoryResponse.class);
        assertEquals(newCategory.name(), createdCategory.name());
        assertEquals(newCategory.slug(), createdCategory.slug());

        deleteObject(createdCategory.id(), "/categories/%s");
    }

    @Test
    void postCategoriesUnsuccessfulByBadMethod() throws IOException {

        int numberOfRecords = numberOfRecords("/categories");

        CategoryRequest newCategory = new CategoryRequest("Filip", "Grzelak");
        String jsonNewCategory = getObjectMapper().writeValueAsString(newCategory);

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .header("Authorization", adminToken())
                .body(jsonNewCategory)
                .when()
                .delete("/categories")
                .then()
                .statusCode(405);

        int actualNumberOfRecords = numberOfRecords("/categories");
        assertEquals(numberOfRecords, actualNumberOfRecords);

        String errorMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Method is not allowed for the requested route", errorMessage);
    }

    @Test
    void postCategoriesUnsuccessfulByBadBodyFormat() {

        int numberOfRecords = numberOfRecords("/categories");

        String jsonNewBrand = "{category: newCategory, ksr: op}";

        given()
                .header("Content-Type", "application/json")
                .header("Authorization", adminToken())
                .body(jsonNewBrand)
                .when()
                .post("/categories")
                .then()
                .statusCode(422);

        int actualNumberOfRecords = numberOfRecords("/categories");
        assertEquals(numberOfRecords, actualNumberOfRecords);
    }

}
