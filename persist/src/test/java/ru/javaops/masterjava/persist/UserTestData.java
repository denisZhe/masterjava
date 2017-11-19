package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.User;
import ru.javaops.masterjava.persist.model.UserFlag;

import java.util.List;

public class UserTestData {
    public static User ADMIN;
    public static User DELETED;
    public static User FULL_NAME;
    public static User USER1;
    public static User USER2;
    public static User USER3;
    public static List<User> FIST5_USERS;

    public static User NEW_USER1;
    public static User NEW_USER2;
    public static User NEW_USER3;
    public static User NEW_USER3_2;
    public static User NEW_USER4;
    public static User NEW_USER5;
    public static User NEW_USER6;
    public static List<User> NEW_USERS;

    public static void init() {
        ADMIN = new User("Admin", "admin@javaops.ru", UserFlag.superuser);
        DELETED = new User("Deleted", "deleted@yandex.ru", UserFlag.deleted);
        FULL_NAME = new User("Full Name", "gmail@gmail.com", UserFlag.active);
        USER1 = new User("User1", "user1@gmail.com", UserFlag.active);
        USER2 = new User("User2", "user2@yandex.ru", UserFlag.active);
        USER3 = new User("User3", "user3@yandex.ru", UserFlag.active);
        FIST5_USERS = ImmutableList.of(ADMIN, DELETED, FULL_NAME, USER1, USER2);

        NEW_USER1 = new User("NewUser1", "newuser1@yandex.ru", UserFlag.active);
        NEW_USER2 = new User("NewUser2", "newuser2@yandex.ru", UserFlag.active);
        NEW_USER3 = new User("NewUser3", "newuser3@yandex.ru", UserFlag.active);
        NEW_USER3_2 = new User("NewUser3_2", "newuser3@yandex.ru", UserFlag.active);
        NEW_USER4 = new User("NewUser4", "newuser4@yandex.ru", UserFlag.active);
        NEW_USER5 = new User("NewUser5", "newuser5@yandex.ru", UserFlag.active);
        NEW_USER6 = new User("NewUser6", "newuser6@yandex.ru", UserFlag.active);
        NEW_USERS = ImmutableList.of(NEW_USER1, NEW_USER2, NEW_USER3, NEW_USER3_2, NEW_USER4, NEW_USER5, NEW_USER6);
    }

    public static void setUp() {
        UserDao dao = DBIProvider.getDao(UserDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            FIST5_USERS.forEach(dao::insert);
            dao.insert(USER3);
        });
    }
}
