package org.localhost.interfaces;

import org.localhost.exceptions.AccessDeniedException;
import org.localhost.LoggerSystemModule.Log;
import org.localhost.exceptions.LogNotFoundException;
import org.localhost.exceptions.UserNotFoundException;

import java.util.List;
import java.util.Set;

public interface LoggerSystemInterface {
    void createLog(Log log);
    void deleteLog(long logId, long userId) throws UserNotFoundException, AccessDeniedException, LogNotFoundException;
    Set<Log> getLogsForUser(long userId) throws UserNotFoundException;
    List<Log> getActiveLogs();
    List<Log> getDeletedLogs();
}
