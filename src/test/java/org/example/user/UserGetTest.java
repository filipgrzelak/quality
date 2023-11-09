package org.example.user;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.example.BaseTest;
import org.example.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserGetTest extends BaseTest {

    @BeforeEach
    void init() {
        RestAssured.baseURI = getBaseURL();
    }

    @Test
    void getUsersSuccessful() {
        ValidatableResponse response = given()
                .header("Authorization", adminToken())
                .when()
                .get("/users")
                .then()
                .statusCode(200);
        List<Map<String, Object>> dataList = response.extract().jsonPath().getList("data");
        int numberOfRecords = dataList.size();
        assertEquals(2, numberOfRecords);
    }

    @Test
    void getUsersUnsuccessfulByNoAuthorizationHeader() {
        ValidatableResponse response = given()
                .when()
                .get("/users")
                .then()
                .statusCode(401);

        String responseMessage = response.extract().jsonPath().getString("message");
        assertEquals("Unauthorized", responseMessage);
    }

    @Test
    void getUsersUnsuccessfulByResourceNotFound() {
        ValidatableResponse response = given()
                .when()
                .get("/userss")
                .then()
                .statusCode(404);

        String responseMessage = response.extract().jsonPath().getString("message");
        assertEquals("Resource not found", responseMessage);
    }

    @Test
    void getSpecificUserSuccessfulByUserId() {
        User exampleUser = exampleIdPresentInDatabaseOfSpecificType();

        ValidatableResponse response = given()
                .header("Authorization", adminToken())
                .when()
                .get("/users/%s".formatted(exampleUser.id()))
                .then()
                .statusCode(200);

        User userResponse = response.extract().body().jsonPath().getObject(".", User.class);
        assertEquals(exampleUser, userResponse);
    }

    @Test
    void getSpecificUserUnsuccessfulByUserIdNotExist() {

        ValidatableResponse response = given()
                .header("Authorization", adminToken())
                .when()
                .get("/users/%s".formatted("123"))
                .then()
                .statusCode(404);

        String responseMessage = response.extract().jsonPath().getString("message");
        assertEquals("Requested item not found", responseMessage);
    }

    @Test
    void getSpecificUserUnsuccessfulByUserNotAuthenticated() {
        User exampleUser = exampleIdPresentInDatabaseOfSpecificType();

        ValidatableResponse response = given()
                .when()
                .get("/users/%s".formatted(exampleUser.id()))
                .then()
                .statusCode(401);

        String responseMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Unauthorized", responseMessage);
    }

    public User exampleIdPresentInDatabaseOfSpecificType() {
        List<User> responseList = responseListOfSpecificTypeFromPage("/users", User.class);
        return responseList.get(0);
    }
}
