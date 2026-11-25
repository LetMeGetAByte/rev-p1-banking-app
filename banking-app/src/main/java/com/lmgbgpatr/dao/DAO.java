package com.lmgbgpatr.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.lmgbgpatr.dto.AccountType;
import com.lmgbgpatr.dto.RetrievalRequest;
import com.lmgbgpatr.dto.RetrievalResult;
import com.lmgbgpatr.dto.UserLevel;
import com.lmgbgpatr.model.Account;

import io.github.cdimascio.dotenv.Dotenv;

@Deprecated
public class DAO {

    private static final Logger logger = Logger.getLogger(DAO.class.getName());
    private static final Dotenv dotenv = Dotenv.load();
    
    private Connection conn;
    private FileHandler fhand;

    @Deprecated
    public DAO() {
        String host = dotenv.get("POSTGRES_HOST");
        String user = dotenv.get("POSTGRES_USER");
        String password = dotenv.get("POSTGRES_PASSWORD");
        String db = dotenv.get("POSTGRES_DB");
        String url = buildURL(user, password, host, db);
        
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        try {
            fhand = new FileHandler("DAO.log", true);
            fhand.setFormatter(new SimpleFormatter());
            logger.addHandler(fhand);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            System.exit(-1);
        }
    }

    @Deprecated
    public static String buildURL(String user, String password, String host, String db) {
        return String.format(
            "jdbc:postgresql://%s/%s?user=%s&password=%s",
            host,
            db,
            user,
            password
        );
    }

    @Deprecated
    public boolean isUser(String username) {
        logger.info("Verifying user: " + username);

        try {
            PreparedStatement st = conn.prepareStatement("SELECT * FROM users WHERE users.username = ?");
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }

        return false;
    }

    @Deprecated
    public int getUserIDFromHash(int hash) {
        logger.info("Retrieving ID of user: " + hash);

        try {
            PreparedStatement st = conn.prepareStatement("SELECT id FROM users WHERE users.hash = ?");
            st.setInt(1, hash);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }

        return -1;
    }

    @Deprecated
    public UserLevel login(int hash) {
        logger.info("Attempting to login as: " + hash);

        try {
            PreparedStatement st = conn.prepareStatement("SELECT level FROM users WHERE users.hash = ?");
            st.setInt(1, hash);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return UserLevel.valueOf(rs.getInt(1));
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }

        return UserLevel.UNAUTHORIZED;
    }

    @Deprecated
    public UserLevel register(String username, String firstName, String lastName, int hash) {
        logger.info("Attempting to register username: " + username + " with hash: " + hash);

        if (username == null || username.length() == 0) {
            return UserLevel.UNAUTHORIZED;
        }

        if (firstName == null || firstName.length() == 0) {
            firstName = "John";
        }

        if (lastName == null || lastName.length() == 0) {
            lastName = "Doe";
        }
        
        try {
            if (isUser(username)) {
                return UserLevel.UNAUTHORIZED;
            }

            PreparedStatement st = conn.prepareStatement(
                "INSERT INTO users (hash, username, firstname, lastname, level) " +
                "VALUES (?, ?, ?, ?, ?)"
            );
            st.setInt(1, hash);
            st.setString(2, username);
            st.setString(3, firstName);
            st.setString(4, lastName);
            st.setInt(5, 1);
            st.executeUpdate();
            return UserLevel.CUSTOMER;
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }

        return UserLevel.UNAUTHORIZED;
    }

    @Deprecated
    public RetrievalResult<?> request(int hash, RetrievalRequest request) {
        logger.info("Attempting to retrieve " + request.toString() + " as: " + hash);

        RetrievalResult<?> result = null;
        
        try {
            PreparedStatement st = conn.prepareStatement(
                switch (request) {
                    case RetrievalRequest.ACCOUNTS -> "SELECT * FROM accounts WHERE custid = ?";
                    case RetrievalRequest.TRANSACTIONS -> "SELECT * FROM transactions WHERE custid = ?";
                    case RetrievalRequest.CUSTOMERS -> "";
                    case RetrievalRequest.LOGS -> "";
                }
            );
            st.setInt(1, getUserIDFromHash(hash));
            ResultSet rs = st.executeQuery();

            if (RetrievalRequest.ACCOUNTS.equals(request)) {
                List<Account> accounts = new ArrayList<>();
                while (rs.next()) {
                    Account account = new Account(
                        rs.getInt("id"),
                        rs.getInt("custid"),
                        AccountType.valueOf(rs.getString("type")),
                        rs.getBoolean("isfrozen"),
                        rs.getBigDecimal("balance")
                    );
                    logger.info("Retrieved account: " + account.toString());
                    accounts.add(account);
                }
                result = new RetrievalResult<>(Map.of(hash, accounts));
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }

        return result;
    }

    @Deprecated
    public int createAccount(int hash, AccountType type) {
        logger.info("Attempting to create account of type: " + type + " with hash: " + hash);

        try {
            PreparedStatement st = conn.prepareStatement(
                "INSERT INTO accounts (hash, type) " +
                "VALUES (?, ?)"
            );
            st.setInt(1, getUserIDFromHash(hash));
            st.setString(2, type.toString());
            st.executeUpdate();
            
            PreparedStatement st2 = conn.prepareStatement(
                "SELECT id FROM accounts ORDER BY id DESC LIMIT 1"
            );
            ResultSet rs = st2.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }

        return -1;
    }

    @Deprecated
    public boolean closeAccount(int hash, int id) {
        logger.info("Attempting to close account with id: " + id + " with hash: " + hash);

        try {
            PreparedStatement matchingAccCnt = conn.prepareStatement(
                "SELECT id, COUNT(*) " +
                "FROM accounts WHERE custId = ? AND id = ?"
            );
            matchingAccCnt.setInt(1, getUserIDFromHash(hash));
            matchingAccCnt.setInt(2, id);
            ResultSet rs = matchingAccCnt.executeQuery();
            int matchedID;

            if (!rs.next() || (matchedID = rs.getInt(1)) != 1) {
                return false;
            }

            PreparedStatement st = conn.prepareStatement(
                "DELETE FROM accounts WHERE id = ?"
            );
            st.setInt(1, matchedID);
            st.executeUpdate();
            
            return true;

        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }

        return false;


    }

}