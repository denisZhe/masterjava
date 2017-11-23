package ru.javaops.masterjava.upload;

import com.google.common.primitives.Ints;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.User;
import ru.javaops.masterjava.persist.model.UserFlag;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class UserProcessor {

    private static UserDao dao = DBIProvider.getDao(UserDao.class);
    private ExecutorService executor = Executors.newFixedThreadPool(10);

    public List<User> process(final InputStream is, int chunkSize) throws Exception {
        final StaxStreamProcessor processor = new StaxStreamProcessor(is);

        List<User> partOfUsers = new ArrayList<>();
        List<User> existedUsers = new ArrayList<>();


        List<Future<List<User>>> futures = new ArrayList<>();

        while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
            final String email = processor.getAttribute("email");
            final UserFlag flag = UserFlag.valueOf(processor.getAttribute("flag"));
            final String fullName = processor.getReader().getElementText();
            final User user = new User(fullName, email, flag);
            partOfUsers.add(user);

            if (partOfUsers.size() == chunkSize) {
                UserSaver userSaver = new UserSaver(new ArrayList<>(partOfUsers));
                futures.add(executor.submit(userSaver));
                partOfUsers.clear();
            }
        }
        processor.close();

        if (!partOfUsers.isEmpty()) {
            UserSaver userSaver = new UserSaver(partOfUsers);
            futures.add(executor.submit(userSaver));
        }

        for (Future<List<User>> future : futures) {
            existedUsers.addAll(future.get());
        }

        return existedUsers;
    }

    private class UserSaver implements Callable<List<User>> {
        private final List<User> partOfUsers;

        private UserSaver(List<User> partOfUsers) {
            this.partOfUsers = partOfUsers;
        }

        @Override
        public List<User> call() throws Exception {
            try {
                List<User> existedUsersFromPart = new ArrayList<>();
                List<Integer> userIdList = Ints.asList(dao.insertBatch(partOfUsers));
                if (userIdList != null && userIdList.size() != partOfUsers.size()) {
                    List<User> uploadedUsers = dao.getListByEmail(partOfUsers.stream().map(User::getEmail).collect(Collectors.toList()));
                    existedUsersFromPart.addAll(uploadedUsers.stream().filter(u -> !userIdList.contains(u.getId())).collect(Collectors.toList()));
                }
                return existedUsersFromPart;
            } catch (Exception e) {
                String message = String.format("[%s - %s] Cause: %s: %s",
                        partOfUsers.get(0).getEmail(),
                        partOfUsers.get(partOfUsers.size() - 1).getEmail(),
                        e.getClass().getSimpleName(),
                        e.getMessage());
                throw new Exception(message);
            }
        }
    }
}
