package gs.konick.dao.mysql;

import gs.konick.db.ConnectionPool;
import gs.konick.dao.UserDao;
import gs.konick.model.User;
import gs.konick.sql.UserSqlQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySqlUserDao implements UserDao {

    private static UserDao INSTANCE;

    private MySqlUserDao() {}

    public static synchronized UserDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MySqlUserDao();
        }
        return INSTANCE;
    }

    private static User makeUser(ResultSet resultSet) throws SQLException {
        int columnIndex = 0;
        return new User.Builder()
                .setId(resultSet.getLong(++columnIndex))
                .setLogin(resultSet.getString(++columnIndex))
                .setRole(resultSet.getLong(++columnIndex))
                .setEmail(resultSet.getString(++columnIndex))
                .setAddress(resultSet.getString(++columnIndex))
                .setCreateDate(resultSet.getDate(++columnIndex))
                .build();

    }

    @Override
    public User getUserByPassword(String login, char[] password) {
        return null;
    }

    @Override
    public User getUserByLogin(String login) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(UserSqlQuery.FIND_USER_BY_LOGIN)) {
            ps.setString(1, login);

            try (ResultSet resultSet = ps.executeQuery()) {
                // Так как login UNIQUE, то такая строка может быть только одна
                if (!resultSet.next()) return null;
                return makeUser(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка getUserByLogin", e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return List.of();
    }

    @Override
    public void changeRole(long userId, int roleId) {

    }

    @Override
    public User save(User object) {
        return null;
    }

    @Override
    public Boolean delete(User object) {
        return null;
    }

    @Override
    public User update(User object) {
        return null;
    }

    @Override
    public User find(Long id) {
        return null;
    }
}
