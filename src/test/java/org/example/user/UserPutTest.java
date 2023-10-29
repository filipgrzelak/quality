package org.example.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.example.BaseTest;
import org.example.brand.model.BrandRequest;
import org.example.brand.model.BrandResponse;
import org.example.user.model.User;
import org.example.user.model.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserPutTest extends BaseTest {

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

    public User exampleIdPresentInDatabaseOfSpecificType() {
        List<User> responseList = responseListOfSpecificTypeFromPage("/users", User.class);
        return responseList.get(0);
    }
}
