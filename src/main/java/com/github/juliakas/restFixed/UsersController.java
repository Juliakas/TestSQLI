package com.github.juliakas.restFixed;

import com.github.juliakas.DbContext;
import com.github.juliakas.IUsersController;
import oracle.jdbc.OracleTypes;
import oracle.sql.SQLUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.LinkedList;

@Path("/usersFixed")
public class UsersController implements IUsersController {
    @Path("/auth")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authenticate(@FormParam("email") String email, @FormParam("password") String password) throws SQLException {
        try (Connection con = DbContext.getConnection()) {
            PreparedStatement stmt = con.prepareStatement("SELECT u.username FROM users u WHERE u.email = ?" + "AND u.password = ?");
            stmt.setString(1, email);
            stmt.setString(1, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Response.ok("Vartotojas '"+ rs.getString(1) + "' prisijungė").build();
            } else {
                return Response.status(404).build();
            }
        }
    }

    @Path("/table/{tableName}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response selectFromTable(@PathParam("tableName") String tableName) throws SQLException {
        if (!(tableName.equals("users") || tableName.equals("shop_items"))) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try (Connection con = DbContext.getConnection()) {
            CallableStatement stmt = con.prepareCall(
                    "DECLARE " +
                    "  rc sys_refcursor; " +
                    "BEGIN " +
                    "  OPEN rc FOR 'SELECT * FROM ' || dbms_assert.enquote_name(?); " +
                    "  ? := rc; " +
                    "END;");
            stmt.setString(1, tableName);
            stmt.registerOutParameter(2, OracleTypes.CURSOR);
            stmt.execute();
            ResultSet rs = (ResultSet) stmt.getObject(2);

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
