package gs.konick.dao;

import gs.konick.model.SaleUnit;

import java.util.List;
import java.util.Map;

public interface OrderAndSaleUnitDao {

    List<Map<SaleUnit, Integer>> getAllSaleUnitsByOrderId(long orderId);

    /**
     * Добавляем в таблицу Many to Many order_and_saleunit
     */
    void addOrderAndSaleUnit(long orderId, long saleUnitId, int count);

}
