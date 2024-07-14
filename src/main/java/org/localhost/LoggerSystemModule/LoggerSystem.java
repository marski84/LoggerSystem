package org.localhost.LoggerSystemModule;

import lombok.RequiredArgsConstructor;
import org.localhost.exceptions.AccessDeniedException;
import org.localhost.exceptions.LogNotFoundException;
import org.localhost.exceptions.UserNotFoundException;
import org.localhost.interfaces.LoggerSystemInterface;
import org.localhost.userModule.User;
import org.localhost.userModule.UserService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class LoggerSystem implements LoggerSystemInterface {
    private final Set<Log> deletedLogs = new HashSet<>();
    private final Set<Log> activeLogs = new HashSet<>();
    private final UserService userService;

    public void createLog(Log log) {
        if (log == null) {
            throw new IllegalArgumentException("Log cannot be null");
        }
        activeLogs.add(log);
    }

    public void deleteLog(long logId, long userId) throws UserNotFoundException, AccessDeniedException, LogNotFoundException {
        User user = userService.getUserData(userId).orElseThrow(UserNotFoundException::new);
        Log logToDelete = findLogById(logId);
        validateLogAccessOrThrow(user, logToDelete);
        validateDeleteLogInput(logId);
        performLogDeletion(logToDelete);
    }

    private void validateLogAccessOrThrow(User user, Log log) throws AccessDeniedException {
        try {
            validateLogAccess(user, log);
        } catch (AccessDeniedException e) {
            throw e;
        }
    }

    private void performLogDeletion(Log log) {
        deletedLogs.add(log);
        activeLogs.remove(log);
    }

    private Log findLogById(long logId) throws LogNotFoundException {
        try {
            return activeLogs.stream()
                    .filter(log -> log.getId() == logId)
                    .findFirst()
                    .orElseThrow(LogNotFoundException::new);
        } catch (LogNotFoundException e) {
            throw e;
        }
    }

    //    TODO switch + dedykowane metody
    public Set<Log> getLogsForUser(long userId) throws UserNotFoundException {
        try {
            if (!userService.userExists(userId)) {
                throw new UserNotFoundException();
            }
        } catch (UserNotFoundException e) {
            throw e;
        }

        try {
            User user = userService.getUserData(userId).orElseThrow(UserNotFoundException::new);
            return getResultSetForUserAccessLevel(user);
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }


        return null;
    }

    private Set<Log> getResultSetForUserAccessLevel(User user) {
        return switch (user.getLogAccessType()) {
            case OWNER -> handleOwnerLogs();
            case ADMIN -> handleAdminLogs(user);
            case BASIC -> handleBasicUserLogs(user);
        };
    }

    private Set<Log> handleBasicUserLogs(User user) {
        return activeLogs.stream()
                .filter(log -> log.getLogAccessType() == LogAccessType.BASIC)
                .filter(log -> log.getCreator().equals(user))
                .collect(Collectors.toSet());
    }

    private Set<Log> handleAdminLogs(User user) {
        List<Log> basicUsersLogs = activeLogs.stream()
                .filter(log -> log.getLogAccessType() == LogAccessType.BASIC)
                .toList();
        List<Log> userLogs = activeLogs.stream()
                .filter(log -> log.getLogAccessType() != LogAccessType.OWNER)
                .filter(log -> log.getCreator().equals(user))
                .toList();

        return Stream.of(basicUsersLogs, userLogs)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    };

    private Set<Log> handleOwnerLogs() {
        return new HashSet<>(activeLogs);
    }

    private void validateDeleteLogInput(long logId) throws LogNotFoundException {
        if (activeLogs.isEmpty()) {
            throw new LogNotFoundException();
        }
    }

    private void validateLogAccess(User user, Log logToVerify) throws AccessDeniedException {
        LogAccessType userAccessType = user.getLogAccessType();
        LogAccessType logAccessType = logToVerify.getLogAccessType();
        if (userAccessType == LogAccessType.ADMIN && logAccessType == LogAccessType.OWNER) {
            throw new AccessDeniedException();
        }
        if (userAccessType == LogAccessType.BASIC && logAccessType != LogAccessType.BASIC) {
            throw new AccessDeniedException();

        }
    }

    public List<Log> getActiveLogs() {
        return new ArrayList<>(activeLogs);
    }

    public List<Log> getDeletedLogs() {
        return new ArrayList<>(deletedLogs);
    }
}
