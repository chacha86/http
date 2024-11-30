package com.example;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class HttpServerTest {
    private static final int PORT = 8080;

    @BeforeAll
    static void BeforeAll() {
        new Thread(() -> new HttpServer().start(PORT)).start();
    }

    @Test
    @DisplayName("GET / should return 200 and 'Hello, World!'")
    void t1() {
        RestAssured.baseURI = "http://127.0.0.1:" + PORT;

        given()
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .header("Content-Type", "text/html")
                .body(equalTo("Hello, World!"));
    }
}
