package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Group extends BaseEntity{
    private @NonNull String name;
    private @NonNull GroupType type;
    @Column("project_id")
    private @NonNull Project project;

    public Group(Integer id, String name, GroupType type, Project project) {
        this(name, type, project);
        this.id=id;
    }
}
