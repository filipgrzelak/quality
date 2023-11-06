package org.example.user.forgotpassword;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.example.BaseTest;
import org.example.user.forgotpassword.model.UserEmailRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserForgotPasswordPostTest extends BaseTest {

    @BeforeEach
    void init() {
        RestAssured.baseURI = getBaseURL();
    }

    @Test
    void postUserForgotPasswordSuccessful() {

        UserEmailRequest userEmail = new UserEmailRequest("customer@practicesoftwaretesting.com");
        String jsonData;

        try {
            jsonData = getObjectMapper().writeValueAsString(userEmail);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .body(jsonData)
                .when()
                .post("/users/forgot-password")
                .then()
                .statusCode(200);

        String responseMessage = response.extract().body().jsonPath().getString("success");
        assertEquals("true", responseMessage);
    }

    @Test
    void postUserForgotPasswordUnsuccessfulByEmailNotExist() {

        UserEmailRequest userEmail = new UserEmailRequest("jane1@doe.example");
        String jsonData;

        try {
            jsonData = getObjectMapper().writeValueAsString(userEmail);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .body(jsonData)
                .when()
                .post("/users/forgot-password")
                .then()
                .statusCode(302);

        String locationHeader = response.extract().header("Location");

        assertEquals("https://api.practicesoftwaretesting.com", locationHeader);
    }

}
