package org.localhost.LoggerSystemModule;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.localhost.userModule.User;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class LogTest {

    private Log log;
    private Logger mockLogger;

    @BeforeEach
    void setUp() {
        mockLogger = Mockito.mock(Logger.class);
        log = new Log(123L, LocalDateTime.now(), new User(), "Sample log message");

        ReflectionTestUtils.setField(log, "logger", mockLogger);
    }

    @Test
    void testShowLog() {
        // Wywołanie metody, którą chcemy przetestować
        log.showLog();

        // Weryfikacja czy metoda info została wywołana z odpowiednim komunikatem
        verify(mockLogger).info("Sample log message");
    }
}