package gs.konick.dao;

import gs.konick.dao.impl.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BaseDao {
    public static UserDao userDao = UserDaoImpl.getInstance();
    public static SaleUnitDao saleUnitDao = SaleUnitDaoImpl.getInstance();
    public static OrderDao orderDao = OrderDaoImpl.getInstance();
    public static OrderAndSaleUnitDao orderAndSaleUnitDao = OrderAndSaleUnitDaoImpl.getInstance();
    public static CategoryDao categoryDao = CategoryDaoImpl.getInstance();
}
