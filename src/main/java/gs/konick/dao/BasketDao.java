package gs.konick.dao;

import gs.konick.model.SaleUnit;

import java.util.Map;

public interface BasketDao extends BaseDao {
    /**
     * Добавление заказа в корзину
     */
    void addSaleUnitToBasket(long userId, long saleUnitId, int count);

    /**
     * Получение корзины юзера
     */
    Map<SaleUnit, Integer> getBasket(long userId);

    void makeOrder(long userId, Map<SaleUnit, Integer> basket);

    void removeSaleUnitFromBasket(long userId, long saleUnitId);

    void cleanBasket(long userId);
}
