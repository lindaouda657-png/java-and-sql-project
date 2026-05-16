package com.marketplace.shop.dao;

import com.marketplace.shop.model.UserCredential;
import com.marketplace.shop.util.DatabaseConnection;

import java.sql.*;

public class UserCredentialDAO {

    public UserCredential findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM user_credentials WHERE user_id_fk=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                UserCredential uc = new UserCredential();
                uc.setCredentialId(rs.getInt("credential_id"));
                uc.setUserIdFk(rs.getInt("user_id_fk"));
                uc.setPasswordHash(rs.getString("password_hash"));
                uc.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                uc.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                return uc;
            }
        }
        return null;
    }

    public void insert(UserCredential uc) throws SQLException {
        String sql = "INSERT INTO user_credentials (user_id_fk, password_hash) VALUES (?,?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, uc.getUserIdFk());
            ps.setString(2, uc.getPasswordHash());
            ps.executeUpdate();
        }
    }

    public void updatePassword(int userId, String newHash) throws SQLException {
        String sql = "UPDATE user_credentials SET password_hash=? WHERE user_id_fk=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, newHash);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }
}
