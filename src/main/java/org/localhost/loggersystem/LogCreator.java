package org.localhost.loggersystem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogCreator {
    private Long userId;
    private String userName;
    private LogAccessType logAccessType;
}
