package com.lmgbgpatr.backend;

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

import com.lmgbgpatr.middleware.dto.Account;
import com.lmgbgpatr.middleware.dto.AccountType;
import com.lmgbgpatr.middleware.dto.RetrievalRequest;
import com.lmgbgpatr.middleware.dto.RetrievalResult;
import com.lmgbgpatr.middleware.dto.UserLevel;

import io.github.cdimascio.dotenv.Dotenv;

public class DAO {

    private static final Logger logger = Logger.getLogger(DAO.class.getName());
    private static final Dotenv dotenv = Dotenv.load();
    
    private Connection conn;
    private FileHandler fhand;

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

    public static String buildURL(String user, String password, String host, String db) {
        return String.format(
            "jdbc:postgresql://%s/%s?user=%s&password=%s",
            host,
            db,
            user,
            password
        );
    }

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

    public UserLevel register(String username, int hash) {
        logger.info("Attempting to register username: " + username + " with hash: " + hash);
        if (username == null || username.length() == 0) {
            return UserLevel.UNAUTHORIZED;
        }
        
        try {
            if (isUser(username)) {
                return UserLevel.UNAUTHORIZED;
            }

            PreparedStatement st = conn.prepareStatement(
                "INSERT INTO users (hash, username, level) " +
                "VALUES (?, ?, ?)"
            );
            st.setInt(1, hash);
            st.setString(2, username);
            st.setInt(3, 1);
            st.executeUpdate();
            return UserLevel.CUSTOMER;
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }

        return UserLevel.UNAUTHORIZED;
    }

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
                        rs.getInt(1),
                        rs.getInt(2),
                        AccountType.valueOf(rs.getString(3)),
                        rs.getDouble(4)
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
}