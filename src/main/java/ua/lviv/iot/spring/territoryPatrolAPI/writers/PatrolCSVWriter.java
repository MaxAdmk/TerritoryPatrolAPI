package ua.lviv.iot.spring.robotpatrol.writers;

import ua.lviv.iot.spring.robotpatrol.models.Patrol;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class PatrolCSVWriter {

    private final String HEADERS_OF_PATROL = "id, route, startTime, endTime";

    public void writeAllPatrolsToCSV(Map<Integer, Patrol> patrolsMap, String folderpath) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatterForFileNaming = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.now();

        for (Patrol patrol : patrolsMap.values()) {
            String filename = patrol.getId() + "-" + "Patrol" + "-" + currentDate.format(formatterForFileNaming)
                    + ".csv";
            String filepath = folderpath + "/" + filename;
            BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
            writer.write("Patrol");
            writer.newLine();
            writer.write(HEADERS_OF_PATROL);
            writer.newLine();
            String startTime = patrol.getStartTime().format(formatter);
            String endTime = patrol.getEndTime().format(formatter);
            writer.write(String.format("%d,%s,%s,%s", patrol.getId(),
                    patrol.getRoute(), startTime, endTime));
            writer.newLine();
            writer.close();
        }
    }
}
