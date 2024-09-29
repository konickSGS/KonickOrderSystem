package gs.konick.dao.impl;

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

public class UserDaoImpl implements UserDao {

    private static UserDao INSTANCE;

    private UserDaoImpl() {
    }

    public static synchronized UserDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserDaoImpl();
        }
        return INSTANCE;
    }

    /**
     * Создаем user из текущей строки таблицы
     * @param resultSet - текущий resultSet
     * @return user
     */
    private static User makeUser(ResultSet resultSet) {
        try {
            int idColumn = resultSet.findColumn("id");
            int loginColumn = resultSet.findColumn("login");
            int hashedPasswordColumn = resultSet.findColumn("hashed_password");
            int roleColumn = resultSet.findColumn("role_id");
            int emailColumn = resultSet.findColumn("email");
            int addressColumn = resultSet.findColumn("address");
            int dateColumn = resultSet.findColumn("create_date");
            return new User.Builder()
                    .setId(resultSet.getLong(idColumn))
                    .setLogin(resultSet.getString(loginColumn))
                    .setHashedPassword(resultSet.getString(hashedPasswordColumn))
                    .setRole(resultSet.getInt(roleColumn))
                    .setEmail(resultSet.getString(emailColumn))
                    .setAddress(resultSet.getString(addressColumn))
                    .setCreateDate(resultSet.getDate(dateColumn))
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException("В таблице нет колонки с таким именем", e);
        }
    }

    @Override
    public User getUserByLogin(String login) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UserSqlQuery.FIND_USER_BY_LOGIN);) {
            preparedStatement.setString(1, login);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Так как login UNIQUE, то такая строка может быть только одна
                if (!resultSet.next()) return null;
                return makeUser(resultSet);
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
                users.add(makeUser(resultSet));
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
                return makeUser(resultSet);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User logIn(String login, String password) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UserSqlQuery.LOG_IN)) {
            preparedStatement.setString(1, login);
            String hashPassword = HashPassword.hash(password);
            preparedStatement.setString(1, hashPassword);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) return null;
                return makeUser(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User signUp(String login, String unhashedPassword, String email, String address) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UserSqlQuery.SIGN_UP)) {

            String hashPassword = HashPassword.hash(unhashedPassword);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, hashPassword);
            //preparedStatement.setInt(3, roleId);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, address);

            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalArgumentException("Не получилось создать нового пользователя");
            }

            return getUserByLogin(login);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
    public void deleteUserById(long id) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UserSqlQuery.DELETE_USER)) {
            preparedStatement.setLong(1, id);
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalArgumentException("Не получилось удалить пользователя");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
