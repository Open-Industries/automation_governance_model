package org.openindustries.governance.budget;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@Path("/api/v1/execution-budgets")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Execution Budgets", description = "Guarded monthly execution budgets per automation and the run charges against them.")
public class BudgetResource {

    @Inject
    BudgetService service;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Open a executionbudget")
    public Response open(@Valid OpenExecutionBudgetRequest request) {
        return Response.status(Response.Status.CREATED).entity(service.open(request)).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get one by id")
    public ExecutionBudget get(@PathParam("id") UUID id) {
        return service.get(id);
    }

    @POST
    @Path("/{id}/charges")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Draw from the account (rejects if it would overdraw, 422)")
    public ExecutionCharge consume(@PathParam("id") UUID id, @Valid CreateExecutionChargeRequest request) {
        return service.consume(id, request);
    }

    @GET
    @Path("/{id}/charges")
    @Operation(summary = "List consumption records of an account")
    public List<ExecutionCharge> records(@PathParam("id") UUID id) {
        return service.recordsOf(id);
    }
}
