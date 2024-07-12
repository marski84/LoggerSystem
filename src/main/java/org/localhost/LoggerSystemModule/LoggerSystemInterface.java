package org.localhost.LoggerSystemModule;

import java.util.List;
import java.util.Set;

public interface LoggerSystemInterface {
    void createLog(Log log);
    void deleteLog(long logId, long userId);
    Set<Log> getLogsForUser(long userId);
    List<Log> getActiveLogs();
    List<Log> getDeletedLogs();
}
