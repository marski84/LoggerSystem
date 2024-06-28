package org.localhost.loggersystem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import java.util.LinkedList;


@Getter
@Setter
@NoArgsConstructor
@Service
public class UserService {
    private final LinkedList<LogCreator> logCreators = new LinkedList<>();


    public void createUser(Long userId, String userName, LogAccessType logAccessType) {
        if (userId == null || userName == null || logAccessType == null) {
            throw new IllegalArgumentException("Params cannot be null");
        }
        logCreators.add(new LogCreator(userId, userName, logAccessType));
    }

    public boolean userExists(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        return logCreators.stream().anyMatch(user -> user.getUserId().equals(userId));
    }

    LogCreator getUserData(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        if (!userExists(userId)) {
            throw new IllegalArgumentException("User not found");
        }

        return logCreators.stream()
                .filter(user -> user.getUserId().equals(userId))
                .toList().get(0);
    }

}
