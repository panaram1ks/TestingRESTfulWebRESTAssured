package com.appsdeveloperblog.UsersService.ui;

import com.appsdeveloperblog.UsersService.ui.model.User;
import com.appsdeveloperblog.UsersService.ui.model.UserRest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.internal.RequestSpecificationImpl;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
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

    @LocalServerPort
    private int port;

    private final RequestLoggingFilter requestLoggingFilter =
//            new RequestLoggingFilter();
                RequestLoggingFilter.with(LogDetail.BODY, LogDetail.HEADERS);

    private final ResponseLoggingFilter responseLoggingFilter = new ResponseLoggingFilter();

    @BeforeAll
    void setUp() {
        RestAssured.baseURI = "http://localhost";
//        RestAssured.port=8080;
        RestAssured.port = this.port;
//        RestAssured.filters(requestLoggingFilter, responseLoggingFilter);

        RestAssured.requestSpecification = new RequestSpecBuilder() // set global setting to request
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(requestLoggingFilter)
                .addFilter(responseLoggingFilter)
                .build();
    }

    @Order(1)
    @Test
    void testContainerIsRunning() {
        assertTrue(mysqlContainer.isRunning());
    }

    @Order(2)
    @Test
    void testCreateUser_whenValidDetailsProvided_returnsCreatedUser() {
//        // Arrange
//        Headers headers = new Headers(
//                new Header("Content-Type", "application/json"),
//                new Header("Accept", "application/json")
//        );
        User newUser = new User("fName", "lName", "test@emal.com", "12345678"); // first approach
        // second approach
//        Map<String, Object> newUser = new HashMap<>(); // if original User class in other project
//        newUser.put("firstName", "fName");
//        newUser.put("lastName", "lName");
//        newUser.put("email", "test@emal.com");
//        newUser.put("password", "12345678");
        // Act
        Response response = given()
                ////                .header("Content-Type", "application/json")
                //                .contentType(ContentType.JSON)
                ////                .header("Accept", "application/json")
                //                .accept(ContentType.JSON)
//                .headers(headers)
                .body(newUser)
                .when()
                .post("/users")
                .then()
                .extract()
//                            .as(UserRest.class);
                .response();
        // Assert
//        Assertions.assertEquals(newUser.getFirstName(), createdUser.getFirstName());
//        Assertions.assertEquals(newUser.getLastName(), createdUser.getLastName());
//        Assertions.assertEquals(newUser.getEmail(), createdUser.getEmail());
//        Assertions.assertNotNull(createdUser.getId());

        UserRest createdUser = response.as(UserRest.class);
        Assertions.assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        Assertions.assertEquals(newUser.getFirstName(), response.jsonPath().getString("firstName"));
        Assertions.assertEquals(newUser.getLastName(), createdUser.getLastName());
        Assertions.assertEquals(newUser.getEmail(), createdUser.getEmail());
        Assertions.assertNotNull(createdUser.getId());
    }

    @Order(2)
    @Test
    void testCreateUser_whenValidDetailsProvided_returnsCreatedUser_validateHttpResponse() {
//        // Arrange
//        Headers headers = new Headers(
//                new Header("Content-Type", "application/json"),
//                new Header("Accept", "application/json")
//        );
        User newUser = new User("fName2", "lName2", "test@emal2.com", "123456789"); // first approach

        // Act
        given()
//                .log().all() // log request info use requestLoggingFilter instead!
//                .headers(headers)
                .body(newUser)
        .when()
                .post("/users")
        .then()
                .log().all()
                .statusCode(201)
                .body("id", notNullValue())
                .body("firstName", equalTo(newUser.getFirstName()))
                .body("lastName", equalTo(newUser.getLastName()))
                .body("email", equalTo(newUser.getEmail()));

    }
}
