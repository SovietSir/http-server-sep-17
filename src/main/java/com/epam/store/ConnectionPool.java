package com.epam.store;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionPool implements AutoCloseable {
    /**
     * A c3po connection pool.
     *
     * @see <a href="http://www.mchange.com/projects/c3p0/">c3po</a>
     * @see ComboPooledDataSource
     */
    private ComboPooledDataSource cpds;

    public static ConnectionPool pool = new ConnectionPool();

    @SneakyThrows
    private ConnectionPool() {
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/db.properties"));

        cpds = new ComboPooledDataSource();
        cpds.setDriverClass(properties.getProperty("driver"));
        cpds.setJdbcUrl(properties.getProperty("url"));
        cpds.setUser(properties.getProperty("user"));
        cpds.setPassword(properties.getProperty("password"));
        int minPoolSize;
        try {
            minPoolSize = Integer.parseInt(properties.getProperty("minPoolSize"));
        } catch (Exception e) {
            minPoolSize = 1;
        }
        int maxPoolSize;
        try {
            maxPoolSize = Integer.parseInt(properties.getProperty("maxPoolSize"));
        } catch (Exception e) {
            maxPoolSize = 5;
        }
        cpds.setMinPoolSize(minPoolSize);
        cpds.setMaxPoolSize(maxPoolSize);
        cpds.setAcquireIncrement(1);
    }

    @SneakyThrows
    public Connection getConnection() {
        return cpds.getConnection();
    }

    @Override
    public void close() throws SQLException {
        cpds.close(true);
    }
}