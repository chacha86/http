package com.example;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class HttpServerTest {

    private static final int PORT = 8080;
    private static HttpServer server;

    @BeforeAll
    static void beforeAll() {
        server = new HttpServer();
        new Thread(() -> server.start(PORT)).start();
        RestAssured.baseURI = "http://127.0.0.1:" + PORT;
    }

    @AfterAll
    static void afterAll() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    @DisplayName("GET / should return 200 and 'Hello, World!'")
    void t1() {
        given()
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .header("Content-Type", "text/html")
                .body(equalTo("Hello, World!"));
    }

    @Test
    @DisplayName("GET /about should return 200 and 'I am a body!'")
    void t2() {
        given()
                .when()
                .get("/about")
                .then()
                .statusCode(200)
                .header("Content-Type", "text/html")
                .body(equalTo("I am a body!"));
    }

    @Test
    @DisplayName("GET /api/v1/posts/1")
    void t3() {
        given()
                .when()
                .get("/api/v1/posts/1")
                .then()
                .statusCode(200)
                .header("Content-Type", "application/json")
                .body("id", equalTo(1))
                .body("title", equalTo("Post 1"))
                .body("content", equalTo("Content 1"));
    }
}
