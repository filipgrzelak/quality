package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ResponseBody;
import io.restassured.response.ValidatableResponse;
import org.example.model.AuthLoginRequest;
import org.example.user.model.UserRequest;
import org.example.user.register.model.UserRegisterResponse;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class BaseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseURL = "https://api.practicesoftwaretesting.com";

    public String adminToken() {
        return bearer("admin@practicesoftwaretesting.com", "welcome01");
    }

    public String customerToken() {
        return bearer("customer@practicesoftwaretesting.com", "welcome01");
    }

    public String invalidToken() {
        return "invalid";
    }

    public int numberOfRecords(String url) {
        ValidatableResponse response = given().when().get(url).then();
        response.statusCode(200);
        return response.extract().jsonPath().getList("$").size();
    }

    public int numberOfProductRecords(String url) {
        ValidatableResponse response = given().contentType("application/json").when().get(url).then();
        response.statusCode(200);
        List<Map<String, Object>> dataList = response.extract().jsonPath().getList("data");
        return dataList.size();
    }

    public int numberOfRecordsAdmin(String url) {
        ValidatableResponse response = given().header("Authorization", adminToken()).when().get(url).then();
        response.statusCode(200);
        return response.extract().jsonPath().getMap("$").size();
    }

    public <T> List<T> responseListOfSpecificType(String url, Class<T> clazz) {
        return given().header("Authorization", adminToken()).when().get(url).then().extract().body().jsonPath().getList(".", clazz);
    }

    public <T> List<T> responseListOfSpecificTypeFromPage(String url, Class<T> clazz) {
        return given().header("Authorization", adminToken()).when().get(url).then().extract().body().jsonPath().getList("data", clazz);
    }

    public void deleteObject(String id, String url) {
        given().header("Authorization", adminToken()).delete(url.formatted(id)).thenReturn();
    }

    public <T,R> R addObject(T t, String url, Class<R> clazz) {
        String jsonObject;
        try {
            jsonObject = getObjectMapper().writeValueAsString(t);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .body(jsonObject)
                .when()
                .post(url)
                .then();

        return response.extract().body().jsonPath().getObject(".",clazz);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public String getBaseURL() {
        return baseURL;
    }

    private String bearer(String email, String password) {
        AuthLoginRequest authData = new AuthLoginRequest(email, password);
        String jsonAuthData;

        try {
            jsonAuthData = objectMapper.writeValueAsString(authData);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        ResponseBody response = given()
                .header("Content-Type", "application/json")
                .body(jsonAuthData)
                .when()
                .post("/users/login")
                .getBody();

        return "Bearer " + response.jsonPath().get("access_token");
    }

    public UserRegisterResponse createNewUser() {
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
                "welcome01"
        );

        String jsonUserData;

        try {
            jsonUserData = getObjectMapper().writeValueAsString(userData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert object to JSON");
        }

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .body(jsonUserData)
                .when()
                .post("/users/register")
                .then()
                .statusCode(201);

        return response.extract().body().jsonPath().getObject(".", UserRegisterResponse.class);
    }

    public String exampleUserToken() {
        return bearer("jane1234@doe.example", "welcome01");
    }
}