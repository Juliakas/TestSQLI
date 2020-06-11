package com.github.juliakas;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/items")
public interface IShopItemsController {
    @Path("/buyer/{buyerId}")
    @GET
    @Produces("application/json")
    Response getByBuyer(@PathParam("buyerId") String buyerId) throws SQLException;

    @Path("/buyer/{buyerId}/doubleDecode")
    @GET
    @Produces("application/json")
    Response getByBuyerDoubleDecode(@PathParam("buyerId") String buyerId) throws SQLException;

    @Path("/search/{name}")
    @GET
    @Produces("application/json")
    Response searchByName(@PathParam("name") String name) throws SQLException;
}
