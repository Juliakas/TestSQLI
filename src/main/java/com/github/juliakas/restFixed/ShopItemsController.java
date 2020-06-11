package com.github.juliakas.restFixed;

import com.github.juliakas.DbContext;
import com.github.juliakas.IShopItemsController;
import com.github.juliakas.models.ShopItem;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Path("/itemsFixed")
public class ShopItemsController implements IShopItemsController {
    @Path("/buyer/{buyerId}")
    @GET
    @Produces("application/json")
    public Response getByBuyer(@PathParam("buyerId") String buyerId) throws SQLException {
        try (Connection con = DbContext.getConnection()) {
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT si.name, si.description, si.price " +
                    "FROM shop_items si WHERE si.bought_by = ?");
            stmt.setString(1, buyerId);
            ResultSet rs = stmt.executeQuery();
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
            name = name.replace("/", "//")
                    .replace("%", "/%")
                    .replace("_", "/_")
                    .replace("[", "/[");
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT si.* FROM shop_items si WHERE si.NAME LIKE 'K_' || ? ESCAPE '/'");
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
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
