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
        userService.createUser(1234217L, "Owner", LogAccessType.OWNER);


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


    //    delete log tests
    @Test
    @DisplayName("it should delete log by the user with appropriate access level")
    void deleteWhenUserValid() {
        //        given
        int expectedLogsSize = objectUnderTest.getActiveLogs().stream()
                .filter(log -> log.getId() != 31313L)
                .toList()
                .size();
//        when
        objectUnderTest.deleteLog(31113L, userService.getLogCreators().get(1));
//        then
        Assertions.assertEquals(expectedLogsSize, objectUnderTest.getActiveLogs().size());

    }


    @Test
    @DisplayName("it should remove any given log when user accessTyope = owner")
    void deleteLogByOwner() {
//        given
        LogCreator owner = userService.getLogCreators().get(2);
//        when
        int expectedLogsSize = objectUnderTest.getActiveLogs().stream()
                .filter(log -> log.getId() != 31313L)
                .toList()
                .size();

        int expectedDeletedLogsSize = objectUnderTest.getDeletedLogs().size() + 1;

        objectUnderTest.deleteLog(31313L, owner);
        Assertions.assertEquals(expectedLogsSize, objectUnderTest.getActiveLogs().size());
        Assertions.assertEquals(expectedDeletedLogsSize, objectUnderTest.getDeletedLogs().size());
    }

    @Test
    @DisplayName("it should remove any basic log given when user accessType = admin")
    void deleteLogByAdmin() {
//        given
        LogCreator admin = userService.getLogCreators().get(1);
//        when
        int startingLogsSize = objectUnderTest.getActiveLogs().stream()
                .filter(log -> log.getId() != 31313L)
                .toList()
                .size();

        int expectedDeletedLogsSize = objectUnderTest.getDeletedLogs().size() + 1;

        objectUnderTest.deleteLog(31313L, admin);
        Assertions.assertEquals(startingLogsSize, objectUnderTest.getActiveLogs().size());
        Assertions.assertEquals(expectedDeletedLogsSize, objectUnderTest.getDeletedLogs().size());

    }


    @Test
    @DisplayName("it should remove basic log given when user accessType = basic")
    void deleteLogByUser() {
//        given
        LogCreator user = userService.getLogCreators().get(0);
//        when
        int startingLogsSize = objectUnderTest.getActiveLogs().stream()
                .filter(log -> log.getId() != 31313L)
                .toList()
                .size();

        int expectedDeletedLogsSize = objectUnderTest.getDeletedLogs().size() + 1;

        objectUnderTest.deleteLog(31313L, user);
        Assertions.assertEquals(startingLogsSize, objectUnderTest.getActiveLogs().size());
        Assertions.assertEquals(expectedDeletedLogsSize, objectUnderTest.getDeletedLogs().size());
    }

    @Test
    @DisplayName("it should when user with accessType = basic tries to delete log with owner level")
    void throwWhenDeleteByUserNotValid() {
//      given
        LogCreator user = userService.getLogCreators().get(0);
//      when, then
        Assertions.assertThrows(RuntimeException.class, () -> objectUnderTest.deleteLog(42352L, user));
    }

    @Test
    @DisplayName("it should when user with accessType = admin tries to delete log with owner level")
    void throwWhenDeleteByAdminNotValid() {
//      given
        LogCreator admin = userService.getLogCreators().get(1);
//      when, then
        Assertions.assertThrows(RuntimeException.class, () -> objectUnderTest.deleteLog(42352L, admin));
    }

    @Test
    @DisplayName("it shoudl throw when user is empty")
    void throwWhenUserIsEmpty() {
        //      when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> objectUnderTest.deleteLog(42352L, null));
    }


    //    view logs tests
    @Test
    @DisplayName("It should return valid list of logs for access type= owner")
    void getActiveLogsForOwner() {
//        given
        LogCreator owner = userService.getLogCreators().get(2);
        Log additaionalTestLog = new Log(1111L, LocalDateTime.now(), owner, "second owner log");
        objectUnderTest.createLog(additaionalTestLog);
        int expectedLogListSize = objectUnderTest.getActiveLogs().size();
//        when
        List<Log> result = objectUnderTest.getLogsForUser(owner.getUserId());
//        then
        Assertions.assertEquals(expectedLogListSize, result.size());
    }

    @Test
    @DisplayName("It should return valid list of logs for access type= admin")
    void getActiveLogsForAdmin() {
//        given
        LogCreator admin = userService.getLogCreators().get(1);

        Log additaionalTestLog = new Log(1111L, LocalDateTime.now(), admin, "second admin log");
        objectUnderTest.createLog(additaionalTestLog);
        int expectedLogListSize = objectUnderTest.getActiveLogs().stream()
                .filter(log -> log.getLogAccessType() == LogAccessType.ADMIN || log.getLogAccessType() == LogAccessType.BASIC)
                .toList()
                .size();
//        when
        List<Log> result = objectUnderTest.getLogsForUser(admin.getUserId());
//        then
        Assertions.assertEquals(expectedLogListSize, result.size());
    }

    @Test
    @DisplayName("It should return valid list of logs for access type= basic")
    void getActiveLogsForUser() {
//        given
        LogCreator user = userService.getLogCreators().get(0);
        int expectedLogListSize = objectUnderTest.getActiveLogs().stream()
                .filter(log -> log.getCreator().equals(user) && log.getLogAccessType() == LogAccessType.BASIC)
                .toList()
                .size();
//        when
        List<Log> result = objectUnderTest.getLogsForUser(user.getUserId());
//        then
        Assertions.assertEquals(expectedLogListSize, result.size());
    }

    @Test
    @DisplayName("It should throw when userId is empty")
    void throwWhenUserIdIsEmpty() {
//        when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> objectUnderTest.getLogsForUser(null));
    }

    @Test
    @DisplayName("It should throw when user with given id is not found")
    void throwWhenUserIdIsNotFound() {
        //        when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> objectUnderTest.getLogsForUser(1111l));
    }


}