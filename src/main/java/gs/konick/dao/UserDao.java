package gs.konick.dao;

import gs.konick.model.User;

import java.util.List;

public interface UserDao {

    User getUserByLogin(String login);

    List<User> getAllUsers();

    User getUserById(Long id);

    User logIn(String login, String password);

    void signUp(String login, String password);

    void changePassword(long userId, String password);

    void changeRole(long userId, int roleId);
}
