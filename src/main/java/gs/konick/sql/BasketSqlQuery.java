package gs.konick.sql;

public class BasketSqlQuery extends SqlQuery {
    private static final String manyToManyTable = "basket_and_saleunit";
    private static final String tableName2 = "saleunits";


    public static final String GET_BASKET                  = "SELECT saleunit_id, name, category_id, price, weight, description, count FROM " + manyToManyTable + " AS t1 INNER JOIN " + tableName2 + " as t2 ON t2.id = t1.saleunit_id WHERE t1.user_id = ?";
    public static final String ADD_SALEUNIT_TO_BASKET      = "INSERT INTO " + manyToManyTable + " (user_id, saleunit_id, count) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE count = ?";
    public static final String CLEAN_USER_BASKET           = "DELETE FROM " + manyToManyTable + " WHERE user_id = ?";
    public static final String REMOVE_SALEUNIT_FROM_BASKET = "DELETE FROM " + manyToManyTable + " WHERE user_id = ? AND saleunit_id = ?";

}
