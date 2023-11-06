package org.example.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.example.BaseTest;
import org.example.user.model.User;
import org.example.user.model.UserRequest;
import org.example.user.register.model.UserRegisterResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserPutTest extends BaseTest {

    @BeforeEach
    void init() {
        RestAssured.baseURI = getBaseURL();
    }

    @Test
    void updateUserUnsuccessfulByNotAuthorizationHeader() throws JsonProcessingException {
        User exampleUser = exampleIdPresentInDatabaseOfSpecificType();

        UserRequest updateUser = new UserRequest("John",
                "Doe",
                "Street 1",
                "City",
                "State",
                "Country",
                "123AA",
                "0987654321",
                "1970-01-01",
                "john@doe.example",
                "super-secret"
        );
        String jsonUpdateUser = getObjectMapper().writeValueAsString(updateUser);

        ValidatableResponse response = given()
                .body(jsonUpdateUser)
                .when()
                .put("/users/%s".formatted(exampleUser.id()))
                .then()
                .statusCode(401);

        String responseMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Unauthorized", responseMessage);
    }

    @Test
    void updateUserSuccessful() {

        UserRegisterResponse newUser = createNewUser();

        UserRequest updatedUserData = new UserRequest(
                "EditedFirst",
                "EditedLast",
                "EditedAddress",
                "EditedCity",
                "EditedState",
                "EditedCountry",
                "EditedPost",
                "123321123",
                "1970-01-01",
                "jane1234@doe.example",
                "welcome01"
        );

        String updatedJsonUserData;
        try {
            updatedJsonUserData = getObjectMapper().writeValueAsString(updatedUserData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert object to JSON");
        }

        ValidatableResponse response = given()
                .header("Authorization", adminToken())
                .header("Content-Type", ContentType.JSON)
                .body(updatedJsonUserData)
                .when()
                .put("/users/%s".formatted(newUser.id()))
                .then()
                .statusCode(200);

        String responseMessage = response.extract().body().jsonPath().getString("success");
        assertEquals("true", responseMessage);

        deleteObject(newUser.id(), "/users/%s");
    }

    public User exampleIdPresentInDatabaseOfSpecificType() {
        List<User> responseList = responseListOfSpecificTypeFromPage("/users", User.class);
        return responseList.get(0);
    }
}
