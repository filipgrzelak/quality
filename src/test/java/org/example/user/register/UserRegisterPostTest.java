package org.example.user.register;

import org.example.BaseTest;
import org.example.user.model.UserRequest;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.example.user.register.model.UserRegisterResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserRegisterPostTest extends BaseTest {

    @BeforeEach
    void init() {
        RestAssured.baseURI = getBaseURL();
    }

    @Test
    void userRegistrationSuccessful() {

        UserRequest userData = new UserRequest(
                "John",
                "Doe",
                "Street 1",
                "City",
                "State",
                "Country",
                "1234AA",
                "0987654321",
                "1970-01-01",
                "jane1234@doe.example",
                "super-secret"
        );

        String jsonUserData = convertObjectToJson(userData);

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .body(jsonUserData)
                .when()
                .post("/users/register")
                .then()
                .statusCode(201);

        UserRegisterResponse createdUser = response.extract().body().jsonPath().getObject(".", UserRegisterResponse.class);

        deleteObject(createdUser.id(), "/users/%s");
    }

    @Test
    void userRegistrationUnsuccessfulAlreadyExist() {

        UserRequest userData = new UserRequest(
                "John",
                "Doe",
                "Street 1",
                "City",
                "State",
                "Country",
                "1234AA",
                "0987654321",
                "1970-01-01",
                "customer2@practicesoftwaretesting.com",
                "super-secret"
        );

        String jsonUserData = convertObjectToJson(userData);

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .body(jsonUserData)
                .when()
                .post("/users/register")
                .then()
                .statusCode(422);

        String responseMessage = response.extract().jsonPath().getString("email");
        assertEquals("[A customer with this email address already exists.]", responseMessage);
    }

    private String convertObjectToJson(Object object) {
        try {
            return getObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert object to JSON");
        }
    }
}
