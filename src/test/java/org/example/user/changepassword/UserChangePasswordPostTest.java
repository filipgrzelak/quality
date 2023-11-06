package org.example.user.changepassword;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.example.BaseTest;
import org.example.user.changepassword.model.UserChangePasswordRequest;
import org.example.user.register.model.UserRegisterResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserChangePasswordPostTest extends BaseTest {
    @BeforeEach
    void init() {
        RestAssured.baseURI = getBaseURL();
    }

    @Test
    void UserChangePasswordPostUnsuccessfulByNoAuthorization() {

        UserChangePasswordRequest userPasswords = new UserChangePasswordRequest("welcome01", "welcome01", "welcome01");
        String jsonData;

        try {
            jsonData = getObjectMapper().writeValueAsString(userPasswords);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        ValidatableResponse response = given()
                .body(jsonData)
                .when()
                .post("/users/change-password")
                .then()
                .statusCode(401);

        String responseMessage = response.extract().jsonPath().getString("message");
        assertEquals("Unauthorized", responseMessage);
    }

    @Test
    void UserChangePasswordPostSuccessful() {

        UserRegisterResponse exampleUser = createNewUser();

        UserChangePasswordRequest userPasswords = new UserChangePasswordRequest("welcome01", "welcome02", "welcome02");
        String jsonData;

        try {
            jsonData = getObjectMapper().writeValueAsString(userPasswords);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        ValidatableResponse response = given()
                .header("Authorization", exampleUserToken())
                .header("Content-Type", ContentType.JSON)
                .body(jsonData)
                .when()
                .post("/users/change-password")
                .then()
                .statusCode(200);

        String responseMessage = response.extract().jsonPath().getString("success");
        assertEquals("true", responseMessage);

        deleteObject(exampleUser.id(), "/users/%s");
    }
}
