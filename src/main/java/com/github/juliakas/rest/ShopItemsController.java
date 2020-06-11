package com.github.juliakas.rest;

import com.github.juliakas.DbContext;
import com.github.juliakas.IShopItemsController;
import com.github.juliakas.models.ShopItem;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Path("/items")
public class ShopItemsController implements IShopItemsController {
    @Override
    @Path("/buyer/{buyerId}")
    @GET
    @Produces("application/json")
    public Response getByBuyer(@PathParam("buyerId") String buyerId) throws SQLException {
        try (Connection con = DbContext.getConnection()) {
            ResultSet rs = con.createStatement()
                    .executeQuery("SELECT si.name, si.description, si.price " +
                            "FROM shop_items si WHERE si.bought_by = " + buyerId);
            List<ShopItem> items = new LinkedList<>();
            while (rs.next()) {
                ShopItem item = new ShopItem();
                item.setName(rs.getString("name"));
                item.setDescription(rs.getString("description"));
                item.setPrice(rs.getDouble("price"));
                items.add(item);
            }
            return Response.ok(items).build();
        }
    }

    @Override
    @Path("/buyer/{buyerId}/doubleDecode")
    @GET
    @Produces("application/json")
    public Response getByBuyerDoubleDecode(@PathParam("buyerId") String buyerId) throws SQLException {
        return getByBuyer(URLDecoder.decode(buyerId, StandardCharsets.UTF_8));
    }

    @Override
    @Path("/search/{name}")
    @GET
    @Produces("application/json")
    public Response searchByName(@PathParam("name") String name) throws SQLException {
        try (Connection con = DbContext.getConnection()) {
            ResultSet rs = con.createStatement()
                    .executeQuery("SELECT si.* FROM shop_items si WHERE si.NAME LIKE 'K_" + name + "' ESCAPE '/'");
            List<ShopItem> items = new LinkedList<>();
            while (rs.next()) {
                ShopItem item = new ShopItem();
                item.setItemId(rs.getLong("item_id"));
                item.setName(rs.getString("name"));
                item.setDescription(rs.getString("description"));
                item.setPrice(rs.getDouble("price"));
                items.add(item);
            }
            return Response.ok(items).build();
        }
    }
}
