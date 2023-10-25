package org.example;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ResponseBody;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

class BaseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void init() throws IOException {
        baseURI = "https://api.practicesoftwaretesting.com";
    }

    public String bearer() throws IOException {
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

    int numberOfRecords(String url) {
        ValidatableResponse response = given().when().get(url).then();
        response.statusCode(200);
        return response.extract().jsonPath().getList("$").size();
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    record Auth(String email, String password) {

    }
}