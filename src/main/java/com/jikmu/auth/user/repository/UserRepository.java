package com.jikmu.auth.user.repository;

import com.jikmu.auth.user.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class UserRepository {

    private final Map<UUID, User> userDb = new HashMap<>();


    public User save(User user) {
        userDb.put(user.getId(), user);
        return user;
    }

    public boolean findByNickname(String nickname) {
        for (User user : userDb.values()) {
            if (user.getNickname().equals(nickname)) {
                return true;
            }
        }
        return false;
    }
}
