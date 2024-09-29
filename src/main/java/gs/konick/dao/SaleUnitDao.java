package gs.konick.dao;

import gs.konick.model.SaleUnit;

import java.util.List;

public interface SaleUnitDao extends BaseDao {
    SaleUnit getSaleUnitById(long id);

    SaleUnit getSaleUnitByName(String name);

    List<SaleUnit> getAllSaleUnits();

    List<SaleUnit> getFilteredSaleUnitsByCategoryId(long categoryId);

    void changePrice(long id, long price);

    SaleUnit addSaleUnit(String name, long categoryId, long price);

    void deleteSaleUnit(long id);
}
