package org.localhost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class LoggerSystemApplication {


    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(LoggerSystemApplication.class, args);
//        LoggerSystem loggerSystem = context.getBean(LoggerSystem.class);
//
//        LogCreator user = new LogCreator(62617L, "User", LogAccessType.BASIC);
//        LogCreator admin = new LogCreator(6231L, "Admin", LogAccessType.BASIC);
//        LogCreator owner = new LogCreator(1234217L, "Admin", LogAccessType.OWNER);
//
//        Log firstLog = new Log(31313L, LocalDateTime.now(), user, "user log");
//        Log secondLog = new Log(31113L, LocalDateTime.now(), admin, "admin log");
//        Log thirdLog = new Log(42352L, LocalDateTime.now(), owner, "owner log");

//        loggerSystem.createLog(firstLog);
//        loggerSystem.createLog(secondLog);
//        loggerSystem.createLog(thirdLog);
//
//        loggerSystem.getActiveLogs().stream().forEach(log -> System.out.println(log.getLogMessage()));
//        loggerSystem.deleteLog(31113L, user);




//        Log4Test log4Test = new Log4Test();
//        log4Test.performSomeTask();
    }

}
