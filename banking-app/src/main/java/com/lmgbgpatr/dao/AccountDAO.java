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
import com.lmgbgpatr.dto.AccountType;
import com.lmgbgpatr.dto.UserLevel;
import com.lmgbgpatr.model.Account;
import com.lmgbgpatr.model.User;

public class AccountDAO implements DataAccessObject<Account> {

    private final DataSource dataSource;

    public AccountDAO() {
        this.dataSource = DatabaseConfig.getDataSource();
    }

    public AccountDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Account getByID(int id) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement st = conn.prepareStatement(
                "SELECT * FROM accounts WHERE accounts.id = ?"
            );
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Account(
                    rs.getInt("id"),
                    rs.getInt("custid"),
                    AccountType.valueOf(rs.getString("type")),
                    rs.getBoolean("isfrozen"),
                    rs.getBigDecimal("balance")
                );
            }
        } catch (SQLException e) {}

        return null;
    }

    public List<Account> getByCustID(int id) {
        List<Account> accountList = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement st = conn.prepareStatement(
                "SELECT * FROM accounts WHERE accounts.custid = ?"
            );
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                accountList.add(
                    new Account(
                        rs.getInt("id"),
                        rs.getInt("custid"),
                        AccountType.valueOf(rs.getString("type")),
                        rs.getBoolean("isfrozen"),
                        rs.getBigDecimal("balance")
                    )
                );
            }
        } catch (SQLException e) {}

        return accountList;
    }

    @Override
    public List<Account> getAll() {
        List<Account> accountList = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM accounts");
            while (rs.next()) {
                accountList.add(
                    new Account(
                        rs.getInt("id"),
                        rs.getInt("custid"),
                        AccountType.valueOf(rs.getString("type")),
                        rs.getBoolean("isfrozen"),
                        rs.getBigDecimal("balance")
                    )
                );
            }
        } catch (SQLException e) {}

        return accountList;
    }

    @Override
    public void insert(Account account) {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            PreparedStatement st = conn.prepareStatement(
                "INSERT INTO accounts (id, custid, balance) " +
                "VALUES (?, ?, ?, ?)"
            );
            st.setInt(1, account.id());
            st.setInt(2, account.custID());
            st.setString(3, account.type().toString());
            st.setBoolean(4, account.isFrozen());
            st.setBigDecimal(5, account.balance());
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
