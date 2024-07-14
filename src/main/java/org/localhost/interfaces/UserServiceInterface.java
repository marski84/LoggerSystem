package org.localhost.interfaces;

import org.localhost.LoggerSystemModule.LogAccessType;
import org.localhost.userModule.User;

import java.util.Optional;

public interface UserServiceInterface {
    void createUser(long userId, String userName, LogAccessType logAccessType);
    boolean userExists(long userId);
    public Optional<User> getUserData(long userId);
}
