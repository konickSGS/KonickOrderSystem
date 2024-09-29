package gs.konick.db.dao;

import gs.konick.db.ConnectionPool;
import gs.konick.utils.SQLUtils;
import gs.konick.utils.Utils;
import org.junit.jupiter.api.BeforeAll;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Базовый класс для тестов. Меняет настройки ConnectionPool, чтобы подключаться к тестовой базе данных
 */
public class BaseDaoTest {
    private static final String DATABASE_PROPERTIES_TEST_URL = "src/test/resources/db.properties";
    private static final String DATABASE_DROP_TABLES_PATH = "sql/drop_tables.sql";
    private static final String DATABASE_DROP_TABLES = Utils.makeStringFromFile(DATABASE_DROP_TABLES_PATH);


    @BeforeAll
    static void init() {
        ConnectionPool.setPathDatabase(DATABASE_PROPERTIES_TEST_URL);
        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            SQLUtils.executeSeveralQueries(connection, DATABASE_DROP_TABLES);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
