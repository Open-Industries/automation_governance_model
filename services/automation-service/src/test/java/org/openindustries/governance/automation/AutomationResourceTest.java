package org.openindustries.governance.automation;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class AutomationResourceTest {

    @Test
    void createAndFetch() {
        String id = given().contentType("application/json")
                .body("{\"name\":\"Nightly Report Publisher\",\"automationType\":\"WORKFLOW\",\"riskTier\":\"MEDIUM\"}")
                .when().post("/api/v1/automations")
                .then().statusCode(201)
                .body("status", is("PROPOSED"))
                .body("automationType", is("WORKFLOW"))
                .extract().path("id");

        given().when().get("/api/v1/automations/" + id)
                .then().statusCode(200).body("status", is("PROPOSED"));
    }

    @Test
    void transitionWithoutBody() {
        // Lifecycle transitions take no body and must not require a Content-Type (no 415).
        String id = given().contentType("application/json")
                .body("{\"name\":\"Nightly Report Publisher\",\"automationType\":\"WORKFLOW\",\"riskTier\":\"MEDIUM\"}")
                .when().post("/api/v1/automations")
                .then().statusCode(201)
                .extract().path("id");

        given().when().post("/api/v1/automations/" + id + "/approve")
                .then().statusCode(200).body("status", is("APPROVED"));
    }
}
