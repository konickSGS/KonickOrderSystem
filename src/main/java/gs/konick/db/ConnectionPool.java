package gs.konick.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionPool implements ConnectionManager {
    private static ConnectionPool INSTANCE;

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSource;

    private ConnectionPool() {
        Properties properties = new Properties();
        //try (InputStream input = new FileInputStream("db.properties")) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            properties.load(input);

            config.setJdbcUrl(properties.getProperty("jdbcUrl"));
            config.setUsername(properties.getProperty("user"));
            config.setPassword(properties.getProperty("password"));
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            dataSource = new HikariDataSource(config);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Нет файла db.properties", e);
        } catch (IOException e) {
            throw new IllegalStateException("Не могу подключиться к DB", e);
        }
    }

    public static ConnectionPool getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConnectionPool();
        }
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
