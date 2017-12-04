package ru.javaops.masterjava.persist.testdata;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.GroupType;

import java.util.List;

import static ru.javaops.masterjava.persist.testdata.ProjectTestData.*;

public class GroupTestData {
    public static Group MJ1;
    public static Group TJ6;
    public static Group TJ7;
    public static Group TJ8;
    public static List<Group> GROUPS;
    public static Group NEWGROUP;

    public static void init() {
        MJ1 = new Group("masterjava01", GroupType.CURRENT, MASTERJAVA);
        TJ6 = new Group("topjava06", GroupType.FINISHED, TOPJAVA);
        TJ7 = new Group("topjava07", GroupType.FINISHED, TOPJAVA);
        TJ8 = new Group("topjava07", GroupType.CURRENT, TOPJAVA);
        NEWGROUP = new Group("new group", GroupType.REGISTERING, TOPJAVA);
        GROUPS = ImmutableList.of(MJ1, TJ6, TJ7, TJ8);
    }

    public static void setUp() {
        GroupDao dao = DBIProvider.getDao(GroupDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            GROUPS.forEach(dao::insert);
        });
    }
}