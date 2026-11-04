package org.openindustries.governance.automation;

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

@Path("/api/v1/automations")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Automations", description = "Registry of governed automations with risk tiers and approval lifecycle.")
public class AutomationResource {

    @Inject
    AutomationService service;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a automation")
    public Response create(@Valid CreateAutomationRequest request) {
        return Response.status(Response.Status.CREATED).entity(service.create(request)).build();
    }

    @GET
    @Operation(summary = "List all")
    public List<Automation> list() {
        return service.list();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get one by id")
    public Automation get(@PathParam("id") UUID id) {
        return service.get(id);
    }

    @POST
    @Path("/{id}/approve")
    @Operation(summary = "Approve this automation (lifecycle transition, no body)")
    public Automation approve(@PathParam("id") UUID id) {
        return service.approve(id);
    }

    @POST
    @Path("/{id}/retire")
    @Operation(summary = "Retire this automation (lifecycle transition, no body)")
    public Automation retire(@PathParam("id") UUID id) {
        return service.retire(id);
    }
}
