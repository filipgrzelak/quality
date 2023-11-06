package org.example.user;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.example.BaseTest;
import org.example.user.model.User;
import org.example.user.model.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDeleteTest extends BaseTest {

    @BeforeEach
    void init() {
        RestAssured.baseURI = getBaseURL();
    }

    @Test
    void DeleteUserUnsuccessfulByNotAuthorizationHeader() {
        User exampleUser = exampleIdPresentInDatabaseOfSpecificType();

        ValidatableResponse response = given()
                .body(exampleIdPresentInDatabaseOfSpecificType().id())
                .when()
                .delete("/users/%s".formatted(exampleUser.id()))
                .then()
                .statusCode(401);

        String responseMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Unauthorized", responseMessage);
    }

    @Test
    void DeleteUserUnsuccessfulByNotExistingUserId() {
        User exampleUser = exampleIdPresentInDatabaseOfSpecificType();

        ValidatableResponse response = given()
                .header("Authorization", adminToken())
                .body("4321")
                .when()
                .delete("/users/%s".formatted(exampleUser.id()))
                .then()
                .statusCode(409);

        String responseMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Seems like this customer is used elsewhere.", responseMessage);
    }

    @Test
    void DeleteUserSuccessfulByNewUserId() {
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

        String jsonUserData;
        try {
            jsonUserData = getObjectMapper().writeValueAsString(userData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert object to JSON");
        }

        ValidatableResponse postResponse = given()
                .header("Content-Type", "application/json")
                .body(jsonUserData)
                .when()
                .post("/users/register")
                .then()
                .statusCode(201);

        String createdUserId = postResponse.extract().body().jsonPath().getString("id");

        ValidatableResponse response = given()
                .header("Authorization", adminToken())
                .body(createdUserId)
                .when()
                .delete("/users/%s".formatted(createdUserId))
                .then()
                .statusCode(204);

        String resStatusCode = String.valueOf(response.extract().statusCode());
        assertEquals("204", resStatusCode);
    }

    public User exampleIdPresentInDatabaseOfSpecificType() {
        List<User> responseList = responseListOfSpecificTypeFromPage("/users", User.class);
        return responseList.get(0);
    }
}
