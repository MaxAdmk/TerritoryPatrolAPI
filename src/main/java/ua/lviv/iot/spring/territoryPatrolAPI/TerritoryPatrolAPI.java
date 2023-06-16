package ua.lviv.iot.spring.robotpatrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ua.lviv.iot.spring.robotpatrol.models.Robot;
import ua.lviv.iot.spring.robotpatrol.readers.RobotReader;

@SpringBootApplication
public class TerritoryPatrolAPI {

    public static void main(String[] args) {
        SpringApplication.run(TerritoryPatrolAPI.class, args);
        RobotReader robotReader = new RobotReader();
        System.out.println(robotReader.readAllRobotFiles());
    }

}
