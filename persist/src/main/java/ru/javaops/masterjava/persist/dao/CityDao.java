package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class CityDao implements AbstractDao {

    public City insert(City city) {
        if (city.isNew()) {
            int id = insertGeneratedId(city);
            city.setId(id);
        } else {
            insertWitId(city);
        }
        return city;
    }

    @SqlUpdate("INSERT INTO cities (city, code) VALUES (:city, :code)")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean City city);

    @SqlUpdate("INSERT INTO cities (id, city, code) VALUES (:id, :city, :code)")
    abstract void insertWitId(@BindBean City city);

    @SqlBatch("INSERT INTO cities (city, code) VALUES (:city, :code) ON CONFLICT (code) DO NOTHING")
    public abstract int[] insertBatch(@BindBean List<City> cities);

    @SqlQuery("SELECT * FROM cities ORDER BY city")
    public abstract List<City> getAll();

    @SqlUpdate("TRUNCATE cities CASCADE")
    @Override
    public abstract void clean();
}
