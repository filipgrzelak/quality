package org.example.product;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.example.BaseTest;
import org.example.product.model.ProductRequest;
import org.example.product.model.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductPostTest extends BaseTest {
    @BeforeEach
    void init() {
        RestAssured.baseURI = getBaseURL();
    }

    @Test
    void postProductsSuccessful() throws IOException {

        int numberOfRecords = numberOfProductRecords("/products");

        ProductRequest newProduct = new ProductRequest(
                "Product Name",
                "Product Description",
                1.99,
                "1",
                "1",
                1,
                1,
                0
        );

        String jsonNewProduct = getObjectMapper().writeValueAsString(newProduct);

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .body(jsonNewProduct)
                .when()
                .post("/products")
                .then()
                .statusCode(201);

        int actualNumberOfRecords = numberOfProductRecords("/products");
        assertEquals(numberOfRecords + 1, actualNumberOfRecords);

        ProductResponse createdProduct = response.extract().body().jsonPath().getObject(".", ProductResponse.class);
        assertEquals(newProduct.name(), createdProduct.name());
        assertEquals(newProduct.description(), createdProduct.description());

        deleteObject(createdProduct.id(), "/products/%s");
    }
}
