package org.localhost.LoggerSystemModule;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.localhost.userModule.User;
import org.localhost.userModule.UserService;

import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private final UserService objectUnderTest = new UserService();

    @Test
    @DisplayName("it should init user list")
    void createUserList() {
//        given
        User testUser = new User(666l, "Testowy", LogAccessType.ADMIN);
        User secondTestUser = new User(667l, "TestowyKolejny", LogAccessType.BASIC);
//        when
        objectUnderTest.createUser(666l, "Testowy", LogAccessType.ADMIN);
        objectUnderTest.createUser(667l, "TestowyKolejny", LogAccessType.BASIC);

        LinkedList<User> expectedUsers = new LinkedList<>();
        expectedUsers.add(testUser);
        expectedUsers.add(secondTestUser);

        Assertions.assertEquals(
                expectedUsers.get(0).getUserId(),
                objectUnderTest.getAllUsers().get(0).getUserId()
        );
        Assertions.assertEquals(
                expectedUsers.get(1).getUserId(),
                objectUnderTest.getAllUsers().get(1).getUserId()
        );
    }

    private static Stream<Arguments> generateInvalidData() {
        return Stream.of(
                Arguments.of(null, "Testowy", LogAccessType.ADMIN),
                Arguments.of(666L, null, LogAccessType.ADMIN),
                Arguments.of(666L, "Testowy", null)
        );
    }

    @ParameterizedTest
    @MethodSource("generateInvalidData")
    @DisplayName("It should throw when any argument of createUser is null")
    void throwWhenNull(long id, String name, LogAccessType logAccessType) {
        // when, then
        assertThrows(IllegalArgumentException.class, () ->
                objectUnderTest.createUser(id, name, logAccessType)
        );
    }


    @Test
    @DisplayName("It should return true when a user exist")
    void userExistsReturns() {
//        given
        objectUnderTest.createUser(666l, "Testowy", LogAccessType.ADMIN);
//        when
        boolean expectedResult = objectUnderTest.userExists(666l);
//        then
        Assertions.assertEquals(expectedResult, true);
    }

    @Test
    @DisplayName("It should return false when a user does not exist")
    void userExistsThrows() {
//        give, when
        boolean expectedResult = objectUnderTest.userExists(666l);
//        then
        Assertions.assertEquals(expectedResult, false);
    }


    @Test
    @DisplayName("getUserData should return data when user was created")
    void getUserDataReturns() {
//        given
        objectUnderTest.createUser(666l, "Testowy", LogAccessType.ADMIN);
        Optional<User> resultUser = objectUnderTest.getUserData(666l);
//        when
        User expectedUser = new User(666l, "Testowy", LogAccessType.ADMIN);
//        then
        Assertions.assertEquals(expectedUser.getUserName(), resultUser.get().getUserName());
        Assertions.assertEquals(expectedUser.getUserId(), resultUser.get().getUserId());
        Assertions.assertEquals(expectedUser.getLogAccessType(), resultUser.get().getLogAccessType());
    }
    @Test
    @DisplayName("getUserData should throw when user doesn't exist")
    void getUserDataThrows() {
//        when, then
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> objectUnderTest.getUserData(666l));
    }

}