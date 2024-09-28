package gs.konick.db.dao;

import gs.konick.db.ConnectionPool;
import org.junit.jupiter.api.BeforeAll;

/**
 * Базовый класс для тестов. Меняет настройки ConnectionPool, чтобы подключаться к тестовой базе данных
 */
public class BaseDaoTest {
    private static final String DATABASE_PROPERTIES_TEST_URL = "src/test/resources/db.properties";

    @BeforeAll
    static void init() {
        ConnectionPool.setPathDatabase(DATABASE_PROPERTIES_TEST_URL);
    }
}
