package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.model.Project;
import ru.javaops.masterjava.persist.testdata.ProjectTestData;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.javaops.masterjava.persist.testdata.ProjectTestData.NEWPROJECT;
import static ru.javaops.masterjava.persist.testdata.ProjectTestData.PROJECTS;

public class ProjectDaoTest extends AbstractDaoTest<ProjectDao> {

    public ProjectDaoTest() {
        super(ProjectDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        ProjectTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        ProjectTestData.setUp();
    }

    @Test
    public void insert() {
        Project project = dao.insert(NEWPROJECT);
        assertTrue(dao.getAll().contains(project));
    }

    @Test
    public void getAll() {
        List<Project> projects = dao.getAll();
        assertEquals(PROJECTS, projects);
    }
}
