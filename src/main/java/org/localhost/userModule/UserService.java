package org.localhost.userModule;

import lombok.Getter;
import lombok.Setter;
import org.localhost.LoggerSystemModule.LogAccessType;
import org.springframework.stereotype.Service;
import java.util.LinkedList;
import java.util.Optional;


@Getter
@Setter
@Service
public class UserService implements UserServiceInterface {
    private final LinkedList<User> users = new LinkedList<>();


    public void createUser(long userId, String userName, LogAccessType logAccessType) {
        if (userName == null || logAccessType == null) {
            throw new IllegalArgumentException("Params cannot be null");
        }
        users.add(new User(userId, userName, logAccessType));
    }

    public boolean userExists(long userId) {
        return users.stream().anyMatch(user -> user.getUserId() == userId);
    }

    public Optional<User> getUserData(long userId) {
        if (!userExists(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        return users.stream()
                .filter(user -> user.getUserId() == userId)
                .findFirst();
    }

}
