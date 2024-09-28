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

    /**
     * Создаем user из текущей строки таблицы
     * @param resultSet - текущий resultSet
     * @return user
     */
    private static User makeUser(ResultSet resultSet) {
        try {
            return new User.Builder()
                    .setId(resultSet.getLong(resultSet.findColumn("id")))
                    .setLogin(resultSet.getString(resultSet.findColumn("login")))
                    .setHashedPassword(resultSet.getString(resultSet.findColumn("hashed_password")))
                    .setRole(resultSet.getInt(resultSet.findColumn("role_id")))
                    .setEmail(resultSet.getString(resultSet.findColumn("email")))
                    .setAddress(resultSet.getString(resultSet.findColumn("address")))
                    .setCreateDate(resultSet.getDate(resultSet.findColumn("create_date")))
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

    /**
     * Получение всех users из таблицы
     * @return list юзеров
     */
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

    /**
     * Получение user из таблицы по id
     * @param id - id
     * @return user или null, если такой строки нет
     */
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

    /**
     * Возвращение User по login и password
     * @param login    - user login
     * @param password - user password
     * @return user(login, password). Если такого user нет, то null
     */
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

    /**
     * Создание новой строки user
     */
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
