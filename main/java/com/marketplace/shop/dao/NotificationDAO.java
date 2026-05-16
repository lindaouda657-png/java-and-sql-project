package com.marketplace.shop.dao;

import com.marketplace.shop.model.Notification;
import com.marketplace.shop.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO implements GenericDAO<Notification> {

    @Override
    public void insert(Notification n) throws SQLException {
        String sql = "INSERT INTO notifications (user_id_fk, customer_id_fk, message, is_read) VALUES (?,?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (n.getUserIdFk() != null) ps.setInt(1, n.getUserIdFk()); else ps.setNull(1, Types.INTEGER);
            if (n.getCustomerIdFk() != null) ps.setInt(2, n.getCustomerIdFk()); else ps.setNull(2, Types.INTEGER);
            ps.setString(3, n.getMessage());
            ps.setBoolean(4, n.isRead());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) n.setNotificationId(keys.getInt(1));
        }
    }

    @Override
    public void update(Notification n) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection()
                .prepareStatement("UPDATE notifications SET is_read=? WHERE notification_id=?")) {
            ps.setBoolean(1, n.isRead());
            ps.setInt(2, n.getNotificationId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Object id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection()
                .prepareStatement("DELETE FROM notifications WHERE notification_id=?")) {
            ps.setInt(1, (Integer) id);
            ps.executeUpdate();
        }
    }

    @Override
    public Notification findById(Object id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection()
                .prepareStatement("SELECT * FROM notifications WHERE notification_id=?")) {
            ps.setInt(1, (Integer) id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    @Override
    public List<Notification> findAll() throws SQLException {
        List<Notification> list = new ArrayList<>();
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM notifications ORDER BY created_at DESC")) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Notification> findByCustomer(int customerId) throws SQLException {
        List<Notification> list = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection()
                .prepareStatement("SELECT * FROM notifications WHERE customer_id_fk=? ORDER BY created_at DESC")) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Notification> findByUser(int userId) throws SQLException {
        List<Notification> list = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getConnection()
                .prepareStatement("SELECT * FROM notifications WHERE user_id_fk=? ORDER BY created_at DESC")) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public void markRead(int notificationId) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection()
                .prepareStatement("UPDATE notifications SET is_read=1 WHERE notification_id=?")) {
            ps.setInt(1, notificationId);
            ps.executeUpdate();
        }
    }

    private Notification mapRow(ResultSet rs) throws SQLException {
        Notification n = new Notification();
        n.setNotificationId(rs.getInt("notification_id"));
        int uid = rs.getInt("user_id_fk"); n.setUserIdFk(rs.wasNull() ? null : uid);
        int cid = rs.getInt("customer_id_fk"); n.setCustomerIdFk(rs.wasNull() ? null : cid);
        n.setMessage(rs.getString("message"));
        n.setRead(rs.getBoolean("is_read"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) n.setCreatedAt(ts.toLocalDateTime());
        return n;
    }
}
