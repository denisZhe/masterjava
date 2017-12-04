package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import one.util.streamex.IntStreamEx;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.mappers.UserMapper;
import ru.javaops.masterjava.persist.model.User;

import java.util.List;
import java.util.stream.Collectors;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class UserDao implements AbstractDao {

    public User insert(User user) {
        if (user.isNew()) {
            int userId = insertGeneratedId(user, user.getCity().getId());
            user.setId(userId);
        } else {
            insertWitId(user, user.getCity().getId());
        }
        return user;
    }

    @SqlQuery("SELECT nextval('user_seq')")
    abstract int getNextVal();

    @Transaction
    public int getSeqAndSkip(int step) {
        int id = getNextVal();
        DBIProvider.getDBI().useHandle(h -> h.execute("ALTER SEQUENCE user_seq RESTART WITH " + (id + step)));
        return id;
    }

    @SqlUpdate("INSERT INTO users (full_name, email, flag, city_id) VALUES (:fullName, :email, CAST(:flag AS USER_FLAG), :cityId) ")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean User user, @Bind("cityId") int cityId);

    @SqlUpdate("INSERT INTO users (id, full_name, email, flag, city_id) VALUES (:id, :fullName, :email, CAST(:flag AS USER_FLAG), :cityId) ")
    abstract void insertWitId(@BindBean User user, @Bind("cityId") int cityId);

    @SqlQuery("SELECT users.*, cities.* FROM users LEFT JOIN cities ON users.city_id = cities.id ORDER BY full_name, email LIMIT :it")
    @Mapper(UserMapper.class)
    public abstract List<User> getWithLimit(@Bind int limit);

    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE users")
    @Override
    public abstract void clean();

    //    https://habrahabr.ru/post/264281/
    @SqlBatch("INSERT INTO users (id, full_name, email, flag, city_id) VALUES (:id, :fullName, :email, CAST(:flag AS USER_FLAG), :cityId)" +
            "ON CONFLICT DO NOTHING")
//            "ON CONFLICT (email) DO UPDATE SET full_name=:fullName, flag=CAST(:flag AS USER_FLAG)")
    public abstract int[] insertBatch(@BindBean List<User> users, @Bind("cityId") List<Integer> cityIds, @BatchChunkSize int chunkSize);


    public List<String> insertAndGetConflictEmails(List<User> users) {
        List<Integer> cityIds = users.stream().map(user -> user.getCity().getId()).collect(Collectors.toList());
        int[] result = insertBatch(users, cityIds, users.size());
        return IntStreamEx.range(0, users.size())
                .filter(i -> result[i] == 0)
                .mapToObj(index -> users.get(index).getEmail())
                .toList();
    }
}
