package ua.lviv.iot.spring.robotpatrol.controllers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lviv.iot.spring.robotpatrol.models.Patrol;
import ua.lviv.iot.spring.robotpatrol.models.Robot;
import ua.lviv.iot.spring.robotpatrol.services.PatrolService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RequestMapping("/patrol")
@RestController
public class PatrolController {

    @Autowired
    private PatrolService patrolService;

    @GetMapping
    public List<Patrol> getPatrols() {
        return patrolService.getPatrols();
    }

    @GetMapping(path = "/{id}")
    public Patrol getPatrol(final @PathVariable("id") Integer patrolId) {
        return patrolService.getPatrol(patrolId);
    }

    @PostMapping
    public Patrol createPatrol(final @RequestBody Patrol patrol) {
        return patrolService.createPatrol(patrol);
    }

    @DeleteMapping(path = "/{id}")
    public void deletePatrol(@PathVariable("id") Integer patrolId) {
        patrolService.deletePatrol(patrolId);
    }

    @Getter
    @Setter
    private static class UpdateRequest {
        private int id;

        private String route;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<String> updatePatrol(@RequestBody PatrolController.UpdateRequest updateRequest,
                                               @PathVariable("id") Integer idForUpdating) {
        patrolService.updatePatrol(updateRequest.getRoute(), updateRequest.getStartTime(), updateRequest.getEndTime(),
                idForUpdating);
        return ResponseEntity.ok("Patrol updated successfully");
    }

    @PostMapping(path = "/export")
    public ResponseEntity<String> exportAllPatrols(@RequestParam("folderpath") String folderpath) {
        Map<Integer, Patrol> patrolsMap = patrolService.getPatrolsMap();
        try {
            patrolService.exportAllPatrols(patrolsMap, folderpath);
            return ResponseEntity.ok("All patrols files were created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error encoding filepath");
        }
    }

}
