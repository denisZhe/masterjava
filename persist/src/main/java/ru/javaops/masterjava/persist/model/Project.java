package ru.javaops.masterjava.persist.model;

import lombok.*;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Project extends BaseEntity {
    private @NonNull String project;
    private @NonNull String description;

    public Project(Integer id, String project, String description) {
        this(project, description);
        this.id=id;
    }
}
