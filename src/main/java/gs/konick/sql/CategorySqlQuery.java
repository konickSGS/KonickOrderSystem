package gs.konick.sql;

public class CategorySqlQuery extends SqlQuery {
    private static final String tableName = "categories";

    public static final String FIND_CATEGORY_BY_ID   = "SELECT * FROM " + tableName + " WHERE ID = ?;";
    public static final String FIND_CATEGORY_BY_NAME = "SELECT * FROM " + tableName + " WHERE NAME LIKE ?;";
    public static final String GET_ALL_CATEGORIES    = "SELECT * FROM " + tableName;
    public static final String CHANGE_NAME           = "UPDATE " + tableName + " SET name = ? WHERE id = ?";
    public static final String ADD_CATEGORY          = "INSERT INTO " + tableName + " (name) VALUES (?);";
    public static final String DELETE_CATEGORY       = "DELETE FROM " + tableName + " WHERE id = ?";
}
