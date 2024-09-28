package gs.konick.dao;

import gs.konick.model.User;

import java.util.List;

public interface UserDao {

    User getUserByLogin(String login);

    List<User> getAllUsers();

    User getUserById(Long id);

    User logIn(String login, String password);

    User signUp(String login, String password, String email, String address);

    void changePassword(long userId, String password);

    void changeRole(long userId, int roleId);

    void deleteUserById(long id);
}
