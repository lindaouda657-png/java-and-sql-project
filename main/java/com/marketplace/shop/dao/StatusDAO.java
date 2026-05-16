package com.marketplace.shop.dao;

import com.marketplace.shop.model.Status;
import com.marketplace.shop.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatusDAO {

    public List<Status> findAll() throws SQLException {
        List<Status> list = new ArrayList<>();
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM statuses")) {
            while (rs.next()) {
                list.add(new Status(rs.getString("statuse_code"), rs.getString("statuse_name")));
            }
        }
        return list;
    }
}
