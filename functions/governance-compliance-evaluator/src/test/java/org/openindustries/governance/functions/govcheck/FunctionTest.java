package org.openindustries.governance.functions.govcheck;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class FunctionTest {

    @Test
    void compliant() {
        given().contentType("application/json")
                .body("{\"automationId\":\"au-1\",\"measuredOverdueReviews\":0,\"targetOverdueReviews\":0,\"measuredEvidenceCoveragePct\":100.0,\"targetEvidenceCoveragePct\":100.0}")
                .when().post("/governanceComplianceEvaluator")
                .then().statusCode(200).body("compliant", is(true)).body("findings.size()", is(0));
    }

    @Test
    void breachIsReported() {
        given().contentType("application/json")
                .body("{\"automationId\":\"au-2\",\"measuredOverdueReviews\":3,\"targetOverdueReviews\":0,\"measuredEvidenceCoveragePct\":100.0,\"targetEvidenceCoveragePct\":100.0}")
                .when().post("/governanceComplianceEvaluator")
                .then().statusCode(200).body("compliant", is(false)).body("findings.size()", is(1));
    }
}
