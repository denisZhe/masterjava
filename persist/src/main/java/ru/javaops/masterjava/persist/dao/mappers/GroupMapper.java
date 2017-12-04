package ru.javaops.masterjava.persist.dao.mappers;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.GroupType;
import ru.javaops.masterjava.persist.model.Project;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupMapper implements ResultSetMapper<Group> {
    @Override
    public Group map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        Project project = new Project(
                r.getInt("project_id"),
                r.getString("project"),
                r.getString("description"));
        return new Group(r.getInt("id"),
                r.getString("name"),
                GroupType.valueOf(r.getString("type")),
                project);

    }
}