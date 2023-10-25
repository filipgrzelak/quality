package org.example;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.response.ValidatableResponse;
import org.apache.groovy.json.internal.IO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private static ObjectMapper objectMapper;
    private static String token;

    @BeforeAll
    static void init() throws IOException {
        objectMapper = new ObjectMapper();
        baseURI = "https://api.practicesoftwaretesting.com";
        token = bearer();
    }

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
        String jsonNewBrand = objectMapper.writeValueAsString(newBrand);

        given()
                .header("Authentication", token)
                .header("Content-Type", "application/json")
                .body(jsonNewBrand)
                .when()
                .post("/brands")
                .then()
                .statusCode(201);

        int actualNumberOfRecords = numberOfRecords("/brands");
        assertEquals(numberOfRecords + 1, actualNumberOfRecords);
    }

    static String bearer() throws IOException {
        Auth authData = new Auth("customer@practicesoftwaretesting.com", "welcome01");
        String jsonAuthData = objectMapper.writeValueAsString(authData);
        ResponseBody response = given()
                .header("Content-Type", "application/json")
                .body(jsonAuthData)
                .when()
                .post("/users/login")
                .getBody();

        return "Bearer " + response.jsonPath().get("access_token");
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    record Auth(String email, String password) {

    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    record Brand(String name, String slug) {
    }

    int numberOfRecords(String url) {
        ValidatableResponse response = given().when().get(url).then();
        response.statusCode(200);
        return response.extract().jsonPath().getList("$").size();
    }
}