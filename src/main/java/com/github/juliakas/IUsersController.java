package com.github.juliakas;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/users")
public interface IUsersController {
    @Path("/auth")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    Response authenticate(@FormParam("email") String email, @FormParam("password") String password) throws SQLException;

    @Path("/table/{tableName}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response selectFromTable(@PathParam("tableName") String tableName) throws SQLException;
}
