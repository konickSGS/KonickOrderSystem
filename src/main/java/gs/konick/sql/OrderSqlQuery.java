package gs.konick.sql;

public class OrderSqlQuery extends SqlQuery {
    private static final String tableName = "orders";
    private static final String manyToManyTable1 = "order_and_saleunit";
    private static final String manyToManyTable2 = "basket_and_saleunit";

    public static final String FIND_ALL_ORDERS_BY_USER_ID    = "SELECT * FROM " + tableName + " WHERE user_id = ?;";
    public static final String GET_ALL_ORDERS                = "SELECT * FROM " + tableName;
    public static final String GET_FILTERED_ORDERS_BY_STATUS = "SELECT * FROM " + tableName + " WHERE status_id = ?;";
    public static final String ADD_ORDER                     = "INSERT INTO " + tableName + " (user_id) VALUES (?)";
    public static final String ADD_ORDER_HAS_DISH            = "INSERT INTO " + manyToManyTable1 + " (order_id, saleunit_id, count, price) VALUES (?, ?, ?, ?)";
    public static final String REMOVE_DISH_FROM_CART         = "DELETE FROM " + manyToManyTable2 + " WHERE user_id = ? AND saleunit_id = ?";
}
