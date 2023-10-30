package org.example.contact;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.example.BaseTest;
import org.example.category.model.CategoryRequest;
import org.example.category.model.CategoryResponse;
import org.example.contact.model.ContactRequest;
import org.example.contact.model.ContactResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContactPostTest extends BaseTest {

    @BeforeEach
    void init() {
        RestAssured.baseURI = getBaseURL();
    }

    @Test
    void postMessageSuccessful() throws IOException {

        int numberOfRecords = numberOfRecordsAdmin("/messages");

        ContactRequest newContact = new ContactRequest("name", "last", "email@email.com", "subject", "test message");
        String jsonNewContact = getObjectMapper().writeValueAsString(newContact);

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .body(jsonNewContact)
                .when()
                .post("/messages")
                .then()
                .statusCode(201);

        int actualNumberOfRecords = numberOfRecordsAdmin("/messages");
        assertEquals(numberOfRecords + 1, actualNumberOfRecords);

        ContactRequest createdContact = response.extract().body().jsonPath().getObject(".", ContactRequest.class);
        assertEquals(newContact.message(), createdContact.message());
        assertEquals(newContact.subject(), createdContact.subject());
    }

    @Test
    void postMessageUnsuccessfulByBadMethod() throws IOException {

        int numberOfRecords = numberOfRecordsAdmin("/messages");

        ContactRequest newContact = new ContactRequest("name", "last", "email@email.com", "subject", "test message");
        String jsonNewContact = getObjectMapper().writeValueAsString(newContact);

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .body(jsonNewContact)
                .when()
                .delete("/messages")
                .then()
                .statusCode(405);

        int actualNumberOfRecords = numberOfRecordsAdmin("/messages");
        assertEquals(numberOfRecords, actualNumberOfRecords);

        String errorMessage = response.extract().body().jsonPath().getString("message");
        assertEquals("Method is not allowed for the requested route", errorMessage);
    }

}
