package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.dao.mappers.GroupMapper;
import ru.javaops.masterjava.persist.model.Group;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class GroupDao implements AbstractDao {

    public Group insert(Group group) {
        if (group.isNew()) {
            int id = insertGeneratedId(group, group.getProject().getId());
            group.setId(id);
        } else {
            insertWitId(group, group.getProject().getId());
        }
        return group;
    }

    @SqlUpdate("INSERT INTO groups (name, type, project_id) VALUES (:name, CAST(:type AS GROUP_TYPE), :projectId)")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean Group group, @Bind("projectId") int projectId);

    @SqlUpdate("INSERT INTO groups (id, name, type, project_id) VALUES (:id, :name, CAST(:type AS GROUP_TYPE), :projectId)")
    abstract void insertWitId(@BindBean Group group, @Bind("projectId") int projectId);

    @SqlBatch("INSERT INTO groups (name, type, project_id) VALUES (:name, CAST(:type AS GROUP_TYPE), :projectId) ON CONFLICT DO NOTHING")
    public abstract int[] insertBatch(@BindBean List<Group> groups, @Bind("projectId") List<Integer> projectIds);

    @SqlQuery("SELECT groups.*, projects.* FROM groups LEFT JOIN projects ON groups.project_id = projects.id ORDER BY groups.name")
    @Mapper(GroupMapper.class)
    public abstract List<Group> getAll();

    @SqlUpdate("TRUNCATE groups")
    @Override
    public abstract void clean();
}
