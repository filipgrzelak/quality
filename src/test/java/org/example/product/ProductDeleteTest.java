package org.example.product;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.example.BaseTest;
import org.example.product.model.ProductRequest;
import org.example.product.model.ProductResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductDeleteTest extends BaseTest {

    ProductResponse productResponse;

    @BeforeEach
    void init() {
        RestAssured.baseURI = getBaseURL();

        ProductRequest newProduct = new ProductRequest(
                "Heavy Hammer",
                "Very heavy hammer, one of a kind",
                4.99,
                "1",
                "1",
                1,
                1,
                0
        );
        productResponse = addObject(newProduct, "/products", ProductResponse.class);
    }

    @AfterEach
    void cleanup() {
        deleteObject(productResponse.id(), "/products/%s");
    }

    @Test
    void deleteProductByIdSuccessful() {

        String id = productResponse.id();

        ValidatableResponse response = given()
                .header("Authorization", adminToken())
                .when()
                .delete("/products/%s".formatted(id))
                .then()
                .statusCode(204);
    }

    @Test
    void deleteProductByIdUnsuccessfulByNotAuthorization() {

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .when()
                .delete("/products/%s".formatted("1234"))
                .then()
                .statusCode(401);

        String responseMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Unauthorized", responseMessage);
    }

}
