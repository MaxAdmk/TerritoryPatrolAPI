package ua.lviv.iot.spring.robotpatrol.writers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.spring.robotpatrol.models.Patrol;
import ua.lviv.iot.spring.robotpatrol.models.Robot;
import ua.lviv.iot.spring.robotpatrol.models.Territory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TerritoryCSVWriterTest {

    private TerritoryCSVWriter territoryCSVWriter;
    private Map<Integer, Territory> territoriesMap;
    private String folderPath;

    @BeforeEach
    public void setup() {
        territoryCSVWriter = new TerritoryCSVWriter();
        territoriesMap = createTestTerritories();
        folderPath = "C:\\Users\\maksi\\Desktop\\Kursach\\robotpatrol\\robotpatrol\\CSVfiles\\TestFiles\\TerritoryTest";
    }

    @Test
    public void testWriteAllTerritoriesToCSV() throws IOException {
        territoryCSVWriter.writeAllTerritoriesToCSV(territoriesMap, folderPath);

        for (Territory territory : territoriesMap.values()) {
            String filename = territory.getName() + "-" + getCurrentDateFormatted() + ".csv";
            String filepath = folderPath + "/" + filename;

            File createdFile = new File(filepath);
            Assertions.assertTrue(createdFile.exists());

            try (BufferedReader reader = new BufferedReader(new FileReader(createdFile))) {
                String line = reader.readLine();

                Assertions.assertEquals("id,area,name,description", line); // Перевірка першого рядка заголовків

                String expectedTerritoryDataLine = String.format("%d,%.2f,%s,%s",
                        territory.getId(), territory.getArea(), territory.getName(), territory.getDescription());

                Assertions.assertEquals(expectedTerritoryDataLine, reader.readLine()); // Перевірка рядка з даними про територію

                line = reader.readLine();
                Assertions.assertEquals("robotsOfTerritory", line); // Перевірка рядка "robotsOfTerritory"
                Assertions.assertEquals("id, batteryLevel, model, status", reader.readLine()); // Перевірка заголовків роботів

                for (Robot robot : territory.getRobotsOfTerritory().values()) {
                    String expectedRobotDataLine = String.format("%d,%d,%s,%s",
                            robot.getId(), robot.getBatteryLevel(), robot.getModel(), robot.getStatus());

                    Assertions.assertEquals(expectedRobotDataLine, reader.readLine()); // Перевірка рядків з даними про роботів
                }

                line = reader.readLine();
                Assertions.assertEquals("patrolsOfRobots", line); // Перевірка рядка "patrolsOfRobots"
                Assertions.assertEquals("id, route, startTime, endTime", reader.readLine()); // Перевірка заголовків патрульних записів

                for (Robot robot : territory.getRobotsOfTerritory().values()) {
                    Patrol patrol = robot.getPatrolOfRobot();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String startTime = patrol.getStartTime().format(formatter);
                    String endTime = patrol.getEndTime().format(formatter);

                    String expectedPatrolDataLine = String.format("%d,%s,%s,%s",
                            patrol.getId(), patrol.getRoute(), startTime, endTime);

                    Assertions.assertEquals(expectedPatrolDataLine, reader.readLine()); // Перевірка рядків з даними про патрульні записи
                }

                Assertions.assertNull(reader.readLine()); // Перевірка закінчення файлу
            }
        }
    }

    private Map<Integer, Territory> createTestTerritories() {
        Map<Integer, Territory> territories = new HashMap<>();

        Territory territory1 = new Territory();
        territory1.setId(1);
        territory1.setArea(100.5);
        territory1.setName("Territory1");
        territory1.setDescription("Description1");
        Robot robot1 = new Robot(1, createTestPatrol(), 80, "Model1", "Active");
        territory1.addRobotToMap(robot1.getId(), robot1);
        territories.put(1, territory1);

        Territory territory2 = new Territory();
        territory2.setId(2);
        territory2.setArea(200.75);
        territory2.setName("Territory2");
        territory2.setDescription("Description2");
        Robot robot2 = new Robot(2, createTestPatrol(),90, "Model2", "Inactive");
        territory2.addRobotToMap(robot2.getId(), robot2);
        territories.put(2, territory2);

        return territories;
    }

    private Patrol createTestPatrol() {
        LocalDateTime startTime = LocalDateTime.of(2023, 6, 1, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2023, 6, 1, 17, 0);
        return new Patrol(1, "Route1", startTime, endTime);
    }

    private String getCurrentDateFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.now();
        return currentDate.format(formatter);
    }
}