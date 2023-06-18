package ua.lviv.iot.spring.robotpatrol.writers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.spring.robotpatrol.models.Patrol;
import ua.lviv.iot.spring.robotpatrol.models.Robot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RobotCSVWriterTest {

    private RobotCSVWriter robotCSVWriter;
    private Map<Integer, Robot> robotsMap;
    private String folderPath;

    @BeforeEach
    public void setup() {
        robotCSVWriter = new RobotCSVWriter();
        robotsMap = createTestRobots();
        folderPath = "C:\\Users\\maksi\\Desktop\\Kursach\\robotpatrol\\robotpatrol\\CSVfiles\\TestFiles\\RobotTest";
    }

    @Test
    public void testWriteAllRobotsToCSV() throws IOException {
        robotCSVWriter.writeAllRobotsToCSV(robotsMap, folderPath);

        for (Robot robot : robotsMap.values()) {
            String expectedFilename = robot.getId() + "-" + robot.getModel() + "-" + getCurrentDateFormatted() + ".csv";
            String expectedFilePath = folderPath + "/" + expectedFilename;

            File createdFile = new File(expectedFilePath);
            Assertions.assertTrue(createdFile.exists());

            try (BufferedReader reader = new BufferedReader(new FileReader(createdFile))) {
                String line = reader.readLine();

                Assertions.assertEquals("Robot", line);
                Assertions.assertEquals("id, batteryLevel, model, status", reader.readLine());
                String expectedRobotDataLine = String.format("%d,%d,%s,%s",
                        robot.getId(), robot.getBatteryLevel(), robot.getModel(), robot.getStatus());

                Assertions.assertEquals(expectedRobotDataLine, reader.readLine());

                line = reader.readLine();
                Assertions.assertEquals("patrolOfRobot", line);
                Assertions.assertEquals("id, route, startTime, endTime", reader.readLine());

                Patrol patrol = robot.getPatrolOfRobot();
                String expectedPatrolDataLine = String.format("%d,%s,%s,%s",
                        patrol.getId(), patrol.getRoute(), patrol.getStartTimeFormatted(), patrol.getEndTimeFormatted());

                Assertions.assertEquals(expectedPatrolDataLine, reader.readLine());

                Assertions.assertNull(reader.readLine());
            }

            Assertions.assertTrue(createdFile.delete());
        }
    }

    private Map<Integer, Robot> createTestRobots() {
        Map<Integer, Robot> robots = new HashMap<>();

        LocalDateTime startTime = LocalDateTime.parse("2023-06-18T10:00:00");
        LocalDateTime endTime = LocalDateTime.parse("2023-06-18T12:00:00");
        Patrol patrol = new Patrol(1, "Route1", startTime, endTime);

        Robot robot1 = new Robot(1, patrol, 80, "A1", "Offline");
        robots.put(1, robot1);

        LocalDateTime startTime2 = LocalDateTime.parse("2023-06-18T14:00:00");
        LocalDateTime endTime2 = LocalDateTime.parse("2023-06-18T16:00:00");
        Patrol patrol2 = new Patrol(2, "Route2", startTime2, endTime2);

        Robot robot2 = new Robot(2, patrol2, 100, "B2", "patrolling");
        robots.put(2, robot2);

        return robots;
    }

    private String getCurrentDateFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDateTime.now().format(formatter);
    }
}