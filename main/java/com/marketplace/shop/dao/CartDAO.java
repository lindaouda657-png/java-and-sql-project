package com.marketplace.shop.dao;

import com.marketplace.shop.model.Cart;
import com.marketplace.shop.model.CartItem;
import com.marketplace.shop.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    public Cart getOrCreateCart(int customerId) throws SQLException {
        String sql = "SELECT * FROM cart WHERE customer_id_fk = ? ORDER BY created_at DESC LIMIT 1";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Cart c = new Cart();
                c.setCartId(rs.getInt("cart_id"));
                c.setCustomerIdFk(rs.getInt("customer_id_fk"));
                return c;
            }
        }

        String ins = "INSERT INTO cart (customer_id_fk) VALUES (?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(ins, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, customerId);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            Cart c = new Cart();
            c.setCustomerIdFk(customerId);
            if (keys.next()) c.setCartId(keys.getInt(1));
            return c;
        }
    }

    public List<CartItem> getCartItems(int cartId) throws SQLException {
        String sql = "SELECT ci.*, p.product_name, p.unit_price FROM cart_items ci " +
                     "JOIN products p ON ci.product_code_fk = p.product_code " +
                     "WHERE ci.cart_id_fk = ? ORDER BY ci.added_at ASC";
        List<CartItem> list = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CartItem item = new CartItem();
                item.setCartItemId(rs.getInt("cart_item_id"));
                item.setCartIdFk(rs.getInt("cart_id_fk"));
                item.setProductCodeFk(rs.getString("product_code_fk"));
                item.setQuantity(rs.getInt("quantity"));
                item.setProductName(rs.getString("product_name"));
                item.setUnitPrice(rs.getBigDecimal("unit_price"));
                list.add(item);
            }
        }
        return list;
    }

    public void addItem(int cartId, String productCode, int quantity) throws SQLException {

        String check = "SELECT cart_item_id, quantity FROM cart_items WHERE cart_id_fk=? AND product_code_fk=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(check)) {
            ps.setInt(1, cartId);
            ps.setString(2, productCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int newQty = rs.getInt("quantity") + quantity;
                int id = rs.getInt("cart_item_id");
                String upd = "UPDATE cart_items SET quantity=? WHERE cart_item_id=?";
                try (PreparedStatement ps2 = DatabaseConnection.getConnection().prepareStatement(upd)) {
                    ps2.setInt(1, newQty);
                    ps2.setInt(2, id);
                    ps2.executeUpdate();
                }
                return;
            }
        }

        String ins = "INSERT INTO cart_items (cart_id_fk, product_code_fk, quantity) VALUES (?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(ins)) {
            ps.setInt(1, cartId);
            ps.setString(2, productCode);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        }
    }

    public void updateItemQuantity(int cartItemId, int quantity) throws SQLException {
        String sql = "UPDATE cart_items SET quantity=? WHERE cart_item_id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, cartItemId);
            ps.executeUpdate();
        }
    }

    public void removeItem(int cartItemId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE cart_item_id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, cartItemId);
            ps.executeUpdate();
        }
    }

    public void clearCart(int cartId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE cart_id_fk=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.executeUpdate();
        }
    }

    public int countItems(int cartId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(quantity),0) FROM cart_items WHERE cart_id_fk=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }
}
