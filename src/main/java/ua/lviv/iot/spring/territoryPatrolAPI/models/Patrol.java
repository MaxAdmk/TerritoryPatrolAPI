package ua.lviv.iot.spring.robotpatrol.models;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Patrol {

    private int id;

    private List<String> route;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
