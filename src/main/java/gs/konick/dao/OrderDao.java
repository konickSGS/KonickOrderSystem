package gs.konick.dao;

import gs.konick.model.Order;

import java.util.List;

public interface OrderDao extends BaseDao {

    /**
     * Получение заказов конкретного юзера
     */
    List<Order> getUserOrders(long userId);

    List<Order> getAllOrders(long userId);

    /**
     * Получение заказов конкретного статуса
     */
    List<Order> getFilteredOrdersByStatus(long statusId);

    void changeStatus(long id, long statusId);

    void deleteOrder(long id);
}
