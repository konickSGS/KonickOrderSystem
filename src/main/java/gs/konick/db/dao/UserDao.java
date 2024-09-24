package gs.konick.db.dao;

import gs.konick.model.User;

import java.util.List;

public interface UserDao {

    /**
     * Получение пользователя по паролю.
     * @param login
     * @param password
     * @return
     */
    User getUserByPassword(String login, char[] password);

    User getUserByLogin(String login);

    List<User> getAllUsers();

    void changeRole(long userId, int roleId);
}
