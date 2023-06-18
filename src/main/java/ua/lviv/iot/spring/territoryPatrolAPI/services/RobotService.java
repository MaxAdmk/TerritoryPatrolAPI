package ua.lviv.iot.spring.robotpatrol.services;

import lombok.Getter;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ua.lviv.iot.spring.robotpatrol.models.Patrol;
import ua.lviv.iot.spring.robotpatrol.models.Robot;
import ua.lviv.iot.spring.robotpatrol.writers.RobotCSVWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Service
public class RobotService {

    private RobotCSVWriter robotCSVWriter = new RobotCSVWriter();

    private final Map<Integer, Robot> robotsMap = new HashMap<>();

    private final AtomicInteger robotsIdCounter = new AtomicInteger();

    public List<Robot> getRobots() {
        return new LinkedList<Robot>(robotsMap.values());
    }

    public Robot getRobot(Integer robotId) {
        return robotsMap.get(robotId);
    }

    public Robot createRobot(Robot robot) {
        robot.setId(robotsIdCounter.incrementAndGet());
        robotsMap.put(robot.getId(), robot);
        return robot;
    }

    public void deleteRobot(@PathVariable("id") Integer robotId) {
        robotsMap.remove(robotId);
    }

    public void updateRobot(Patrol patrolOfRobot, int batteryLevel, String model, String status,
                            Integer idForUpdating) {
        Robot robot = robotsMap.get(idForUpdating);
        if (robot != null) {
            robot.setPatrolOfRobot(patrolOfRobot);
            robot.setBatteryLevel(batteryLevel);
            robot.setModel(model);
            robot.setStatus(status);
            robotsMap.put(idForUpdating, robot);
        } else {
            throw new IllegalArgumentException("No robots found with id: " + idForUpdating);
        }
    }

    public void loadPatrolInformationToRobotById(Integer idOfRobot, Patrol patrolToLoad) {
        Robot robot = robotsMap.get(idOfRobot);

        if (robot != null) {
            robot.setPatrolOfRobot(patrolToLoad);
            robotsMap.put(idOfRobot, robot);
        } else {
            throw new ObjectNotFoundException(idOfRobot, "No objects found with given id");
        }
    }

    public void exportAllRobots(Map<Integer, Robot> robotsMap, String folderpath) {
        try {
            robotCSVWriter.writeAllRobotsToCSV(robotsMap, folderpath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
