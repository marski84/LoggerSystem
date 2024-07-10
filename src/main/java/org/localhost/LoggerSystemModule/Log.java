package org.localhost.LoggerSystemModule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.localhost.userModule.User;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class Log {
    private long id;
    private LocalDateTime localDateTime;
    private User creator;
    private LogAccessType logAccessType;
    private String logMessage;

    public Log(Long id, LocalDateTime localDateTime, User creator, String logMessage) {
        Objects.requireNonNull(creator);
        this.id = id;
        this.localDateTime = localDateTime;
        this.creator = creator;
        this.logMessage = logMessage;
//        set log access level
        this.logAccessType = creator.getLogAccessType();
    }

    public void showLog() {
        log.info(logMessage);
    }
}
