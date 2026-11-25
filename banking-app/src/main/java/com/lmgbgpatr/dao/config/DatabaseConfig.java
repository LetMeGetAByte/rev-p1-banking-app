package com.lmgbgpatr.dao.config;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;

import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConfig {

    private static final Dotenv dotenv = Dotenv.load();

    private static String buildURL(String user, String password, String host, String db) {
        return String.format(
            "jdbc:postgresql://%s",
            host
        );
    }

    public static DataSource getDataSource() {
        String host = dotenv.get("POSTGRES_HOST");
        String user = dotenv.get("POSTGRES_USER");
        String password = dotenv.get("POSTGRES_PASSWORD");
        String db = dotenv.get("POSTGRES_DB");
        String url = buildURL(user, password, host, db);

        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setURL(url);
        ds.setUser(user);
        ds.setPassword(password);
        ds.setDatabaseName(db);
        return ds;
    }
}
