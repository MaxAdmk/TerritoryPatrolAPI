package ua.lviv.iot.spring.robotpatrol.controllers;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lviv.iot.spring.robotpatrol.models.Patrol;
import ua.lviv.iot.spring.robotpatrol.models.Robot;
import ua.lviv.iot.spring.robotpatrol.models.Territory;
import ua.lviv.iot.spring.robotpatrol.services.PatrolService;
import ua.lviv.iot.spring.robotpatrol.services.RobotService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RequestMapping("/robot")
@RestController
public class RobotController {

    @Autowired
    private RobotService robotService;

    @Autowired
    private PatrolService patrolService;

    private Patrol patrolToLoad = null;

    @GetMapping
    public List<Robot> getRobots() {
        return robotService.getRobots();
    }

    @GetMapping(path = "/{id}")
    public Robot getRobot(final @PathVariable("id") Integer robotId) {
        return robotService.getRobot(robotId);
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Robot createRobot(final @RequestBody Robot robot) {
        return robotService.createRobot(robot);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteRobot(@PathVariable("id") Integer robotId) {
        robotService.deleteRobot(robotId);
    }

    @Getter
    @Setter
    private static class UpdateRequest {
        private Patrol patrolOfRobot;
        private int batteryLevel;

        private String model;
        private String status;
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<String> updateTerritory(@RequestBody RobotController.UpdateRequest updateRequest,
                                                  @PathVariable("id") Integer idForUpdating) {
        robotService.updateRobot(updateRequest.getPatrolOfRobot(), updateRequest.getBatteryLevel(),
                updateRequest.getModel(),
                updateRequest.getStatus(), idForUpdating);
        return ResponseEntity.ok("Robot updated successfully");
    }

    @PostMapping(path = "/load/patrol/information/{idOfRobot}/{idOfPatrolToLoad}")
    public ResponseEntity<String> loadPatrolInformation(@PathVariable("idOfRobot") Integer idOfRobot,
                                                        @PathVariable("idOfPatrolToLoad") Integer idOfPatrolToLoad) {
        Map<Integer, Patrol> patrolsMap = patrolService.getPatrolsMap();
        try {
            patrolToLoad = patrolsMap.get(idOfPatrolToLoad);
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
            ResponseEntity.badRequest();
        }

        robotService.loadPatrolInformationToRobotById(idOfRobot, patrolToLoad);
        return ResponseEntity.ok("Patrol loaded successfully");
    }

    @PostMapping(path = "/export")
    public ResponseEntity<String> exportAllRobots(@RequestParam("folderpath") String folderpath) {
        Map<Integer, Robot> robotsMap = robotService.getRobotsMap();
        try {
            robotService.exportAllRobots(robotsMap, folderpath);
            return ResponseEntity.ok("All robots files were created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error encoding filepath");
        }
    }

}
