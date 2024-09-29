package gs.konick.sql;

public class OrderAndSaleUnitSqlQuery {
    private static final String manyToManyTable = "order_and_saleunit";

    public static final String FIND_ALL_SALEUNITS_BY_ORDERID = "SELECT saleunit_id, count FROM " + manyToManyTable + " WHERE order_id = ?;";
    public static final String ADD_ORDER_AND_SALEUNIT        = "INSERT INTO " + manyToManyTable + " (order_id, saleunit_id, count) VALUES (?, ?, ?);";
}
