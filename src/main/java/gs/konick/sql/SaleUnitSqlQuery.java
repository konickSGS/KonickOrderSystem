package gs.konick.sql;

public class SaleUnitSqlQuery extends SqlQuery {
    private static final String tableName = "saleunits";

    public static final String FIND_SALEUNIT_BY_ID          = "SELECT * FROM " + tableName + " WHERE id = ?";
    public static final String GET_ALL_SALEUNITS            = "SELECT * FROM " + tableName;
    public static final String FILTER_SALEUNITS_BY_CATEGORY = "SELECT * FROM " + tableName + " WHERE category_id = ?";
    public static final String CHANGE_PRICE                 = "UPDATE " + tableName + " SET price = ? WHERE id = ?";
    public static final String ADD_SALEUNIT                 = "INSERT INTO " + tableName + " (name, category_id, price) VALUES (?, ?, ?)";
    public static final String DELETE_SALEUNIT              = "DELETE FROM " + tableName + " WHERE id = ?";
}
