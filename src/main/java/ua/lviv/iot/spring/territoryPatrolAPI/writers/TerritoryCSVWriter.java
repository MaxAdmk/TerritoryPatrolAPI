package ua.lviv.iot.spring.robotpatrol.writers;

import ua.lviv.iot.spring.robotpatrol.models.Robot;
import ua.lviv.iot.spring.robotpatrol.models.Territory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class TerritoryCSVWriter {

    private final String HEADERS_OF_TERRITORY_WITHOUT_ROBOTS = "id,area,name,description";
    private final String HEADERS_OF_ROBOTS_WITHOUT_PATROLS = "id, batteryLevel, model, status";
    private final String HEADERS_OF_PATROLS = "id, route, startTime, endTime";

    public void writeOneObjectToCSV(Territory territory, String filepath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        writer.write(HEADERS_OF_TERRITORY_WITHOUT_ROBOTS);

        int id = territory.getId();
        double area = territory.getArea();
        String name = territory.getName();
        String description = territory.getDescription();

        String line = String.format("%d,%.2f,%s,%s", id, area, name, description);
        writer.newLine();
        writer.write(line);
        writer.newLine();
        writer.write("robotsOfTerritory");
        writer.newLine();
        writer.write(HEADERS_OF_ROBOTS_WITHOUT_PATROLS);
        writer.newLine();
        for (Robot robot : territory.getRobotsOfTerritory().values()) {
            writer.write(String.format("%d,%s,%d,%s,%s",
                    robot.getId(), robot.getPatrolOfRobot(), robot.getBatteryLevel(), robot.getModel(),
                    robot.getStatus()));
            writer.newLine();
        }
        writer.write("patrolsOfRobots");
        writer.newLine();
        writer.write(HEADERS_OF_PATROLS);
        writer.newLine();
        for (Robot robot : territory.getRobotsOfTerritory().values()) {
            String startTime = robot.getPatrolOfRobot().getStartTime().format(formatter);
            String endTime = robot.getPatrolOfRobot().getEndTime().format(formatter);
            writer.write(String.format("%d,%s,%s,%s", robot.getPatrolOfRobot().getId(),
                    robot.getPatrolOfRobot().getRoute(), startTime, endTime));
            writer.newLine();
        }
        writer.close();
    }

    public void writeAllTerritoriesToCSV(Map<Integer, Territory> territoriesMap, String folderpath) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatterForFileNaming = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.now();

        for (Territory territory : territoriesMap.values()) {
            String filename = territory.getName() + "-" + currentDate.format(formatterForFileNaming) + ".csv";
            String filepath = folderpath + "/" + filename;
            BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));

            writer.write(HEADERS_OF_TERRITORY_WITHOUT_ROBOTS);

            String line = String.format("%d,%.2f,%s,%s", territory.getId(), territory.getArea(),
                    territory.getName(), territory.getDescription());
            writer.newLine();
            writer.write(line);
            writer.newLine();
            writer.write("robotsOfTerritory");
            writer.newLine();
            writer.write(HEADERS_OF_ROBOTS_WITHOUT_PATROLS);
            writer.newLine();
            for (Robot robot : territory.getRobotsOfTerritory().values()) {
                writer.write(String.format("%d,%d,%s,%s",
                        robot.getId(), robot.getBatteryLevel(), robot.getModel(), robot.getStatus()));
                writer.newLine();
            }
            writer.write("patrolsOfRobots");
            writer.newLine();
            writer.write(HEADERS_OF_PATROLS);
            writer.newLine();
            for (Robot robot : territory.getRobotsOfTerritory().values()) {
                String startTime = robot.getPatrolOfRobot().getStartTime().format(formatter);
                String endTime = robot.getPatrolOfRobot().getEndTime().format(formatter);
                writer.write(String.format("%d,%s,%s,%s", robot.getPatrolOfRobot().getId(),
                        robot.getPatrolOfRobot().getRoute(), startTime, endTime));
                writer.newLine();
            }
            writer.close();
        }
    }
}
