package com.marketplace.shop.dao;

import com.marketplace.shop.model.Order;
import com.marketplace.shop.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO implements GenericDAO<Order> {

    private static final String SELECT_BASE =
            "SELECT o.*, c.customer_name, u.user_name, os.state FROM orders o " +
            "JOIN customers c ON o.customer_id_fk = c.customer_id " +
            "JOIN users u ON o.user_id_fk = u.user_id " +
            "JOIN order_statuse os ON o.order_statuse_id_fk = os.order_statuse_id ";

    @Override
    public void insert(Order o) throws SQLException {
        String sql = "INSERT INTO orders (customer_id_fk, user_id_fk, order_statuse_id_fk) VALUES (?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, o.getCustomerIdFk());
            ps.setInt(2, o.getUserIdFk());
            ps.setInt(3, o.getOrderStatuseIdFk());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) o.setOrderId(keys.getInt(1));
        }
    }

    @Override
    public void update(Order o) throws SQLException {
        String sql = "UPDATE orders SET customer_id_fk=?, user_id_fk=?, order_statuse_id_fk=? WHERE order_id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, o.getCustomerIdFk());
            ps.setInt(2, o.getUserIdFk());
            ps.setInt(3, o.getOrderStatuseIdFk());
            ps.setInt(4, o.getOrderId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Object id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("DELETE FROM orders WHERE order_id=?")) {
            ps.setInt(1, (Integer) id);
            ps.executeUpdate();
        }
    }

    @Override
    public Order findById(Object id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(SELECT_BASE + "WHERE o.order_id=?")) {
            ps.setInt(1, (Integer) id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    @Override
    public List<Order> findAll() throws SQLException {
        List<Order> list = new ArrayList<>();
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(SELECT_BASE + "ORDER BY o.order_id DESC")) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Order> findByCustomer(int customerId) throws SQLException {
        List<Order> list = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(
                SELECT_BASE + "WHERE o.customer_id_fk=? ORDER BY o.order_id DESC")) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    private Order mapRow(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setOrderId(rs.getInt("order_id"));
        o.setCustomerIdFk(rs.getInt("customer_id_fk"));
        o.setUserIdFk(rs.getInt("user_id_fk"));
        o.setOrderStatuseIdFk(rs.getInt("order_statuse_id_fk"));
        o.setCustomerName(rs.getString("customer_name"));
        o.setEmployeeName(rs.getString("user_name"));
        o.setStatusState(rs.getString("state"));
        return o;
    }

    public void updateStatus(int orderId, int newStatusId) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(
                "UPDATE orders SET order_statuse_id_fk=? WHERE order_id=?")) {
            ps.setInt(1, newStatusId);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }

    public java.util.List<int[]> findAllStatusIds() throws SQLException {

        return new java.util.ArrayList<>();
    }

    public java.util.List<com.marketplace.shop.model.OrderStatus> findAllStatuses() throws SQLException {
        java.util.List<com.marketplace.shop.model.OrderStatus> list = new java.util.ArrayList<>();
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT order_statuse_id, order_statuse_code, state FROM order_statuse ORDER BY order_statuse_id")) {
            while (rs.next()) {
                com.marketplace.shop.model.OrderStatus os = new com.marketplace.shop.model.OrderStatus();
                os.setOrderStatuseId(rs.getInt("order_statuse_id"));
                os.setOrderStatuseCode(rs.getString("order_statuse_code"));
                os.setState(rs.getString("state"));
                list.add(os);
            }
        }
        return list;
    }
}
