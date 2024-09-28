package gs.konick.db.dao.mysql;

import gs.konick.Main;
import gs.konick.dao.UserDao;
import gs.konick.dao.mysql.MySqlUserDao;
import gs.konick.db.ConnectionPool;
import gs.konick.db.dao.BaseDaoTest;
import gs.konick.model.User;
import gs.konick.utils.HashPassword;
import gs.konick.utils.SQLUtils;
import gs.konick.utils.Utils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Класс для тестирования MySqlUserDao. Работает с тестовой базой данных
 */
public class MySqlUserDaoTest extends BaseDaoTest {

    /**
     * SQL скрипты и пути к ним, которые создают таблицы и добавляют их содержимое
     */
    private static final String CREATE_TABLES_USERS_AND_ROLES_PATH = "sql/create_tables_users_and_roles.sql";
    private static final String CREATE_TABLES_USERS_AND_ROLES = Utils.makeStringFromFile(CREATE_TABLES_USERS_AND_ROLES_PATH);
    private static final String INSERT_USERS_AND_ROLES_PATH = "sql/user_test/insert_into_users.sql";
    private static final String INSERT_USERS_AND_ROLES = Utils.makeStringFromFile(INSERT_USERS_AND_ROLES_PATH);

    private static UserDao userDao = MySqlUserDao.getInstance();

    @BeforeAll
    static void createTablesAndInsertValues() {
        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            SQLUtils.executeSeveralQueries(connection, CREATE_TABLES_USERS_AND_ROLES);
            SQLUtils.executeSeveralQueries(connection, INSERT_USERS_AND_ROLES);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("Проверка функций getUserById и changePassword")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @MethodSource("provideChangePassword")
    public void changePassword(long id, String newPassword) {
        userDao.changePassword(id, newPassword);
        String newHashedPassword = userDao.getUserById(id).getHashedPassword();

        String expectedNewHashedPassword = HashPassword.hash(newPassword);
        Assertions.assertEquals(expectedNewHashedPassword, newHashedPassword, "Пароль не изменился");
    }

    public static Stream<Arguments> provideChangePassword() {
        return Stream.of(
                Arguments.of(1, "newPassword" + Math.random()),
                Arguments.of(2, "newPassword" + Math.random()),
                Arguments.of(3, "newPassword" + Math.random())
        );
    }

    @DisplayName("Проверка функций getUserById и changeRole")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @MethodSource("provideChangeRole")
    public void changePassword(long id, int expectedRoleId) {
        int oldRoleId = userDao.getUserById(id).getRole().getId();
        userDao.changeRole(id, expectedRoleId);
        int actualRoleId = userDao.getUserById(id).getRole().getId();

        Assertions.assertEquals(expectedRoleId, actualRoleId);

        // Возвращение старого id
        userDao.changeRole(id, oldRoleId);
        int roleIdAfterOldReturn = userDao.getUserById(id).getRole().getId();
        Assertions.assertEquals(oldRoleId, roleIdAfterOldReturn);
    }

    public static Stream<Arguments> provideChangeRole() {
        return Stream.of(
                Arguments.of(1, 1),
                Arguments.of(2, 1),
                Arguments.of(3, 2)
        );
    }

    @DisplayName("Проверка функций signup, getallusers и delete")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @MethodSource("provideGetAllUsersAndSignUpAndDelete")
    public void getAllUsersAndSignUpAndDelete(List<List<String>> newUsersStrings) {
        int sizeBeforeInsert = userDao.getAllUsers().size();
        int sizeNewUsers = newUsersStrings.size();

        newUsersStrings.stream().forEach(l -> {
            userDao.signUp(l.get(0), l.get(1), l.get(2), l.get(3));
        });

        int sizeAfterInsert = userDao.getAllUsers().size();
        // Проверяем, что длина getAllUsers увеличилось
        Assertions.assertEquals(sizeAfterInsert, sizeBeforeInsert + sizeNewUsers);
        // Проверяем, что строки добавились правильно
        newUsersStrings.forEach(l -> {
                    String currentLogin = l.get(0);
                    User currentUser = userDao.getUserByLogin(currentLogin);
                    Assertions.assertAll("Проверка соответствующих строк при добавлении",
                            () -> Assertions.assertEquals(currentLogin, currentUser.getLogin()),
                            () -> Assertions.assertEquals(HashPassword.hash(l.get(1)), currentUser.getHashedPassword()),
                            () -> Assertions.assertEquals(l.get(2), currentUser.getEmail()),
                            () -> Assertions.assertEquals(l.get(3), currentUser.getAddress())
                    );
                });

        List<User> newUsers = newUsersStrings.stream()
                .map(l -> userDao.getUserByLogin(l.get(0)))
                .toList();

        newUsers.forEach(user -> {
                    long currentId = user.getId();
                    userDao.deleteUserById(currentId);
                });

        // Проверяем, что строки удалились правильно
        int sizeAfterDelete = userDao.getAllUsers().size();
        Assertions.assertEquals(sizeBeforeInsert, sizeAfterDelete,
                "После удаления остались строки");
    }

    public static Stream<Arguments> provideGetAllUsersAndSignUpAndDelete() {
        List<List<String>> newUsers1 = List.of(
                List.of("1user1", "1password1", "1email1.com", "1address1"),
                List.of("1user2", "1password2", "1email2.com", "1address2"),
                List.of("1user3", "1password3", "1email3.com", "1address3")
        );

        List<List<String>> newUsers2 = List.of(
                List.of("2user1", "2password1", "2email1.com", "2address1")
        );

        return Stream.of(
                Arguments.of(newUsers1),
                Arguments.of(newUsers2)
        );
    }

    @DisplayName("Проверка функции getUserByLogin")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @MethodSource("provideGetUserByLogin")
    public void getUserByLoginTest(String login, long expectedId) throws SQLException {
        long actualId = userDao.getUserByLogin(login).getId();
        Assertions.assertEquals(expectedId, actualId);
    }

    public static Stream<Arguments> provideGetUserByLogin() {
        return Stream.of(
                Arguments.of("admin", 1),
                Arguments.of("manager1", 2),
                Arguments.of("user1", 3)
        );
    }
}
