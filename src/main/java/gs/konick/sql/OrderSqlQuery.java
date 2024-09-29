package gs.konick.sql;

public class OrderSqlQuery extends SqlQuery {
    private static final String tableName = "orders";

    public static final String FIND_ORDER_BY_ID              = "SELECT * FROM " + tableName + " WHERE id = ?";
    public static final String FIND_ALL_ORDERS_BY_USER_ID    = "SELECT * FROM " + tableName + " WHERE user_id = ?;";
    public static final String GET_ALL_ORDERS                = "SELECT * FROM " + tableName;
    public static final String GET_FILTERED_ORDERS_BY_STATUS = "SELECT * FROM " + tableName + " WHERE status_id = ?;";
    public static final String CHANGE_STATUS                 = "UPDATE " + tableName + " SET status_id = ? WHERE id = ?;";
    public static final String ADD_ORDER                     = "INSERT INTO " + tableName + " (user_id) VALUES (?)";
    public static final String DELETE_ORDER                  = "DELETE FROM " + tableName + " WHERE id = ?;";
}
