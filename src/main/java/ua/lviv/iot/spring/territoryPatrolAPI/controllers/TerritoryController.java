package ua.lviv.iot.spring.robotpatrol.controllers;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lviv.iot.spring.robotpatrol.models.Robot;
import ua.lviv.iot.spring.robotpatrol.models.Territory;
import ua.lviv.iot.spring.robotpatrol.services.RobotService;
import ua.lviv.iot.spring.robotpatrol.services.TerritoryService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/territory")
@RestController
public class TerritoryController {

    @Autowired
    private TerritoryService territoryService;
    @Autowired
    private RobotService robotService;

    private Robot robotToLoad = null;

    @GetMapping
    public List<Territory> getTerritories() {
        return territoryService.getTerritories();
    }

    @GetMapping(path = "/{id}")
    public Territory getTerritory(@PathVariable("id") Integer territoryId) {
        return territoryService.getTerritory(territoryId);
    }

    @PostMapping
    public Territory createTerritory(final @RequestBody Territory territory) {
        return territoryService.createTerritory(territory);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteTerritory(@PathVariable("id") Integer territoryId) {
        territoryService.deleteTerritory(territoryId);
    }

    @Getter
    @Setter
    private static class UpdateRequest {
        private HashMap<Integer, Robot> robotsOfTerritory;
        private double area;

        private String name;
        private String description;
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<String> updateTerritory(@RequestBody TerritoryController.UpdateRequest updateRequest,
                                                  @PathVariable("id") Integer idForUpdating) {
        territoryService.updateTerritory(updateRequest.getRobotsOfTerritory(), updateRequest.getArea(), updateRequest.getName(),
                updateRequest.getDescription(), idForUpdating);
        return ResponseEntity.ok("Territory updated successfully");
    }

    @PostMapping(path = "/load/robots/information/{id}/{idOfRobotToLoad}")
    public ResponseEntity<String> loadRobotsInformation(@PathVariable("id") Integer id,
                                                        @PathVariable("idOfRobotToLoad") Integer idOfRobotToLoad) {
        Map<Integer, Robot> robotsMap = robotService.getRobotsMap();

        try {
            robotToLoad = robotsMap.get(idOfRobotToLoad);
        } catch (ObjectNotFoundException e) {
            ResponseEntity.badRequest();
        }

        territoryService.loadRobotsInformationToTerritoriesMapById(id, idOfRobotToLoad, robotToLoad);
        return ResponseEntity.ok("Robots information loaded successfully");
    }


    @PostMapping(path = "/{id}/export")
    public ResponseEntity<String> exportTerritoryToCSV(@PathVariable("id") Integer territoryId,
                                                       @RequestParam("filename") String filename,
                                                       @RequestParam("folderpath") String folderpath) {
        Map<Integer, Territory> territoriesMap = territoryService.getTerritoriesMap();
        Territory territory = territoriesMap.get(territoryId);

        try {
            String filepath = folderpath + "/" + filename;
            territoryService.exportTerritoryToCSVById(territory, filepath);
            return ResponseEntity.ok("File was created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error encoding filepath");
        }
    }

    @PostMapping(path = "/export")
    public ResponseEntity<String> exportAllTerritories(@RequestParam("folderpath") String folderpath) {
        Map<Integer, Territory> territoriesMap = territoryService.getTerritoriesMap();
        try {
            territoryService.exportAllTerritories(territoriesMap, folderpath);
            return ResponseEntity.ok("All territorries files were created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error encoding filepath");
        }
    }
}
