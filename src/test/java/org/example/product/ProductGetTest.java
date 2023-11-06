package org.example.product;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.example.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductGetTest extends BaseTest {

    @BeforeEach
    void init() {
        RestAssured.baseURI = getBaseURL();
    }

    @Test
    void getProductsSuccessful() {
        ValidatableResponse response = given()
                .contentType("application/json")
                .when()
                .get("/products")
                .then()
                .statusCode(200);
        List<Map<String, Object>> dataList = response.extract().jsonPath().getList("data");
        int numberOfRecords = dataList.size();
        assertEquals(9, numberOfRecords);
    }
}
