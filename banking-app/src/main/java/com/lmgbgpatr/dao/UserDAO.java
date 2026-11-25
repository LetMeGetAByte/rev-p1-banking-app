package com.lmgbgpatr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.lmgbgpatr.dao.config.DatabaseConfig;
import com.lmgbgpatr.dto.UserLevel;
import com.lmgbgpatr.model.User;

public class UserDAO implements DataAccessObject<User> {

    private final DataSource dataSource;

    public UserDAO() {
        this.dataSource = DatabaseConfig.getDataSource();
    }

    public UserDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User getByID(int id) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement st = conn.prepareStatement(
                "SELECT * FROM users WHERE users.id = ?"
            );
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("fname"),
                    rs.getString("lname"),
                    UserLevel.valueOf(rs.getInt("level"))
                );
            }
        } catch (SQLException e) {
            // safe catch and no-op
            // should impl logger
        }

        return null;
    }

    public User getByUsername(String username) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement st = conn.prepareStatement(
                "SELECT * FROM users WHERE users.username = ?"
            );
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("fname"),
                    rs.getString("lname"),
                    UserLevel.valueOf(rs.getInt("level"))
                );
            }
        } catch (SQLException e) {}

        return null;
    }

    @Override
    public List<User> getAll() {
        List<User> userList = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                userList.add(
                    new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("fname"),
                        rs.getString("lname"),
                        UserLevel.valueOf(rs.getInt("level"))
                    )
                );
            }
        } catch (SQLException e) {}

        return userList;
    }

    @Override
    public void insert(User user) {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            PreparedStatement st = conn.prepareStatement(
                "INSERT INTO users (username, password, fname, lname) " +
                "VALUES (?, ?, ?, ?)"
            );
            st.setString(1, user.username());
            st.setString(2, user.password());
            st.setString(3, user.fname());
            st.setString(4, user.lname());
            st.executeUpdate();
            conn.commit();
        } catch (NullPointerException | SQLException e) {}
    }

    @Override
    public void deleteByID(int id) {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            PreparedStatement st = conn.prepareStatement(
                "DELETE FROM users WHERE id = ?"
            );
            st.setInt(1, id);
            st.executeUpdate();
            conn.commit();
        } catch (SQLException e) {}
    }
}
