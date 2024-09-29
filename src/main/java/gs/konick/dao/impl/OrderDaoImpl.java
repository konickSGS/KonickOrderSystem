package gs.konick.dao.impl;

import gs.konick.dao.OrderDao;
import gs.konick.db.ConnectionPool;
import gs.konick.model.Order;
import gs.konick.sql.OrderSqlQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    private static OrderDao INSTANCE;

    private OrderDaoImpl() {
    }

    public static OrderDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OrderDaoImpl();
        }
        return INSTANCE;
    }

    private static Order makeOrder(ResultSet resultSet) {
        try {
            int idColumn = resultSet.findColumn("id");
            int userIdColumn = resultSet.findColumn("user_id");
            int statusColumn = resultSet.findColumn("status_id");
            int totalColumn = resultSet.findColumn("total");
            int dateColumn = resultSet.findColumn("create_date");
            return new Order.Builder()
                    .setId(resultSet.getLong(idColumn))
                    .setUserId(resultSet.getLong(userIdColumn))
                    .setStatusId(resultSet.getLong(statusColumn))
                    .setTotal(resultSet.getInt(totalColumn))
                    .setCreateDate(resultSet.getDate(dateColumn))
                    .build();

        } catch (SQLException e) {
            throw new RuntimeException("В таблице нет колонки с таким именем", e);
        }
    }

    @Override
    public Order getOrderById(long id) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(OrderSqlQuery.FIND_ORDER_BY_ID)) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) return null;
                Order order = makeOrder(resultSet);
                order.setSaleUnitsAndCount(
                        orderAndSaleUnitDao.getAllSaleUnitsByOrderId(id)
                );
                return order;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> getUserOrders(long userId) {
        List<Order> orders = new ArrayList<>();

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(OrderSqlQuery.FIND_ALL_ORDERS_BY_USER_ID)) {
            preparedStatement.setLong(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = makeOrder(resultSet);
                    long orderId = order.getId();
                    // Добавление всех saleunits по данной orderId
                    order.setSaleUnitsAndCount(
                            orderAndSaleUnitDao.getAllSaleUnitsByOrderId(orderId)
                    );
                    orders.add(order);
                }
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> getAllOrders(long userId) {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(OrderSqlQuery.GET_ALL_ORDERS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Order order = makeOrder(resultSet);
                long orderId = order.getId();
                // Добавление всех saleunits по данной orderId
                order.setSaleUnitsAndCount(
                        orderAndSaleUnitDao.getAllSaleUnitsByOrderId(orderId)
                );
                orders.add(order);
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> getFilteredOrdersByStatus(long statusId) {
        List<Order> orders = new ArrayList<>();

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(OrderSqlQuery.GET_FILTERED_ORDERS_BY_STATUS)) {
            preparedStatement.setLong(1, statusId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = makeOrder(resultSet);
                    long orderId = order.getId();
                    // Добавление всех saleunits по данной orderId
                    order.setSaleUnitsAndCount(
                            orderAndSaleUnitDao.getAllSaleUnitsByOrderId(orderId)
                    );
                    orders.add(order);
                }
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order addOrder(long userId) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(OrderSqlQuery.ADD_ORDER, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, userId);

            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalArgumentException("Не получилось создать новый order");
            }

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            int insertId = 0;
            if (generatedKeys.next()) {
                insertId = generatedKeys.getInt(1);
            }
            return getOrderById(insertId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changeStatus(long id, long statusId) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(OrderSqlQuery.CHANGE_STATUS)) {
            preparedStatement.setLong(1, statusId);
            preparedStatement.setLong(2, id);

            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalArgumentException("Не получилось сменить статус заказа");
            }
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteOrder(long id) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(OrderSqlQuery.DELETE_ORDER)) {
            preparedStatement.setLong(1, id);

            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalArgumentException("Не получилось удалить заказ");
            }
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
