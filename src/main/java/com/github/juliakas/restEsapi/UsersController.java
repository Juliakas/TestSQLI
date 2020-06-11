package com.github.juliakas.restEsapi;

import com.github.juliakas.DbContext;
import com.github.juliakas.IUsersController;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.OracleCodec;
import org.owasp.esapi.configuration.consts.EsapiConfiguration;
import org.owasp.esapi.filters.ESAPIFilter;
import org.owasp.esapi.reference.DefaultEncoder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Path("/usersEsapi")
public class UsersController implements IUsersController {
    @Path("/auth")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authenticate(@FormParam("email") String email, @FormParam("password") String password) throws SQLException {
        try (Connection con = DbContext.getConnection()) {
            OracleCodec oracleCodec = new OracleCodec();
            email = ESAPI.encoder().encodeForSQL(oracleCodec, email);
            password = ESAPI.encoder().encodeForSQL(oracleCodec, password);

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

    @Path("/table/{tableName}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response selectFromTable(@PathParam("tableName") String tableName) throws SQLException {
        OracleCodec oracleCodec = new OracleCodec();
        tableName = ESAPI.encoder().encodeForSQL(oracleCodec, tableName);
        try (Connection con = DbContext.getConnection()) {
            ResultSet rs = con.createStatement()
                    .executeQuery("SELECT * FROM " + tableName);
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
