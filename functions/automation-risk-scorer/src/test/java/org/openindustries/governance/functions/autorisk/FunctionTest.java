package org.openindustries.governance.functions.autorisk;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class FunctionTest {

    @Test
    void healthyIsNormal() {
        given().contentType("application/json")
                .body("{\"automationId\":\"au-1\",\"failureIndex\":4,\"privilegeIndex\":10,\"changeIndex\":7}")
                .when().post("/automationRiskScorer")
                .then().statusCode(200).body("decision", is("NORMAL")).body("score", is(0));
    }

    @Test
    void stackedSignalsAreCritical() {
        given().contentType("application/json")
                .body("{\"automationId\":\"au-2\",\"failureIndex\":88,\"privilegeIndex\":76,\"changeIndex\":64}")
                .when().post("/automationRiskScorer")
                .then().statusCode(200).body("decision", is("CRITICAL")).body("score", is(100));
    }
}
