package org.localhost.loggersystem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private final UserService objectUnderTest = new UserService();

    @Test
    @DisplayName("it should init user list")
    void createUserList() {
//        given
        LogCreator testUser = new LogCreator(666l, "Testowy", LogAccessType.ADMIN);
        LogCreator secondTestUser = new LogCreator(667l, "TestowyKolejny", LogAccessType.BASIC);
//        when
        objectUnderTest.createUser(666l, "Testowy", LogAccessType.ADMIN);
        objectUnderTest.createUser(667l, "TestowyKolejny", LogAccessType.BASIC);

        LinkedList<LogCreator> expectedLogCreators = new LinkedList<>();
        expectedLogCreators.add(testUser);
        expectedLogCreators.add(secondTestUser);

        Assertions.assertEquals(
                expectedLogCreators.get(0).getUserId(),
                objectUnderTest.getLogCreators().get(0).getUserId()
        );
        Assertions.assertEquals(
                expectedLogCreators.get(1).getUserId(),
                objectUnderTest.getLogCreators().get(1).getUserId()
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
    void throwWhenNull(Long id, String name, LogAccessType logAccessType) {
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
        LogCreator resultUser = objectUnderTest.getUserData(666l);
//        when
        LogCreator expectedUser = new LogCreator(666l, "Testowy", LogAccessType.ADMIN);
//        then
        Assertions.assertEquals(expectedUser.getUserName(), resultUser.getUserName());
        Assertions.assertEquals(expectedUser.getUserId(), resultUser.getUserId());
        Assertions.assertEquals(expectedUser.getLogAccessType(), resultUser.getLogAccessType());
    }
    @Test
    @DisplayName("getUserData should throw when user doesn't exist")
    void getUserDataThrows() {
//        when, then
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> objectUnderTest.getUserData(666l));
    }

    @Test
    @DisplayName("getUserData should throw when user is given as null")
    void getUserDataThrowsWhenUserNull() {
//        when, then
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> objectUnderTest.getUserData(null));
    }
}