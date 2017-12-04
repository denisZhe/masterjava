package ru.javaops.masterjava.persist.model;

import lombok.*;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class City extends BaseEntity {
    private @NonNull String city;
    private @NonNull String code;

    public City(Integer id, String city, String code) {
        this(city, code);
        this.id=id;
    }
}
