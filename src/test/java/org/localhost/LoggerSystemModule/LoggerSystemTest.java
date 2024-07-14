package org.localhost.LoggerSystemModule;

import org.junit.jupiter.api.*;
import org.localhost.exceptions.AccessDeniedException;
import org.localhost.exceptions.LogNotFoundException;
import org.localhost.exceptions.UserNotFoundException;
import org.localhost.userModule.User;
import org.localhost.userModule.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Set;

class LoggerSystemTest {
    private static final Logger log = LoggerFactory.getLogger(LoggerSystemTest.class);
    //    init test data
    private final UserService userService = new UserService();
    private final LoggerSystem objectUnderTest = new LoggerSystem(userService);


    @BeforeEach
    void initTestData() {
        userService.createUser(62617L, "User", LogAccessType.BASIC);
        userService.createUser(989L, "Admin", LogAccessType.ADMIN);
        userService.createUser(1234217L, "Owner", LogAccessType.OWNER);


        Log firstLog = new Log(
                31313L,
                LocalDateTime.now(),
                userService.getAllUsers().stream().filter(user -> user.getLogAccessType() == LogAccessType.BASIC).findFirst().get()
                , "user log"
        );
        Log secondLog = new Log(
                31113L,
                LocalDateTime.now(),
                userService.getAllUsers().stream().filter(user -> user.getLogAccessType() == LogAccessType.ADMIN).findFirst().get(),
                "admin log"
        );
        Log thirdLog = new Log(
                42352L,
                LocalDateTime.now(),
                userService.getAllUsers().stream().filter(user -> user.getLogAccessType() == LogAccessType.OWNER).findFirst().get(),
                "owner log");

        objectUnderTest.createLog(firstLog);
        objectUnderTest.createLog(secondLog);
        objectUnderTest.createLog(thirdLog);
    }

