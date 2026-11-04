package org.openindustries.governance.control;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class ControlItemResourceTest {

    @Test
    void createAndTransition() {
        String id = given().contentType("application/json")
                .body("{\"name\":\"Privileged Access Review\",\"controlType\":\"ACCESS\",\"reviewCycleDays\":90}")
                .when().post("/api/v1/controls")
                .then().statusCode(201)
                .body("status", is("ACTIVE"))
                .body("controlType", is("ACCESS"))
                .extract().path("id");

        given().when().post("/api/v1/controls/" + id + "/suspend")
                .then().statusCode(200).body("status", is("SUSPENDED"));
    }
}
