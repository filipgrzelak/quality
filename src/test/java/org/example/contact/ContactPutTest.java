package org.example.contact;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.example.BaseTest;
import org.example.category.model.CategoryRequest;
import org.example.category.model.CategoryResponse;
import org.example.contact.model.ContactRequest;
import org.example.contact.model.ContactResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContactPutTest extends BaseTest {

    ContactResponse contactResponse;

    @BeforeEach
    void init() {
        RestAssured.baseURI = getBaseURL();

        ContactRequest newContact = new ContactRequest("name", "last", "email@email.com", "subject", "test message");
        contactResponse = addObject(newContact, "/messages", ContactResponse.class);
    }

    @Test
    void updateContactByIdSuccessful() throws JsonProcessingException {
        String id = contactResponse.id();
        System.out.println(contactResponse);

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .header("Authorization", adminToken())
                .body("{\"status\": \"RESOLVED\"}")
                .when()
                .put("/messages/%s/status".formatted(id))
                .then()
                .statusCode(200);

        String responseMessage = response.extract().body().jsonPath().getString("success");
        assertEquals("true", responseMessage);
    }
}
