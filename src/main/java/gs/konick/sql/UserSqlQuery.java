package gs.konick.sql;

public class UserSqlQuery extends SqlQuery {
    private static final String tableName = "users";

    public static final String FIND_USER_BY_LOGIN = "SELECT * FROM " + tableName + " WHERE login LIKE ?";
    public static final String FIND_USER_BY_ID    = "SELECT * FROM " + tableName + " WHERE id = ?";
    public static final String GET_ALL_USERS      = "SELECT * FROM " + tableName;
    public static final String LOG_IN             = "SELECT * FROM " + tableName + " WHERE login LIKE ? AND hashed_password LIKE ?";
    public static final String CHANGE_PASSWORD    = "UPDATE " + tableName + " SET hashed_password = ? WHERE id = ?";
    public static final String CHANGE_ROLE        = "UPDATE " + tableName + " SET role_id = ? WHERE id = ?";
    //public static final String SIGN_UP_WITH_ROLE  = "INSERT INTO " + tableName + " (login, hashed_password, role_id, email, address) VALUES (?, ?, ?, ?, ?)";
    public static final String SIGN_UP            = "INSERT INTO " + tableName + " (login, hashed_password, email, address) VALUES (?, ?, ?, ?)";
    public static final String DELETE_USER        = "DELETE FROM " + tableName + " WHERE id = ?";
}
