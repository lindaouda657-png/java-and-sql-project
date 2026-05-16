package com.marketplace.shop.dao;

import com.marketplace.shop.model.Department;
import com.marketplace.shop.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO implements GenericDAO<Department> {

    @Override
    public void insert(Department d) throws SQLException {
        String sql = "INSERT INTO department (department_name, statuse_code_fk, city, street_name, building_NO, apartment_NO) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getDepartmentName());
            ps.setString(2, d.getStatuseCodeFk());
            ps.setString(3, d.getCity());
            ps.setString(4, d.getStreetName());
            ps.setString(5, d.getBuildingNo());
            ps.setString(6, d.getApartmentNo());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) d.setDepartmentId(keys.getInt(1));
        }
    }

    @Override
    public void update(Department d) throws SQLException {
        String sql = "UPDATE department SET department_name=?, statuse_code_fk=?, city=?, street_name=?, building_NO=?, apartment_NO=? WHERE department_id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, d.getDepartmentName());
            ps.setString(2, d.getStatuseCodeFk());
            ps.setString(3, d.getCity());
            ps.setString(4, d.getStreetName());
            ps.setString(5, d.getBuildingNo());
            ps.setString(6, d.getApartmentNo());
            ps.setInt(7, d.getDepartmentId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Object id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("DELETE FROM department WHERE department_id=?")) {
            ps.setInt(1, (Integer) id);
            ps.executeUpdate();
        }
    }

    @Override
    public Department findById(Object id) throws SQLException {
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM department WHERE department_id=?")) {
            ps.setInt(1, (Integer) id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    @Override
    public List<Department> findAll() throws SQLException {
        List<Department> list = new ArrayList<>();
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM department")) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    private Department mapRow(ResultSet rs) throws SQLException {
        Department d = new Department();
        d.setDepartmentId(rs.getInt("department_id"));
        d.setDepartmentName(rs.getString("department_name"));
        d.setStatuseCodeFk(rs.getString("statuse_code_fk"));
        d.setCity(rs.getString("city"));
        d.setStreetName(rs.getString("street_name"));
        d.setBuildingNo(rs.getString("building_NO"));
        d.setApartmentNo(rs.getString("apartment_NO"));
        return d;
    }
}
