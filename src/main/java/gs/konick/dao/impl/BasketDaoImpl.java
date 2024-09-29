package gs.konick.dao.impl;

import gs.konick.dao.BasketDao;
import gs.konick.db.ConnectionPool;
import gs.konick.model.SaleUnit;
import gs.konick.sql.BasketSqlQuery;
import gs.konick.sql.SqlQuery;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class BasketDaoImpl implements BasketDao {

    private static BasketDao INSTANCE;

    private BasketDaoImpl() {
    }

    private static synchronized BasketDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BasketDaoImpl();
        }
        return INSTANCE;
    }

    @Override
    public void addSaleUnitToBasket(long userId, long saleUnitId, int count) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(BasketSqlQuery.ADD_SALEUNIT_TO_BASKET)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, saleUnitId);
            preparedStatement.setInt(3, count);
            preparedStatement.setInt(4, count);

            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalArgumentException("Не получилось добавить saleunit в корзину");
            }

            // Для транзакции
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<SaleUnit, Integer> getBasket(long userId) {
        Map<SaleUnit, Integer> basket = new HashMap<>();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(BasketSqlQuery.GET_BASKET)) {
            preparedStatement.setLong(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long saleUnitId = resultSet.getLong("saleunit_id");
                    SaleUnit saleUnit = saleUnitDao.getSaleUnitById(saleUnitId);
                    int count = resultSet.getInt("count");
                    basket.put(saleUnit, count);
                }
            }
            return basket;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void makeOrder(long userId, Map<SaleUnit, Integer> basket) {
        Connection connection = null;
        Savepoint savepoint = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            savepoint = connection.setSavepoint();
            long orderId = orderDao.addOrder(userId).getId();

            for (var basketUnit : basket.entrySet()) {
                long saleUnitId = basketUnit.getKey().getId();
                int count = basketUnit.getValue();
                orderAndSaleUnitDao.addOrderAndSaleUnit(orderId, saleUnitId, count);
            }

        } catch (SQLException e) {
            if (connection != null) {
                SqlQuery.rollback(connection, savepoint);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeSaleUnitFromBasket(long userId, long saleUnitId) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(BasketSqlQuery.REMOVE_SALEUNIT_FROM_BASKET)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, saleUnitId);
            if (preparedStatement.executeUpdate() == 0) {
                throw new SQLException("Не получилось удалить из корзины saleunit");
            }
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cleanBasket(long userId) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(BasketSqlQuery.CLEAN_USER_BASKET)) {
            preparedStatement.setLong(1, userId);
            if (preparedStatement.executeUpdate() == 0) {
                throw new SQLException("Не получилось отчистить корзину");
            }
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}