package org.example;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BrandTest extends BaseTest {

    @Test
    void getBrands() throws IOException {
        given()
                .when()
                .get("/brands")
                .then()
                .statusCode(200);
    }

    @Test
    void postBrand() throws IOException {
        int numberOfRecords = numberOfRecords("/brands");

        Brand newBrand = new Brand("Kamilaa", "Bednarek");
        String jsonNewBrand = getObjectMapper().writeValueAsString(newBrand);

        given()
                .header("Authentication", bearer())
                .header("Content-Type", "application/json")
                .body(jsonNewBrand)
                .when()
                .post("/brands")
                .then()
                .statusCode(201);

        int actualNumberOfRecords = numberOfRecords("/brands");
        assertEquals(numberOfRecords + 1, actualNumberOfRecords);
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    record Brand(String name, String slug) {
    }
}
