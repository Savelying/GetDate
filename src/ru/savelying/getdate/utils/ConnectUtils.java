package ru.savelying.getdate.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import ru.savelying.getdate.dao.query.Query;
import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;


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
    public static final int dbPoolSize = Integer.parseInt(ConfigFileUtils.getConfig("app.datasource.pool.size") != null ? ConfigFileUtils.getConfig("app.datasource.pool.size") : "10");

    private static DataSource dataSource;

    static {
        init();
    }

    @SneakyThrows
    private static void init() {
        Class.forName(dbDriver);
        dataSource = new CustomDataSource();
    }

    @SneakyThrows
    public static Connection getConnnect() {
        return dataSource.getConnection();
    }

    @SneakyThrows
    public static void closePool() {
        ((Closeable) dataSource).close();
    }

    public static PreparedStatement getPreparedStatement(Connection connection, Query query) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query.sql());
        List<Object> args = query.args();
        for (int i = 0; i < args.size(); i++) statement.setObject(i + 1, args.get(i));
        return statement;
    }
}
