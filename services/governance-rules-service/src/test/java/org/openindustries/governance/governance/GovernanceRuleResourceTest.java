package org.openindustries.governance.governance;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class GovernanceRuleResourceTest {

    @Test
    void activateThenEvaluateAllowed() {
        String id = given().contentType("application/json")
                .body("{\"name\":\"medium-standard\",\"riskTier\":\"MEDIUM\",\"maxRunsPerMonth\":5000,\"maxFailurePct\":5,\"priority\":1}")
                .when().post("/api/v1/governance-rules")
                .then().statusCode(201).body("status", is("DRAFT"))
                .extract().path("id");

        given().when().post("/api/v1/governance-rules/" + id + "/activate")
                .then().statusCode(200).body("status", is("ACTIVE"));

        given().contentType("application/json")
                .body("{\"riskTier\":\"MEDIUM\",\"requiredMaxFailurePct\":8,\"requiredMaxRunsPerMonth\":3000}")
                .when().post("/api/v1/governance-rules/evaluate")
                .then().statusCode(200).body("decision", is("ALLOWED"));
    }

    @Test
    void evaluateWithoutRuleIsDenied() {
        given().contentType("application/json")
                .body("{\"riskTier\":\"CRITICAL\",\"requiredMaxFailurePct\":8,\"requiredMaxRunsPerMonth\":3000}")
                .when().post("/api/v1/governance-rules/evaluate")
                .then().statusCode(200).body("decision", is("DENIED"));
    }
}
