package org.localhost.userModule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.localhost.LoggerSystemModule.LogAccessType;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long userId;
    private String userName;
    private LogAccessType logAccessType;
}
