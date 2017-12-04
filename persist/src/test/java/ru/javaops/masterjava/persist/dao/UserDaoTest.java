package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.testdata.CityTestData;
import ru.javaops.masterjava.persist.testdata.UserTestData;
import ru.javaops.masterjava.persist.model.User;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static ru.javaops.masterjava.persist.testdata.UserTestData.FIST5_USERS;

public class UserDaoTest extends AbstractDaoTest<UserDao> {

    public UserDaoTest() {
        super(UserDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        CityTestData.init();
        UserTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        CityTestData.setUp();
        UserTestData.setUp();
    }

    @Test
    public void getWithLimit() {
        List<User> users = dao.getWithLimit(5);
        assertEquals(FIST5_USERS, users);
    }

    @Test
    public void insertBatch() throws Exception {
        dao.clean();
        List<Integer> cityIds = FIST5_USERS.stream().map(user -> user.getCity().getId()).collect(Collectors.toList());
        dao.insertBatch(FIST5_USERS, cityIds, 3);
        assertEquals(5, dao.getWithLimit(100).size());
    }

    @Test
    public void getSeqAndSkip() throws Exception {
        int seq1 = dao.getSeqAndSkip(5);
        int seq2 = dao.getSeqAndSkip(1);
        assertEquals(5, seq2 - seq1);
    }
}