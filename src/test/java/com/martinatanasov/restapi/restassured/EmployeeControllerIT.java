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
    private static String token = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWxmIiwic3ViIjoidXNlciIsImV4cCI6MTc1MzYzMTgzNCwiaWF0IjoxNzUzNjI4MjM0LCJzY29wZSI6IlJFQURfQU5EX1dSSVRFIn0.BMoF9kq-9bEOILJNGCRyI5gEaABTlDLyaaNik1SXEE2SMnEAYR5ts2iLxUC4ad6nHNs7Qc1d39E8qlhLQg8W8i7yDqmvt9URr_fplfsZPin4cAF5uiC2Yf2QiYubjeTgnAMqtWhiTnBiz-afX-l3JNHPelKvs6ZKLq7mqJ_YPCMGs0orgAB3xU752C8Sz8cTmCTE5pxZPJxLq_X3uLemW2IwNU7rvUf12Sjhr1fST4s4Qm5Gji4zbyXzHLNUxAkzzdsuzrK2uV1BCOsjoOgmfyg_zkRjIw1JRPi1V-NzXZ8gF81zzCCAhobZZNUjz1PiZsenVUEOTutWT-WyUjc4TQ";

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
