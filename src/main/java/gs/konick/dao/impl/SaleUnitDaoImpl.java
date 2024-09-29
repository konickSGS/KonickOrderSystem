package gs.konick.dao.impl;

import gs.konick.dao.SaleUnitDao;
import gs.konick.db.ConnectionPool;
import gs.konick.model.SaleUnit;
import gs.konick.sql.SaleUnitSqlQuery;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleUnitDaoImpl implements SaleUnitDao {

    private static SaleUnitDao INSTANCE;

    private SaleUnitDaoImpl() {
    }

    public static synchronized SaleUnitDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SaleUnitDaoImpl();
        }
        return INSTANCE;
    }

    private static SaleUnit makeSaleUnit(ResultSet resultSet) {
        try {
            int idColumn = resultSet.findColumn("id");
            int nameColumn = resultSet.findColumn("name");
            int categoryColumn = resultSet.findColumn("category_id");
            int priceColumn = resultSet.findColumn("price");
            return new SaleUnit.Builder()
                    .setId(resultSet.getLong(idColumn))
                    .setName(resultSet.getString(nameColumn))
                    .setCategoryId(resultSet.getInt(categoryColumn))
                    .setPrice(resultSet.getLong(priceColumn))
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException("В таблице нет колонки с таким именем", e);
        }
    }

    @Override
    public SaleUnit getSaleUnitById(long id) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SaleUnitSqlQuery.FIND_SALEUNIT_BY_ID)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) return null;
                return makeSaleUnit(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SaleUnit> getAllSaleUnits() {
        List<SaleUnit> saleUnits = new ArrayList<>();

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SaleUnitSqlQuery.GET_ALL_SALEUNITS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                saleUnits.add(makeSaleUnit(resultSet));
            }

            return saleUnits;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SaleUnit> getFilteredSaleUnitsByCategoryId(long categoryId) {
        List<SaleUnit> saleUnits = new ArrayList<>();

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SaleUnitSqlQuery.FILTER_SALEUNITS_BY_CATEGORY)) {
            preparedStatement.setLong(1, categoryId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    saleUnits.add(makeSaleUnit(resultSet));
                }
            }
            return saleUnits;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changePrice(long id, long price) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SaleUnitSqlQuery.CHANGE_PRICE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, price);

            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalArgumentException("Не получилось изменить цену товара");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SaleUnit addSaleUnit(String name, long categoryId, long price) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SaleUnitSqlQuery.ADD_SALEUNIT, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, name);
            preparedStatement.setLong(2, categoryId);
            preparedStatement.setLong(3, price);
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalArgumentException("Не получилось создать новый saleunit");
            }

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            int insertId = 0;
            if (generatedKeys.next()) {
                insertId = generatedKeys.getInt(1);
            }
            return getSaleUnitById(insertId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSaleUnit(long id) {

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SaleUnitSqlQuery.DELETE_SALEUNIT)) {
            preparedStatement.setLong(1, id);
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalArgumentException("Не получилось удалить saleunit");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
