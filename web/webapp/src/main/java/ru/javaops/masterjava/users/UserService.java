package ru.javaops.masterjava.users;

import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    private UserDao dao = DBIProvider.getDao(UserDao.class);

    public List<User> getWithLimit(int limit) {
        List<User> users = new ArrayList<>();
        users = dao.getWithLimit(limit);
        return users;
    }
}
