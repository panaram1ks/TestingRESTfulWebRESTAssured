package com.appsdeveloperblog.UsersService.ui;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.http.HttpHeaders;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
@ActiveProfiles("test")
public class UsersControllerWithTestContainerITest {

    @Container // create, start and stop testcontainer during and after tests
    @ServiceConnection // configure application use database in testcontainer
    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:9.2.0");

    @Order(1)
    @Test
    void testContainerIsRunning() {
        assertTrue(mysqlContainer.isRunning());
    }

    @Order(2)
    @Test
    void testCreateUser_whenValidDetailsProvided_returnsCreatedUser() {
        // Arrange
        Headers headers = new Headers(
                new Header("Content-Type", "application/json"),
                new Header("Accept", "application/json")
        );
        // Act
        given()
////                .header("Content-Type", "application/json")
//                .contentType(ContentType.JSON)
////                .header("Accept", "application/json")
//                .accept(ContentType.JSON)
                .headers(headers)
                .when()
                .then();
        // Assert

    }
}
