package gs.konick.dao;

import gs.konick.model.User;

import java.util.List;

public interface UserDao {

    User getUserByLogin(String login);

    /**
     * Получение всех users из таблицы
     * @return list юзеров
     */
    List<User> getAllUsers();

    /**
     * Получение user из таблицы по id
     * @param id - id
     * @return user или null, если такой строки нет
     */
    User getUserById(Long id);

    /**
     * Возвращение User по login и password
     * @param login    - user login
     * @param password - user password
     * @return user(login, password). Если такого user нет, то null
     */
    User logIn(String login, String password);

    /**
     * Создание новой строки user
     */
    User signUp(String login, String password, String email, String address);

    void changePassword(long userId, String password);

    void changeRole(long userId, int roleId);

    void deleteUserById(long id);
}
