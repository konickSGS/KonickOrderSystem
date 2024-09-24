package gs.konick.sql;

public class UserSqlQuery extends SqlQuery {
    public static final String LOG_IN = "SELECT * FROM user WHERE login LIKE ? AND password LIKE ?";
    public static final String SIGN_UP = "INSERT INTO user (login, password) VALUES (?, ?)";
    public static final String FIND_USER_BY_LOGIN = "SELECT * FROM user WHERE login LIKE ?";
    public static final String CHANGE_PASSWORD = "UPDATE user SET password = ? WHERE id = ?";
    public static final String GET_ALL_USERS = "SELECT * FROM user";
    public static final String CHANGE_ROLE = "UPDATE user SET role_id = ? WHERE id = ?";
    public static final String DELETE_USER = "DELETE FROM user WHERE id = ?";
}
