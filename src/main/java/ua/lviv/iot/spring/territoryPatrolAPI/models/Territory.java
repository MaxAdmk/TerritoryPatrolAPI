package ua.lviv.iot.spring.robotpatrol.models;

import lombok.*;

import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Territory {

    private int id;
    private HashMap<Integer, Robot> robotsOfTerritory = new HashMap<>();
    private double area;

    private String name;
    private String description;

    public void addRobotToMap(Integer idOfRobotToAdd, Robot robot) {
        robotsOfTerritory.put(idOfRobotToAdd, robot);
    }

}
