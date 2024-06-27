package org.localhost.loggersystem;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Service
public class LoggerSystem {
    private List<Log> deletedLogs = new ArrayList<>();
    private List<Log> activeLogs = new ArrayList<>();

    private final UserService userService;

    public LoggerSystem(UserService userService) {
        this.userService = userService;
    }

    public void createLog(Log log) {
        if (log == null) {
            throw new IllegalArgumentException("Log cannot be null");
        }
        activeLogs.add(log);
    }

    public void deleteLog(Long logId, LogCreator user) {
        validateDeleteLogInput(logId);
        validateUserData(user);

        Optional<Log> logToDelete = activeLogs.stream().filter(log -> log.getId().equals(logId)).findFirst();
        logToDelete.ifPresentOrElse(
                log -> {
                    try {
                        validateLogAccess(user, log);
                    } catch (AccessDeniedException e) {
                        throw new RuntimeException(e);
                    }
                    deletedLogs.add(log);
                    activeLogs.remove(log);
                },
                () -> { throw new IllegalArgumentException("Log not found"); }                );
    }

    List<Log> getLogsForUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("LogCreatorId cannot be null");
        }
        if (!userService.userExists(userId)) {
            throw new IllegalArgumentException("User not found");
        }

        LogCreator user = userService.getUserData(userId);
        List<Log> resultList = new ArrayList<>();

        if (user.getLogAccessType() == LogAccessType.OWNER) {
            resultList = activeLogs;
        }

        if (user.getLogAccessType() == LogAccessType.ADMIN) {

            List<Log> basicUsersLogs = activeLogs.stream()
                    .filter(log -> log.getLogAccessType() == LogAccessType.BASIC)
                    .toList();
            List<Log> userLogs = activeLogs.stream()
                    .filter(log -> log.getLogAccessType() != LogAccessType.OWNER)
                    .filter(log -> log.getCreator().equals(user))
                    .toList();

           resultList = Arrays.asList(basicUsersLogs, userLogs).stream()
                    .flatMap(list -> list.stream())
                    .toList();
        }

        if (user.getLogAccessType() == LogAccessType.BASIC) {
            List<Log> userLogs = activeLogs.stream()
                    .filter(log -> log.getLogAccessType() != LogAccessType.BASIC)
                    .filter(log -> log.getCreator().equals(user))
                    .toList();
            return userLogs;
        }
        return resultList;
    }

    private void validateDeleteLogInput(Long logId) {
        if (logId == null) {
            throw new IllegalArgumentException("LogId cannot be null");
        }
        if (activeLogs.isEmpty()) {
            throw new IllegalStateException("There are no registered active logs");
        }
    }

    private void validateLogAccess(LogCreator user, Log logToVerify) throws AccessDeniedException {
        LogAccessType userAccessType = user.getLogAccessType();
        LogAccessType logAccessType = logToVerify.getLogAccessType();
        if (userAccessType == LogAccessType.ADMIN && logAccessType == LogAccessType.OWNER) {
            throw new AccessDeniedException("Access denied!");
        }
        if (userAccessType == LogAccessType.BASIC && logAccessType != LogAccessType.BASIC) {
            throw new AccessDeniedException("Access denied!");

        }
    }

    private void validateUserData(LogCreator user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
    }






}
