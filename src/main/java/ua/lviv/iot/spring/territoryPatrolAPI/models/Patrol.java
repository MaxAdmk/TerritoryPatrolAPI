package ua.lviv.iot.spring.robotpatrol.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Patrol {

    private int id;
    private String route;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public String getStartTimeFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return startTime.format(formatter);
    }

    public String getEndTimeFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return endTime.format(formatter);
    }

}
