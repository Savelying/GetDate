package ru.savelying.getdate.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Logger;

import static ru.savelying.getdate.utils.ConnectUtils.*;

@Slf4j
public class CustomDataSource implements DataSource, Closeable {
    private final BlockingDeque<Connection> pool;
    private final List<Connection> connections;

    @SneakyThrows
    public CustomDataSource() {
        this.pool = new LinkedBlockingDeque<>(dbPoolSize);
        this.connections = new ArrayList<>();
        for (int i = 0; i < dbPoolSize; i++) {
            Connection connection = DriverManager.getConnection(dbURL + dbName, dbUser, dbPassword);
            connections.add(connection);
            pool.add(new ProxyConnection(connection, pool));
        }
        log.info("Custom DataSource initialized with {} connections", dbPoolSize);
    }

    @SneakyThrows
    public Connection getConnection() throws SQLException {
        return pool.take();
    }

    @SneakyThrows
    @Override
    public void close() throws IOException {
        for (Connection connection : connections) connection.close();
        log.info("CustomCP was closed");
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        throw  new SQLFeatureNotSupportedException();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return (T) this;
        }
        throw new SQLException("Wrapped DataSource is not an instance of " + iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this);
    }
}
