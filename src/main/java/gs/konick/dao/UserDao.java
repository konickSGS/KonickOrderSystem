package gs.konick.dao;

import gs.konick.model.User;

import java.util.List;

public interface UserDao extends Dao<User, Long> {

    User getUserByPassword(String login, char[] password);

    User getUserByLogin(String login);

    List<User> getAllUsers();

    void changeRole(long userId, int roleId);
}
