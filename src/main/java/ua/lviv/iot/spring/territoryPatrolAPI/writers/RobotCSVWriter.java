package ua.lviv.iot.spring.robotpatrol.writers;

import ua.lviv.iot.spring.robotpatrol.models.Robot;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class RobotCSVWriter {

    private final String HEADERS_OF_ROBOTS_WITHOUT_PATROLS = "id, batteryLevel, model, status";
    private final String HEADERS_OF_PATROLS = "id, route, startTime, endTime";

    public void writeAllRobotsToCSV(Map<Integer, Robot> robotsMap, String folderpath) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatterForFileNaming = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.now();

        for (Robot robot : robotsMap.values()) {
            String filename = robot.getId() + "-" + robot.getModel() + "-" + currentDate.format(formatterForFileNaming) + ".csv";
            String filepath = folderpath + "/" + filename;
            BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));

            writer.write("Robot");
            writer.newLine();
            writer.write(HEADERS_OF_ROBOTS_WITHOUT_PATROLS);
            writer.newLine();
            writer.write(String.format("%d,%d,%s,%s",
                    robot.getId(), robot.getBatteryLevel(), robot.getModel(), robot.getStatus()));
            writer.newLine();
            writer.write("patrolOfRobot");
            writer.newLine();
            writer.write(HEADERS_OF_PATROLS);
            writer.newLine();

            String startTime = robot.getPatrolOfRobot().getStartTime().format(formatter);
            String endTime = robot.getPatrolOfRobot().getEndTime().format(formatter);
            writer.write(String.format("%d,%s,%s,%s", robot.getPatrolOfRobot().getId(),
                    robot.getPatrolOfRobot().getRoute(), startTime, endTime));
            writer.newLine();

            writer.close();
        }
    }
}
