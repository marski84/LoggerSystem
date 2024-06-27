package org.localhost.loggersystem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Log {
    private Long id;
    private LocalDateTime localDateTime;
    private LogCreator creator;
    private LogAccessType logAccessType;
    private String logMessage;

    public Log(Long id, LocalDateTime localDateTime, LogCreator creator, String logMessage) {
        this.id = id;
        this.localDateTime = localDateTime;
        this.creator = creator;
        this.logMessage = logMessage;
//        set log access level
        this.logAccessType = creator.getLogAccessType();
    }
}
