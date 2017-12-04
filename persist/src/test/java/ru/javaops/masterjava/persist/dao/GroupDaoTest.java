package ru.javaops.masterjava.persist.dao;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.testdata.GroupTestData;
import ru.javaops.masterjava.persist.testdata.ProjectTestData;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.javaops.masterjava.persist.testdata.GroupTestData.GROUPS;
import static ru.javaops.masterjava.persist.testdata.GroupTestData.NEWGROUP;

public class GroupDaoTest extends AbstractDaoTest<GroupDao> {
    public GroupDaoTest() {
        super(GroupDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        ProjectTestData.init();
        GroupTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        ProjectTestData.setUp();
        GroupTestData.setUp();
    }

    @Test
    public void insert() {
        Group group = dao.insert(NEWGROUP);
        assertTrue(dao.getAll().contains(group));
    }

    @Test
    public void insertBatch() {
        dao.clean();
        List<Integer> projectIds = GROUPS.stream().map(group -> group.getProject().getId()).collect(Collectors.toList());
        int[] insertedGroups = dao.insertBatch(GROUPS, projectIds);
        assertTrue(insertedGroups.length == GROUPS.size());
    }

    @Test
    public void getAll() {
        List<Group> groups = dao.getAll();
        assertEquals(GROUPS, groups);
    }
}
