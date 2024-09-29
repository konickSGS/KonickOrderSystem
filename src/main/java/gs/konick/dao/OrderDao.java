package gs.konick.dao;

import gs.konick.model.Order;

import java.util.List;

public interface OrderDao extends BaseDao {

    Order getOrderById(long id);

    /**
     * Получение заказов конкретного юзера
     */
    List<Order> getUserOrders(long userId);

    List<Order> getAllOrders(long userId);

    /**
     * Получение заказов конкретного статуса
     */
    List<Order> getFilteredOrdersByStatus(long statusId);

    Order addOrder(long userId);

    void changeStatus(long id, long statusId);

    void deleteOrder(long id);
}
