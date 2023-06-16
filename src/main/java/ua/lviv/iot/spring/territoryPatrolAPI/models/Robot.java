package ua.lviv.iot.spring.robotpatrol.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Robot {

    private Integer id;
    private Patrol patrolOfRobot;
    private int batteryLevel;

    private String model;
    private String status;

    public void setBatteryLevel(int batteryLevel) {
        if (batteryLevel < 0) {
            this.batteryLevel = 0;
        } else if (batteryLevel > 100) {
            this.batteryLevel = 100;
        } else {
            this.batteryLevel = batteryLevel;
        }
    }
}
