package org.example.user.login;

import io.restassured.RestAssured;
import io.restassured.response.ResponseBody;
import io.restassured.response.ValidatableResponse;
import org.example.BaseTest;
import org.example.model.AuthLoginRequest;
import org.example.user.login.model.AuthBearer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class UserLoginPostTest extends BaseTest {

    @BeforeEach
    void init() {
        RestAssured.baseURI = getBaseURL();
    }

    @Test
    void userLoginSuccessful() {
        AuthLoginRequest authData = new AuthLoginRequest("customer@practicesoftwaretesting.com", "welcome01");
        String jsonAuthData;

        try {
            jsonAuthData = getObjectMapper().writeValueAsString(authData);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .body(jsonAuthData)
                .when()
                .post("/users/login")
                .then()
                .statusCode(200);

        AuthBearer bearer = response.extract().body().jsonPath().getObject(".", AuthBearer.class);

        assertNotNull(bearer.accessToken());
        assertNotNull(bearer.expiresIn());
        assertEquals("bearer", bearer.tokenType());
    }

    @Test
    void userLoginUnsuccessfulByInvalidCredentials() {
        AuthLoginRequest authData = new AuthLoginRequest("cos", "cos");
        String jsonAuthData;

        try {
            jsonAuthData = getObjectMapper().writeValueAsString(authData);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .body(jsonAuthData)
                .when()
                .post("/users/login")
                .then()
                .statusCode(401);

        String responseMessage = response.extract().jsonPath().getString("error");
        assertEquals("Unauthorized", responseMessage);
    }
}
