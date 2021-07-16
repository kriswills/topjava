package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    private final Map<Integer, User> userMap = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
       return userMap.remove(id) != null;
 //      return userMap.entrySet().removeIf(entry -> entry.getValue().equals(id));

    }

    @Override
    public User save(User user) {
        log.info("save {}", user);

        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            userMap.put(user.getId(), user);
            return user;
        }
        return userMap.computeIfPresent(user.getId(), (id, oldMeal) -> user);
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return userMap.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");

        return userMap.values()
                .stream()
                .sorted(Comparator.comparing((User user) -> user.getName()).thenComparing(user1 -> user1.getEmail()))
                //      .sorted(Map.Entry.comparingByValue())
                //          .sorted(comparingByValue())
                //         .sorted(Map.Entry.comparingByValue(Comparator.comparing()))
                //         .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return userMap.values()
                .stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst()
                .orElse(null);
    }
}
