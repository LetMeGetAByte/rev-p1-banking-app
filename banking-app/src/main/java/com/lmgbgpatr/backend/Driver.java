package com.lmgbgpatr.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import io.github.cdimascio.dotenv.Dotenv;

public class Driver {

    private static final Dotenv dotenv = Dotenv.load();

    private Properties props = new Properties();
    private Connection conn;

    public Driver() {
        String host = dotenv.get("POSTGRES_HOST");
        String user = dotenv.get("POSTGRES_USER");
        String password = dotenv.get("POSTGRES_PASSWORD");
        String db = dotenv.get("POSTGRES_DB");
        String url = buildURL(user, password, host, db);
        
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
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
}