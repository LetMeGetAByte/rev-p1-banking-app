package com.lmgbgpatr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.sql.Timestamp;

import javax.sql.DataSource;

import com.lmgbgpatr.dao.config.DatabaseConfig;
import com.lmgbgpatr.dto.AccountType;
import com.lmgbgpatr.dto.UserLevel;
import com.lmgbgpatr.model.Account;
import com.lmgbgpatr.model.User;
import com.lmgbgpatr.model.Transaction;


public class TransactionDAO implements DataAccessObject<Transaction> {
    private final DataSource dataSource;

    public TransactionDAO() {
        this.dataSource = DatabaseConfig.getDataSource();
    }

    public TransactionDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Transaction getByID(int id) {
        ZoneOffset systemZoneOffset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement st = conn.prepareStatement(
                "SELECT * FROM transactions WHERE transactions.id = ?"
            );
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Transaction(
                    rs.getInt("id"),
                    rs.getInt("orgid"),
                    rs.getInt("destId"),
                    rs.getDouble("amount"),
                    rs.getBoolean("isWithdrawl"),
                    rs.getTimestamp("timestamp").toLocalDateTime().atOffset(systemZoneOffset)
                );
            }
        } catch (SQLException e) {}

        return null;
    }

    
    public Transaction getByOrgID(int orgid) {
        ZoneOffset systemZoneOffset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement st = conn.prepareStatement(
                "SELECT * FROM transactions WHERE transactions.orgid = ?"
            );
            st.setInt(1, orgid);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Transaction(
                    rs.getInt("id"),
                    rs.getInt("orgid"),
                    rs.getInt("destId"),
                    rs.getDouble("amount"),
                    rs.getBoolean("isWithdrawl"),
                    rs.getTimestamp("timestamp").toLocalDateTime().atOffset(systemZoneOffset)
                );
            }
        } catch (SQLException e) {}

        return null;
    }

    @Override
    public List<Transaction> getAll() {
        List<Transaction> transactionList = new ArrayList<>();
        ZoneOffset systemZoneOffset = ZoneId.systemDefault().getRules().getOffset(Instant.now());
        try (Connection conn = dataSource.getConnection()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM accounts");
            while (rs.next()) {
                transactionList.add(
                    new Transaction(
                    rs.getInt("id"),
                    rs.getInt("orgid"),
                    rs.getInt("destId"),
                    rs.getDouble("amount"),
                    rs.getBoolean("isWithdrawl"),
                    rs.getTimestamp("timestamp").toLocalDateTime().atOffset(systemZoneOffset)
                )
                );
            }
        } catch (SQLException e) {}

        return transactionList;
    }

    @Override
    public void insert(Transaction transaction) {
        
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            PreparedStatement st = conn.prepareStatement(
                "INSERT INTO transactions (id, orgid, amount) " +
                "VALUES (?, ?, ?, ?)"
            );
            st.setInt(1, transaction.id());
            st.setInt(2, transaction.orgID());
            st.setDouble(3, transaction.amount());
            st.setBoolean(4, transaction.isWithdrawl());
            st.setTimestamp(5, Timestamp.valueOf(transaction.timestamp().atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()));
            st.executeUpdate();
            conn.commit();
        } catch (NullPointerException | SQLException e) {}
    }

    @Override
    public void deleteByID(int id) {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            PreparedStatement st = conn.prepareStatement(
                "DELETE FROM transactions WHERE id = ?"
            );
            st.setInt(1, id);
            st.executeUpdate();
            conn.commit();
        } catch (SQLException e) {}
    }

}
