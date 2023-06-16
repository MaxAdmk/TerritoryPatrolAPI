package ua.lviv.iot.spring.robotpatrol.services;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ua.lviv.iot.spring.robotpatrol.models.Patrol;
import ua.lviv.iot.spring.robotpatrol.writers.PatrolCSVWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Service
public class PatrolService {

    private PatrolCSVWriter patrolCSVWriter = new PatrolCSVWriter();

    private final Map<Integer, Patrol> patrolsMap = new HashMap<>();

    private final AtomicInteger patrolsIdCounter = new AtomicInteger();

    public List<Patrol> getPatrols() {
        return new LinkedList<Patrol>(patrolsMap.values());
    }

    public Patrol getPatrol(Integer patrolId) {
        return patrolsMap.get(patrolId);
    }

    public Patrol createPatrol(Patrol patrol) {
        patrol.setId(patrolsIdCounter.incrementAndGet());
        patrolsMap.put(patrol.getId(), patrol);
        return patrol;
    }

    public void deletePatrol(@PathVariable("id") Integer patrolId) {
        patrolsMap.remove(patrolId);
    }

    public void updatePatrol(List<String> route, LocalDateTime startTime, LocalDateTime endTime, Integer idForUpdating) {
        Patrol patrol = patrolsMap.get(idForUpdating);
        if (patrol != null) {
            patrol.setRoute(route);
            patrol.setStartTime(startTime);
            patrol.setEndTime(endTime);
            patrolsMap.put(idForUpdating, patrol);
        } else {
            throw new IllegalArgumentException("No patrols found with id: " + idForUpdating);
        }
    }

    public void exportAllPatrols(Map<Integer, Patrol> patrolsMap, String folderpath) {
        try {
            patrolCSVWriter.writeAllPatrolsToCSV(patrolsMap, folderpath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