    @Test
    @DisplayName("it should add valid logs to list")
    void createLogWhenValidData() {
//        given, when

//        then
        Assertions.assertEquals(objectUnderTest.getActiveLogs().size(), 3);
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
    void deleteWhenUserValid() throws UserNotFoundException, AccessDeniedException, LogNotFoundException {
        //        given
        int expectedLogsSize = objectUnderTest.getActiveLogs().stream()
                .filter(log -> log.getId() != 31313L)
                .toList()
                .size();
//        when
        objectUnderTest.deleteLog(31113L,
                userService.getAllUsers().stream().filter(user -> user.getLogAccessType() == LogAccessType.OWNER).findFirst().get().getUserId());
//        then
        Assertions.assertEquals(expectedLogsSize, objectUnderTest.getActiveLogs().size());

    }


    @Test
    @DisplayName("it should remove any given log when user accessTyope = owner")
    void deleteLogByOwner() {
//        given
        User owner = userService.getAllUsers().get(2);
//        when
        int expectedLogsSize = objectUnderTest.getActiveLogs().stream()
                .filter(log -> log.getId() != 31313L)
                .toList()
                .size();

        int expectedDeletedLogsSize = objectUnderTest.getDeletedLogs().size() + 1;

        try {
            objectUnderTest.deleteLog(31313L, owner.getUserId());
        } catch (Exception e ){

        }
        Assertions.assertEquals(expectedLogsSize, objectUnderTest.getActiveLogs().size());
        Assertions.assertEquals(expectedDeletedLogsSize, objectUnderTest.getDeletedLogs().size());
    }

    @Test
    @DisplayName("it should remove any basic log given when user accessType = admin")
    void deleteLogByAdmin() throws UserNotFoundException, AccessDeniedException, LogNotFoundException {
//        given
        User admin = userService.getAllUsers().get(1);
//        when
        int startingLogsSize = objectUnderTest.getActiveLogs().stream()
                .filter(log -> log.getId() != 31313L)
                .toList()
                .size();

        int expectedDeletedLogsSize = objectUnderTest.getDeletedLogs().size() + 1;

        objectUnderTest.deleteLog(31313L, admin.getUserId());
        Assertions.assertEquals(startingLogsSize, objectUnderTest.getActiveLogs().size());
        Assertions.assertEquals(expectedDeletedLogsSize, objectUnderTest.getDeletedLogs().size());

    }


    @Test
    @DisplayName("it should remove basic log given when user accessType = basic")
    void deleteLogByUser() throws UserNotFoundException, AccessDeniedException, LogNotFoundException {
//        given
        User user = userService.getAllUsers().get(0);
//        when
        int startingLogsSize = objectUnderTest.getActiveLogs().stream()
                .filter(log -> log.getId() != 31313L)
                .toList()
                .size();

        int expectedDeletedLogsSize = objectUnderTest.getDeletedLogs().size() + 1;

        objectUnderTest.deleteLog(31313L, user.getUserId());
        Assertions.assertEquals(startingLogsSize, objectUnderTest.getActiveLogs().size());
        Assertions.assertEquals(expectedDeletedLogsSize, objectUnderTest.getDeletedLogs().size());
    }

    @Test
    @DisplayName("it should when user with accessType = basic tries to delete log with owner level")
    void throwWhenDeleteByUserNotValid() {
//      given
        User user = userService.getAllUsers().stream().filter(result -> result.getLogAccessType() == LogAccessType.BASIC).findFirst().get();
//      when, then
        Assertions.assertThrows(AccessDeniedException.class, () -> objectUnderTest.deleteLog(42352L, user.getUserId()));
    }

    @Test
    @DisplayName("it should when user with accessType = admin tries to delete log with owner level")
    void throwWhenDeleteByAdminNotValid() {
//      given
        User admin = userService.getAllUsers().stream().filter(result -> result.getLogAccessType() == LogAccessType.ADMIN).findFirst().get();
//      when, then
        Assertions.assertThrowsExactly(AccessDeniedException.class, () -> objectUnderTest.deleteLog(42352L, admin.getUserId()));

//        Assertions.assertThrows(AccessDeniedException.class, () -> objectUnderTest.deleteLog(42352L, admin.getUserId()));
    }


    //    view logs tests
    @Test
    @DisplayName("It should return valid list of logs for access type= owner")
    void getActiveLogsForOwner() throws UserNotFoundException {
//        given
        User owner = userService.getAllUsers().stream().filter(result -> result.getLogAccessType() == LogAccessType.OWNER).findFirst().get();
        Log additaionalTestLog = new Log(1111L, LocalDateTime.now(), owner, "second owner log");
        objectUnderTest.createLog(additaionalTestLog);
        int expectedLogListSize = objectUnderTest.getActiveLogs().size();
//        when
        Set<Log> result = objectUnderTest.getLogsForUser(owner.getUserId());
//        then
        Assertions.assertEquals(expectedLogListSize, result.size());
    }

    @Test
    @DisplayName("It should return valid list of logs for access type= admin")
    void getActiveLogsForAdmin() throws UserNotFoundException {
//        given
        User admin = userService.getAllUsers().stream().filter(result -> result.getLogAccessType() == LogAccessType.ADMIN).findFirst().get();

        Log additaionalTestLog = new Log(1111L, LocalDateTime.now(), admin, "second admin log");
        objectUnderTest.createLog(additaionalTestLog);
        int expectedLogListSize = objectUnderTest.getActiveLogs().stream()
                .filter(log -> log.getLogAccessType() == LogAccessType.ADMIN || log.getLogAccessType() == LogAccessType.BASIC)
                .toList()
                .size();
//        when
        Set<Log> result = objectUnderTest.getLogsForUser(admin.getUserId());
//        then
        Assertions.assertEquals(expectedLogListSize, result.size());
    }

    @Test
    @DisplayName("It should return valid list of logs for access type= basic")
    void getActiveLogsForUser() throws UserNotFoundException {
//        given
        User user = userService.getAllUsers().stream().filter(resultUser -> resultUser.getLogAccessType() == LogAccessType.BASIC).findFirst().get();

        int expectedLogListSize = objectUnderTest.getActiveLogs().stream()
                .filter(log -> log.getCreator().equals(user) && log.getLogAccessType() == LogAccessType.BASIC)
                .toList()
                .size();
//        when
        Set<Log> result = objectUnderTest.getLogsForUser(user.getUserId());
//        then
        Assertions.assertEquals(expectedLogListSize, result.size());
    }


    @Test
    @DisplayName("It should throw when user with given id is not found")
    void throwWhenUserIdIsNotFound() {
        //        when, then
        Assertions.assertThrows(UserNotFoundException.class, () -> objectUnderTest.getLogsForUser(1111l));
    }


}