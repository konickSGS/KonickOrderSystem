package gs.konick.db.dao;

import gs.konick.dao.SaleUnitDao;
import gs.konick.dao.UserDao;
import gs.konick.dao.impl.SaleUnitDaoImpl;
import gs.konick.dao.impl.UserDaoImpl;
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
        System.out.println("baseunit");
        ConnectionPool.setPathDatabase(DATABASE_PROPERTIES_TEST_URL);
        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            SQLUtils.executeSeveralQueries(connection, DATABASE_DROP_TABLES);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * SQL скрипты и пути к ним для SaleUnits и Categories
     */
    private static final String CREATE_TABLES_SALEUNITS_AND_CATEGORIES_PATH = "sql/create_tables_saleunits_and_categories.sql";
    private static final String CREATE_TABLES_SALEUNITS_AND_CATEGORIES = Utils.makeStringFromFile(CREATE_TABLES_SALEUNITS_AND_CATEGORIES_PATH);
    private static final String INSERT_CATEGORIES_AND_SALEUNITS_PATH = "sql/insert_into_categories_and_saleunits.sql";
    private static final String INSERT_CATEGORIES_AND_SALEUNITS = Utils.makeStringFromFile(INSERT_CATEGORIES_AND_SALEUNITS_PATH);

    protected static void createAndInsertSaleUnitsAndCategories() {
        System.out.println("saleunits unit");
        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            SQLUtils.executeSeveralQueries(connection, CREATE_TABLES_SALEUNITS_AND_CATEGORIES);
            SQLUtils.executeSeveralQueries(connection, INSERT_CATEGORIES_AND_SALEUNITS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * SQL скрипты и пути к ним для Users и Roles
     */
    private static final String CREATE_TABLES_USERS_AND_ROLES_PATH = "sql/create_tables_users_and_roles.sql";
    private static final String CREATE_TABLES_USERS_AND_ROLES = Utils.makeStringFromFile(CREATE_TABLES_USERS_AND_ROLES_PATH);
    private static final String INSERT_USERS_AND_ROLES_PATH = "sql/insert_into_users.sql";
    private static final String INSERT_USERS_AND_ROLES = Utils.makeStringFromFile(INSERT_USERS_AND_ROLES_PATH);

    protected static void createAndInsertUsersAndRoles() {
        System.out.println("user unit");
        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            SQLUtils.executeSeveralQueries(connection, CREATE_TABLES_USERS_AND_ROLES);
            SQLUtils.executeSeveralQueries(connection, INSERT_USERS_AND_ROLES);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
