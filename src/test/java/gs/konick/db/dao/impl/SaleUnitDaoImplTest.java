package gs.konick.db.dao.impl;

import gs.konick.dao.SaleUnitDao;
import gs.konick.dao.impl.SaleUnitDaoImpl;
import gs.konick.db.ConnectionPool;
import gs.konick.db.dao.BaseDaoTest;
import gs.konick.model.SaleUnit;
import gs.konick.utils.SQLUtils;
import gs.konick.utils.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Stream;

public class SaleUnitDaoImplTest extends BaseDaoTest {
    /**
     * SQL скрипты и пути к ним, которые создают таблицы и добавляют их содержимое
     */
    private static final String CREATE_TABLES_SALES_AND_CATEGORIES_PATH = "sql/create_tables_saleunits_and_categories.sql";
    private static final String CREATE_TABLES_SALES_AND_CATEGORIES = Utils.makeStringFromFile(CREATE_TABLES_SALES_AND_CATEGORIES_PATH);
    private static final String INSERT_CATEGORIES_AND_SALEUNITS_PATH = "sql/insert_into_categories_and_saleunits.sql";
    private static final String INSERT_CATEGORIES_AND_SALEUNITS = Utils.makeStringFromFile(INSERT_CATEGORIES_AND_SALEUNITS_PATH);

    private static SaleUnitDao saleUnitDao = SaleUnitDaoImpl.getInstance();

    @BeforeAll
    static void createTablesAndInsertValues() {
        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            SQLUtils.executeSeveralQueries(connection, CREATE_TABLES_SALES_AND_CATEGORIES);
            SQLUtils.executeSeveralQueries(connection, INSERT_CATEGORIES_AND_SALEUNITS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Проверка функции getSaleUnitById")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @MethodSource("provideGetSaleUnitById")
    public void getSaleUnitByIdTest(String expectedName, long id) throws SQLException {
        SaleUnit actualSaleUnit = saleUnitDao.getSaleUnitById(id);

        Assertions.assertEquals(expectedName, actualSaleUnit.getName());
    }

    public static Stream<Arguments> provideGetSaleUnitById() {
        return Stream.of(
                Arguments.of("Маргарита", 1),
                Arguments.of("Гавайская", 2),
                Arguments.of("Том Ям", 4)
        );
    }
}
