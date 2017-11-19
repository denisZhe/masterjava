package ru.javaops.masterjava.persist.dao;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.UserTestData;
import ru.javaops.masterjava.persist.model.User;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.javaops.masterjava.persist.UserTestData.FIST5_USERS;
import static ru.javaops.masterjava.persist.UserTestData.NEW_USERS;

public class UserDaoTest extends AbstractDaoTest<UserDao> {

    public UserDaoTest() {
        super(UserDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        UserTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        UserTestData.setUp();
    }

    @Test
    public void getWithLimit() {
        List<User> users = dao.getWithLimit(5);
        assertEquals(FIST5_USERS, users);
    }

    @Test
    public void testInsertBatch() {
        int[] users_id = dao.insertBatch(NEW_USERS);
        List<User> users = dao.getWithLimit(100);
        assertTrue(users.size() == 12);
        assertTrue(users_id.length == 6);
    }
}