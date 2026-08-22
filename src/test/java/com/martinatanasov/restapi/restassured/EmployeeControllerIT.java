package com.martinatanasov.restapi.restassured;


import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeControllerIT {

    private static int createdEmployeeId;
    private static String token = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoiYWJ2QGFidi5iZyIsImV4cCI6MTc4NzQwOTY0OSwiaWF0IjoxNzg3NDA2MDQ5LCJzY29wZSI6IlJFQURfQU5EX1dSSVRFIEZBQ1RPUl9QQVNTV09SRCJ9.Pn3EdC2r1TzFlAZd1zWhrm4Tviq7DB14bqOM53RO-4XNjLDMIgPIiuhfwg_Eo_9Dgp5bwGPZJK-a0vI8UmPSMETrxxjUtN1SrsgCGhwBuJhuEGahvdQurLqipYEz7O3r-CWrrxAF9KgV5c3gRR58btTEw_OgEaOgK06KgNWuaMepC_qv8fMz3BQio9fXNto3d1gWGLytHr-X9qd-5IyNtjaEeLdvVCcMGDGB2Byj_7bEdLpsSfZkzdaSaaqyw9_Gmxqs4SIIKXPgXdDPXK5GNd7RYtWfGMZVxg5Y5TYVj67W5yC7Q5O4GJmHKwhA4SpSov-Z540TlXwjrINbisBabw";

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/api/v1/employees";

        //Setup JWT token authorization
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "Bearer " + token)
                .build();
    }

    @Test
    @Order(1)
    void testCreateEmployee() {
        final long timestamp = System.currentTimeMillis();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(getRandomEmployeeData(timestamp))
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("firstName", equalTo("Jane"))
                .body("lastName", equalTo("Smith"))
                .body("email", equalTo("jane" + timestamp + ".smith@example.com"))
                .extract().response();

        createdEmployeeId = response.path("id");
    }

    @Test
    @Order(2)
    void testGetAllEmployees() {
        when()
                .get()
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    @Order(3)
    void testGetEmployeeById() {
        given()
                .pathParam("id", createdEmployeeId)
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(createdEmployeeId));
    }

    @Test
    @Order(4)
    void testUpdateEmployee() {
        String updatedJson = """
                {
                    "firstName": "Janet",
                    "lastName": "Smith",
                    "email": "janet.smith@example.com"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", createdEmployeeId)
                .body(updatedJson)
                .when()
                .put("/{id}")
                .then()
                .statusCode(200)
                .body("firstName", equalTo("Janet"))
                .body("email", equalTo("janet.smith@example.com"));
    }

    @Test
    @Order(5)
    void testDeleteEmployee() {
        given()
                .pathParam("id", createdEmployeeId)
                .when()
                .delete("/{id}")
                .then()
                .statusCode(204); // No Content
    }

    @Test
    @Order(6)
    void testGetDeletedEmployeeNotFound() {
        given()
                .pathParam("id", createdEmployeeId)
                .when()
                .get("/{id}")
                .then()
                .statusCode(404);
    }

    private String getRandomEmployeeData(final long timestamp) {
        StringBuilder jsonValue = new StringBuilder();
        jsonValue.append("{\"firstName\": \"Jane\",");
        jsonValue.append("\"lastName\": \"Smith\",");
        jsonValue.append("\"email\": \"jane");
        jsonValue.append(timestamp);
        jsonValue.append(".smith@example.com\"}");
        return jsonValue.toString();
    }

}
