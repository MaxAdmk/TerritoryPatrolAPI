package ua.lviv.iot.spring.robotpatrol.services;

import lombok.Getter;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ua.lviv.iot.spring.robotpatrol.models.Robot;
import ua.lviv.iot.spring.robotpatrol.models.Territory;
import ua.lviv.iot.spring.robotpatrol.writers.TerritoryCSVWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Service
public class TerritoryService {

    private TerritoryCSVWriter territoryCSVWriter = new TerritoryCSVWriter();

    private final Map<Integer, Territory> territoriesMap = new HashMap<>();

    private final AtomicInteger territoriesIdCounter = new AtomicInteger();

    public List<Territory> getTerritories() {
        return new LinkedList<Territory>(territoriesMap.values());
    }

    public Territory getTerritory(@PathVariable("id") Integer territoryId) {
        return territoriesMap.get(territoryId);
    }

    public Territory createTerritory(Territory territory) {
        territory.setId(territoriesIdCounter.incrementAndGet());
        territoriesMap.put(territory.getId(), territory);
        return territory;
    }

    public void deleteTerritory(Integer territoryId) {
        territoriesMap.remove(territoryId);
    }

    public void updateTerritory(HashMap robotsOfTerritory, double area, String name, String description, Integer idForUpdating) {
        Territory territory = territoriesMap.get(idForUpdating);
        if (territory != null) {
            territory.setRobotsOfTerritory(robotsOfTerritory);
            territory.setArea(area);
            territory.setName(name);
            territory.setDescription(description);
            territoriesMap.put(idForUpdating, territory);
        } else {
            throw new IllegalArgumentException("No territories found with id: " + idForUpdating);
        }
    }

    public void loadRobotsInformationToTerritoriesMapById(Integer id, Integer idOfRobotToLoad, Robot robot) {
        Territory territory = territoriesMap.get(id);
        if (territory != null) {
            territory.addRobotToMap(idOfRobotToLoad, robot);
            territoriesMap.put(id, territory);
        } else {
            throw new ObjectNotFoundException(id, "No objects found with given id");
        }
    }

    public void exportTerritoryToCSVById(Territory territory, String filepath) {

        try {
            territoryCSVWriter.writeOneObjectToCSV(territory, filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportAllTerritories(Map<Integer, Territory> territoriesMap, String folderpath) {
        try {
            territoryCSVWriter.writeAllTerritoriesToCSV(territoriesMap, folderpath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
