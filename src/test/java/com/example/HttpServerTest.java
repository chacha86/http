package com.example;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class HttpServerTest {
    @Test
    @DisplayName("GET / should return 200 and 'Hello, World!'")
    void t1() {
        RestAssured.baseURI = "http://localhost:8080";

        given()
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .header("Content-Type", "text/html")
                .body(equalTo("Hello, World!"));
    }
}
