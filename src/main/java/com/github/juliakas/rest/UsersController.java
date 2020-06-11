package com.github.juliakas.rest;

import com.github.juliakas.DbContext;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Path("/users")
public class UsersController implements com.github.juliakas.IUsersController {
    @Override
    @Path("/auth")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authenticate(@FormParam("email") String email, @FormParam("password") String password) throws SQLException {
        try (Connection con = DbContext.getConnection()) {
            ResultSet rs = con.createStatement()
                    .executeQuery("SELECT u.username FROM users u WHERE u.email = '"
                            + email + "' AND u.password = '" + password + "'");
            if (rs.next()) {
                return Response.ok("Vartotojas '"+ rs.getString(1) + "' prisijungė").build();
            } else {
                return Response.status(404).build();
            }
        }
    }

    @Override
    @Path("/table/{tableName}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response selectFromTable(@PathParam("tableName") String tableName) throws SQLException {
        try (Connection con = DbContext.getConnection()) {
            String sql = String.format("SELECT * FROM %s", tableName);
            ResultSet rs = con.createStatement()
                    .executeQuery(sql);
            LinkedList<LinkedList<String>> result = new LinkedList<>();
            while (rs.next()) {
                result.add(new LinkedList<>());
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    result.getLast().add(rs.getString(i));
                }
            }
            return Response.ok(result).build();
        }
    }
}
