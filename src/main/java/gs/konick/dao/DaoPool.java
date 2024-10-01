package gs.konick.dao;

import gs.konick.dao.impl.*;

public class DaoPool {
    public static UserDao userDao = UserDaoImpl.getInstance();
    public static SaleUnitDao saleUnitDao = SaleUnitDaoImpl.getInstance();
    public static OrderDao orderDao = OrderDaoImpl.getInstance();
    public static OrderAndSaleUnitDao orderAndSaleUnitDao = OrderAndSaleUnitDaoImpl.getInstance();
    public static CategoryDao categoryDao = CategoryDaoImpl.getInstance();
}
