package org.example.product;

import com.fasterxml.jackson.core.JsonProcessingException;
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

class ProductPutTest extends BaseTest {

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
    void updateProductByIdSuccessful() throws JsonProcessingException {
        String id = productResponse.id();

        ProductRequest updateProduct = new ProductRequest("Less heavy Hammer",
                "Less heavy hammer",
                2.99,
                "1",
                "1",
                1,
                1,
                0);

        String jsonUpdateProduct = getObjectMapper().writeValueAsString(updateProduct);

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .body(jsonUpdateProduct)
                .when()
                .put("/products/%s".formatted(id))
                .then()
                .statusCode(200);

        String responseMessage = response.extract().body().jsonPath().getString("success");
        assertEquals("true", responseMessage);
    }

}
