package com.marketplace.shop.dao;

import com.marketplace.shop.model.ProductStatus;
import com.marketplace.shop.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductStatusDAO {

    public List<ProductStatus> findAll() throws SQLException {
        List<ProductStatus> list = new ArrayList<>();
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM products_statuse")) {
            while (rs.next()) {
                list.add(new ProductStatus(
                    rs.getString("product_statuse_code"),
                    rs.getString("state"),
                    rs.getInt("product_statuse_id")
                ));
            }
        }
        return list;
    }
}
