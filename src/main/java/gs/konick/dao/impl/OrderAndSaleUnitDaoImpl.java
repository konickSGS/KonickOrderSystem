package gs.konick.dao.impl;

import gs.konick.dao.OrderAndSaleUnitDao;
import gs.konick.db.ConnectionPool;
import gs.konick.model.SaleUnit;
import gs.konick.sql.OrderAndSaleUnitSqlQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderAndSaleUnitDaoImpl implements OrderAndSaleUnitDao {

    private static OrderAndSaleUnitDao INSTANCE;

    private OrderAndSaleUnitDaoImpl() {
    }

    public static OrderAndSaleUnitDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OrderAndSaleUnitDaoImpl();
        }
        return INSTANCE;
    }

    @Override
    public List<Map<SaleUnit, Integer>> getAllSaleUnitsByOrderId(long orderId) {
        List<Map<SaleUnit, Integer>> saleUnitAndCount = new ArrayList<>();

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(OrderAndSaleUnitSqlQuery.FIND_ALL_SALEUNITS_BY_ORDERID)) {
            preparedStatement.setLong(1, orderId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long saleUnitId = resultSet.getLong("saleunit_id");
                    int count = resultSet.getInt("count");
                    SaleUnit saleUnit = saleUnitDao.getSaleUnitById(saleUnitId);
                    saleUnitAndCount.add(Map.of(saleUnit, count));
                }
            }

            return saleUnitAndCount;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addOrderAndSaleUnit(long orderId, long saleUnitId, int count) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(OrderAndSaleUnitSqlQuery.ADD_ORDER_AND_SALEUNIT)) {
            preparedStatement.setLong(1, orderId);
            preparedStatement.setLong(2, saleUnitId);
            preparedStatement.setInt(3, count);

            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalArgumentException("Не удалось добавить в order_and_saleunit");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
