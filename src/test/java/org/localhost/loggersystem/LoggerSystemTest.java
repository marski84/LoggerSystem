package org.localhost.loggersystem;

import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class LoggerSystemTest {
    //    init test data
    private final UserService userService = new UserService();
    private final LoggerSystem objectUnderTest = new LoggerSystem(userService);


    @BeforeEach
    void initTestData() {
        userService.createUser(62617L, "User", LogAccessType.BASIC);
        userService.createUser(989L, "Admin", LogAccessType.ADMIN);
        userService.createUser(1234217L, "Admin", LogAccessType.OWNER);

        Log firstLog = new Log(31313L, LocalDateTime.now(), userService.getLogCreators().get(0), "user log");
        Log secondLog = new Log(31113L, LocalDateTime.now(), userService.getLogCreators().get(1), "admin log");
        Log thirdLog = new Log(42352L, LocalDateTime.now(), userService.getLogCreators().get(2), "owner log");

        objectUnderTest.createLog(firstLog);
        objectUnderTest.createLog(secondLog);
        objectUnderTest.createLog(thirdLog);
    }

    @AfterEach
    void resetTestData() {
        objectUnderTest.setActiveLogs(new ArrayList<>());
        objectUnderTest.setDeletedLogs(new ArrayList<>());
    }

    @Test
    @DisplayName("it should add valid logs to list")
    void createLogWhenValidData() {
//        given, when

//        then
        Assertions.assertEquals(objectUnderTest.getActiveLogs().size(), 3);
        Assertions.assertEquals(
                objectUnderTest.getActiveLogs().get(2).getCreator(),
                userService.getLogCreators().get(2)
        );
    }

    @Test
    @DisplayName("it should throw when null is given as parameter")
    void throwWhenInvalidData() {
//    given, when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> objectUnderTest.createLog(null));
    }

    @Test
    @DisplayName("it should delete log by the user with appropriate access level")
    void deleteWhenUserValid() {
        //        given, when

        objectUnderTest.deleteLog(31113L, userService.getLogCreators().get(1));

        Assertions.assertEquals(objectUnderTest.getActiveLogs().size(), 2);

    }


    @Test
    void deleteLog() {
    }

    @Test
    @DisplayName("It should return valid List")
    void getActiveLogsForUser() {
//        given
        Log additaionalTestLog = new Log(1111L, LocalDateTime.now(), userService.getLogCreators().get(2), "second owner log");
        objectUnderTest.createLog(additaionalTestLog);
        int logSize = objectUnderTest.getActiveLogs().size();
//        when
        List<Log> result = objectUnderTest.getLogsForUser(1234217L);
//        then
        Assertions.assertEquals(logSize, result.size());

    }
}