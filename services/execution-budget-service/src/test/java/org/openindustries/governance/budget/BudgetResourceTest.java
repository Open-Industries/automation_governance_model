package org.openindustries.governance.budget;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class BudgetResourceTest {

    @Test
    void consumptionDrawsDown() {
        String id = given().contentType("application/json")
                .body("{\"automationId\":\"11111111-1111-1111-1111-111111111111\",\"monthlyRuns\":3000}")
                .when().post("/api/v1/execution-budgets")
                .then().statusCode(201).body("monthlyRuns", is(3000))
                .extract().path("id");

        given().contentType("application/json")
                .body("{\"runType\":\"SCHEDULED\",\"runs\":400}")
                .when().post("/api/v1/execution-budgets/" + id + "/charges")
                .then().statusCode(200)
                .body("remainingAfter", is(2600));
    }

    @Test
    void overdrawIsRejected() {
        String id = given().contentType("application/json")
                .body("{\"automationId\":\"11111111-1111-1111-1111-111111111111\",\"monthlyRuns\":3000}")
                .when().post("/api/v1/execution-budgets")
                .then().statusCode(201)
                .extract().path("id");

        given().contentType("application/json")
                .body("{\"runType\":\"SCHEDULED\",\"runs\":9000}")
                .when().post("/api/v1/execution-budgets/" + id + "/charges")
                .then().statusCode(422).body("status", is(422));

        given().when().get("/api/v1/execution-budgets/" + id)
                .then().statusCode(200).body("monthlyRuns", is(3000));
    }
}
