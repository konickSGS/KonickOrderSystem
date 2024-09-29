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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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

    @DisplayName("Проверка функций getFilteredSaleUnitsByCategoryId")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @MethodSource("provideGetFilteredSaleUnitsByCategoryId")
    public void getFilteredSaleUnitsByCategoryIdTest(long expectedCategoryId, Set<String> names) {
        List<SaleUnit> filteredSaleUnits = saleUnitDao.getFilteredSaleUnitsByCategoryId(expectedCategoryId);

        // Проверка, что количество товаров соответствует
        Assertions.assertEquals(names.size(), filteredSaleUnits.size());

        filteredSaleUnits.stream()
                .map(SaleUnit::getCategoryId)
                .forEach(actualCategoryId -> {
                    Assertions.assertEquals(expectedCategoryId, actualCategoryId);
                });

        Set<String> filteredNameSet = filteredSaleUnits.stream()
                .map(SaleUnit::getName)
                .collect(Collectors.toSet());
        Assertions.assertTrue(filteredNameSet.containsAll(names) && names.containsAll(filteredNameSet));
    }

    public static Stream<Arguments> provideGetFilteredSaleUnitsByCategoryId() {
        return Stream.of(
                Arguments.of(1, Set.of("Маргарита", "Гавайская", "4 сыра")),
                Arguments.of(2, Set.of("Том Ям", "Рамен")),
                Arguments.of(3, Set.of("Филадельфия", "Калифорния"))
        );
    }

    @DisplayName("Проверка функции changePrice")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @MethodSource("provideChangePrice")
    public void changePriceTest(long id, long expectedNewPrice) throws SQLException {
        long oldPrice = saleUnitDao.getSaleUnitById(id).getPrice();
        saleUnitDao.changePrice(id, expectedNewPrice);
        long actualNewPrice = saleUnitDao.getSaleUnitById(id).getPrice();
        Assertions.assertEquals(expectedNewPrice, actualNewPrice);

        // Возвращение старой цены
        saleUnitDao.changePrice(id, oldPrice);
        long actualOldPrice = saleUnitDao.getSaleUnitById(id).getPrice();
        Assertions.assertEquals(oldPrice, actualOldPrice);
    }

    public static Stream<Arguments> provideChangePrice() {
        return Stream.of(
                Arguments.of(1, 10000),
                Arguments.of(2, 20000),
                Arguments.of(3, 40000)
        );
    }

    @DisplayName("Проверка функций add, getallsaleunits и delete")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @MethodSource("provideGetAllSaleUnitsAndAddAndDelete")
    public void getAllSaleUnitsAndAddAndDelete(List<List<String>> newSaleUnitsStrings) {
        int sizeBeforeInsert = saleUnitDao.getAllSaleUnits().size();
        int sizeNewUsers = newSaleUnitsStrings.size();

        newSaleUnitsStrings.stream().forEach(l -> {
            String name = l.get(0);
            long categoryId = Long.parseLong(l.get(1));
            long price = Long.parseLong(l.get(2));
            saleUnitDao.addSaleUnit(name, categoryId, price);
        });

        int sizeAfterInsert = saleUnitDao.getAllSaleUnits().size();
        // Проверяем, что длина getAllUsers увеличилось
        Assertions.assertEquals(sizeAfterInsert, sizeBeforeInsert + sizeNewUsers);
        // Проверяем, что строки добавились правильно
        newSaleUnitsStrings.forEach(l -> {
            String currentName = l.get(0);
            SaleUnit currentSaleUnit = saleUnitDao.getSaleUnitByName(currentName);
            Assertions.assertAll("Проверка соответствующих строк при добавлении",
                    () -> Assertions.assertEquals(currentName, currentSaleUnit.getName()),
                    () -> Assertions.assertEquals(Long.parseLong(l.get(1)), currentSaleUnit.getCategoryId()),
                    () -> Assertions.assertEquals(Long.parseLong(l.get(2)), currentSaleUnit.getPrice())
            );
        });

        List<SaleUnit> newSaleUnits = newSaleUnitsStrings.stream()
                .map(l -> saleUnitDao.getSaleUnitByName(l.get(0)))
                .toList();

        newSaleUnits.forEach(saleUnit -> {
            long currentId = saleUnit.getId();
            saleUnitDao.deleteSaleUnit(currentId);
        });

        // Проверяем, что строки удалились правильно
        int sizeAfterDelete = saleUnitDao.getAllSaleUnits().size();
        Assertions.assertEquals(sizeBeforeInsert, sizeAfterDelete,
                "После удаления остались строки");
    }

    public static Stream<Arguments> provideGetAllSaleUnitsAndAddAndDelete() {
        List<List<String>> newSaleUnits1 = List.of(
                List.of("1name1", "1", "1100000"),
                List.of("1name2", "2", "2100000"),
                List.of("1name3", "3", "3100000")
        );

        List<List<String>> newSaleUnits2 = List.of(
                List.of("2name1", "1", "1200000")
        );

        return Stream.of(
                Arguments.of(newSaleUnits1),
                Arguments.of(newSaleUnits2)
        );
    }
}
