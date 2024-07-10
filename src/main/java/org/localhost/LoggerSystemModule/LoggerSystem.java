package org.localhost.LoggerSystemModule;

import lombok.RequiredArgsConstructor;
import org.localhost.userModule.User;
import org.localhost.userModule.UserService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class LoggerSystem {
    private final Set<Log> deletedLogs = new HashSet<>();
    private final Set<Log> activeLogs = new HashSet<>();
    private final UserService userService;

    public void createLog(Log log) {
        if (log == null) {
            throw new IllegalArgumentException("Log cannot be null");
        }
        activeLogs.add(log);
    }

    public void deleteLog(long logId, long userId) {
        validateDeleteLogInput(logId);
        User user = userService.getUserData(userId).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        Log logToDelete = findLogById(logId);
        validateLogAccessOrThrow(user, logToDelete);
        performLogDeletion(logToDelete);
    }

    private void validateLogAccessOrThrow(User user, Log log) {
        try {
            validateLogAccess(user, log);
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
    }

    private void performLogDeletion(Log log) {
        deletedLogs.add(log);
        activeLogs.remove(log);
    }

    private Log findLogById(long logId) {
        return activeLogs.stream()
                .filter(log -> log.getId() == logId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Log not found"));
    }

    //    TODO switch + dedykowane metody
    Set<Log> getLogsForUser(long userId) {
        if (!userService.userExists(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        User user = userService.getUserData(userId).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return getResultSetForUserAccessLevel(user);

    }

    private Set<Log> getResultSetForUserAccessLevel(User user) {
        Set<Log> resultSet = new HashSet<>();
        switch (user.getLogAccessType()) {
            case OWNER -> resultSet = handleOwnerLogs();
            case ADMIN -> resultSet = handleAdminLogs(user);
            case BASIC -> resultSet = handleBasicUserLogs(user);
        }
        return resultSet;
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
    }

    private Set<Log> handleOwnerLogs() {
        return new HashSet<>(activeLogs);
    }

    private void validateDeleteLogInput(Long logId) {
        if (logId == null) {
            throw new IllegalArgumentException("LogId cannot be null");
        }
        if (activeLogs.isEmpty()) {
            throw new IllegalStateException("There are no registered active logs");
        }
    }

    private void validateLogAccess(User user, Log logToVerify) throws AccessDeniedException {
        LogAccessType userAccessType = user.getLogAccessType();
        LogAccessType logAccessType = logToVerify.getLogAccessType();
        if (userAccessType == LogAccessType.ADMIN && logAccessType == LogAccessType.OWNER) {
            throw new AccessDeniedException("Access denied!");
        }
        if (userAccessType == LogAccessType.BASIC && logAccessType != LogAccessType.BASIC) {
            throw new AccessDeniedException("Access denied!");

        }
    }

    public List<Log> getActiveLogs() {
        return new ArrayList<>(activeLogs);
    }

    public Collection<Object> getDeletedLogs() {
        return  new ArrayList<>(deletedLogs);
    }
}
