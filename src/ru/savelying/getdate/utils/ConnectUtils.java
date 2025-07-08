package ru.savelying.getdate.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@UtilityClass
public class ConnectUtils {

    public static final String dbName = ConfigFileUtils.getConfig("app.datasource.database");
    public static final String dbURL = ConfigFileUtils.getConfig("app.datasource.url");
    public static final String dbUser = ConfigFileUtils.getConfig("app.datasource.username");
    public static final String dbPassword = ConfigFileUtils.getConfig("app.datasource.password");
    public static final String dbDriver = ConfigFileUtils.getConfig("app.datasource.driver");
    public static final int dbQueryTimeout = Integer.parseInt(ConfigFileUtils.getConfig("app.datasource.query.timeout") != null ? ConfigFileUtils.getConfig("app.datasource.query.timeout") : "10");
    public static final int dbFetchSize = Integer.parseInt(ConfigFileUtils.getConfig("app.datasource.fetch.size") != null ? ConfigFileUtils.getConfig("app.datasource.fetch.size") : "100");
    public static final int dbMaxRows = Integer.parseInt(ConfigFileUtils.getConfig("app.datasource.max.rows") != null ? ConfigFileUtils.getConfig("app.datasource.max.rows") : "1000");

    static {
        if (dbDriver != null) {
            try {
                Class.forName(dbDriver);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Connection getConnnect() throws SQLException {
        return DriverManager.getConnection(dbURL + dbName, dbUser, dbPassword);
    }
}
