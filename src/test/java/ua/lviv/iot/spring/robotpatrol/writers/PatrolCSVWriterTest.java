package ua.lviv.iot.spring.robotpatrol.writers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.lviv.iot.spring.robotpatrol.models.Patrol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PatrolCSVWriterTest {

    private PatrolCSVWriter patrolCSVWriter;
    private Map<Integer, Patrol> patrolsMap;
    private String folderPath;

    @BeforeEach
    public void setup() {
        patrolCSVWriter = new PatrolCSVWriter();
        patrolsMap = createTestPatrols();
        folderPath = "C:\\Users\\maksi\\Desktop\\Kursach\\robotpatrol\\robotpatrol\\CSVfiles\\TestFiles\\PatrolTest";
    }

    @Test
    void testWriteAllPatrolsToCSV() throws IOException {
        patrolCSVWriter.writeAllPatrolsToCSV(patrolsMap, folderPath);

        for (Patrol patrol : patrolsMap.values()) {
            String expectedFilename = patrol.getId() + "-Patrol-" + getCurrentDateFormatted() + ".csv";
            String expectedFilePath = folderPath + "/" + expectedFilename;

            File createdFile = new File(expectedFilePath);
            Assertions.assertTrue(createdFile.exists());

            try (BufferedReader reader = new BufferedReader(new FileReader(createdFile))) {
                String line = reader.readLine();

                Assertions.assertEquals("Patrol", line); // Перевірка першого рядка "Patrol"
                Assertions.assertEquals("id, route, startTime, endTime", reader.readLine()); // Перевірка другого рядка заголовків

                String expectedDataLine = String.format("%d,%s,%s,%s",
                        patrol.getId(), patrol.getRoute(), patrol.getStartTimeFormatted(), patrol.getEndTimeFormatted());

                Assertions.assertEquals(expectedDataLine, reader.readLine());

                Assertions.assertNull(reader.readLine());
            }

            Assertions.assertTrue(createdFile.delete());
        }
    }

    private Map<Integer, Patrol> createTestPatrols() {
        Map<Integer, Patrol> patrols = new HashMap<>();

        LocalDateTime startTime1 = LocalDateTime.parse("2023-06-18T10:00:00");
        LocalDateTime endTime1 = LocalDateTime.parse("2023-06-18T12:00:00");
        Patrol patrol1 = new Patrol(1, "Route1", startTime1, endTime1);
        patrols.put(1, patrol1);

        LocalDateTime startTime2 = LocalDateTime.parse("2023-06-18T14:00:00");
        LocalDateTime endTime2 = LocalDateTime.parse("2023-06-18T16:00:00");
        Patrol patrol2 = new Patrol(2, "Point1, point2", startTime2, endTime2);
        patrols.put(2, patrol2);

        return patrols;
    }

    private String getCurrentDateFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDateTime.now().format(formatter);
    }

}