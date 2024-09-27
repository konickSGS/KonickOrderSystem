package gs.konick.dao.mysql;

import gs.konick.db.ConnectionPool;
import gs.konick.dao.UserDao;
import gs.konick.model.User;
import gs.konick.sql.UserSqlQuery;
import gs.konick.utils.HashPassword;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlUserDao implements UserDao {

    private static UserDao INSTANCE;

    private MySqlUserDao() {
    }

    public static synchronized UserDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MySqlUserDao();
        }
        return INSTANCE;
    }

    @Override
    public User getUserByLogin(String login) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UserSqlQuery.FIND_USER_BY_LOGIN);) {
            preparedStatement.setString(1, login);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Так как login UNIQUE, то такая строка может быть только одна
                if (!resultSet.next()) return null;
                return new User(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UserSqlQuery.GET_ALL_USERS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                users.add(new User(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    @Override
    public User getUserById(Long id) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UserSqlQuery.FIND_USER_BY_ID)) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Так как id - Key, то такая строка может быть только одна
                if (!resultSet.next()) return null;
                return new User(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User logIn(String login, String password) {
        return null;
    }

    @Override
    public void signUp(String login, String password) {

    }

    @Override
    public User save(User object) {
        return null;
    }

    @Override
    public void changeRole(long userId, int roleId) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UserSqlQuery.CHANGE_ROLE)) {
            preparedStatement.setInt(1, roleId);
            preparedStatement.setLong(2, userId);

            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalArgumentException("Неправильный аргумент (или аргументы) при смене роли");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changePassword(long userId, String password) {
        String hashPassword = HashPassword.hash(password);

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UserSqlQuery.CHANGE_PASSWORD)) {
            preparedStatement.setString(1, hashPassword);
            preparedStatement.setLong(2, userId);

            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalArgumentException("Ошибка при попытке сменить пароль");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User update(User object) {
        return null;
    }

    @Override
    public Boolean delete(User object) {
        return null;
    }
}
