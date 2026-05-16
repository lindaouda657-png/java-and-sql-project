package com.marketplace.shop.dao;

import com.marketplace.shop.model.Permission;
import com.marketplace.shop.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PermissionDAO {

    public List<Permission> findAll() throws SQLException {
        List<Permission> list = new ArrayList<>();
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(
                 "SELECT * FROM permissions WHERE statuse_code_fk='ACTIVE' ORDER BY permission_code")) {
            while (rs.next()) {
                list.add(new Permission(
                    rs.getInt("permission_id"),
                    rs.getString("permission_code"),
                    rs.getString("statuse_code_fk")));
            }
        }
        return list;
    }
}
