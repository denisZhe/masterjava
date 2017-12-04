package ru.javaops.masterjava.persist.testdata;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Project;

import java.util.List;

public class ProjectTestData {
    public static Project MASTERJAVA;
    public static Project TOPJAVA;
    public static List<Project> PROJECTS;
    public static Project NEWPROJECT;

    public static void init() {
        MASTERJAVA = new Project("masterjava", "Masterjava");
        TOPJAVA = new Project("topjava", "Topjava");
        NEWPROJECT = new Project("new project", "New project for test");
        PROJECTS = ImmutableList.of(MASTERJAVA, TOPJAVA);
    }

    public static void setUp() {
        ProjectDao dao = DBIProvider.getDao(ProjectDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            PROJECTS.forEach(dao::insert);
        });
    }
}
