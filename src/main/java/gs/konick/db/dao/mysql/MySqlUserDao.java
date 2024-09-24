package gs.konick.db.dao.mysql;

import gs.konick.db.ConnectionPool;
import gs.konick.db.dao.UserDao;
import gs.konick.model.User;
import gs.konick.sql.UserSqlQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class MySqlUserDao implements UserDao {
    @Override
    public User getUserByPassword(String login, char[] password) {
        return null;
    }

    @Override
    public User getUserByLogin(String login) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(UserSqlQuery.FIND_USER_BY_LOGIN)) {
            ps.setString(1, login);

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка getUserByLogin", e);
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return List.of();
    }

    @Override
    public void changeRole(long userId, int roleId) {

    }
}
