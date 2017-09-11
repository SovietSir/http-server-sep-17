package com.epam.store;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.stream.Collectors;

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

    public void initDatabase() {
        executeSQL("src/main/resources/init_leagues.sql");
        executeSQL("src/main/resources/init_events.sql");
    }

    public void dropDatabase() {
        executeSQL("src/main/resources/drop.sql");
    }

    @SneakyThrows
    private void executeSQL(String file) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            String query = Files.lines(Paths.get(file))
                    .filter(line -> !line.startsWith("--")) //comments
                    .collect(Collectors.joining(" "));
            statement.execute(query);
        }
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