package fr.istic.science.controller;

import fr.istic.science.dto.EventListDto;
import fr.istic.science.dto.ParcourDto;
import fr.istic.science.dto.ParcourListDto;
import fr.istic.science.exception.ResourceNotFoundException;
import fr.istic.science.model.*;
import fr.istic.science.service.EventService;
import fr.istic.science.service.ParcourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/parcours")
public class ParcourController {

    @Autowired
    private ParcourService parcourService;
    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<Object> createParcour(@RequestBody ParcourDto parcour) {
        Parcour createdParcour = parcourService.createParcour(parcour);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdParcour);
    }

    @GetMapping("/{parcourId}")
    public ResponseEntity<Object> getParcourById(@PathVariable Long parcourId) {

        try{
            ParcourListDto parcour = parcourService.getParcourById(parcourId);
            return ResponseEntity.status(HttpStatus.OK).body(parcour);
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Le parcour avec l'identifiant "+parcourId+" n'existe pas !");
        }
    }
    @GetMapping("")
    public ResponseEntity<Object> getParcours() {
        List<ParcourListDto> parcours = parcourService.getParcours();
        return ResponseEntity.status(HttpStatus.OK).body(parcours);
    }

    @GetMapping("/pagination")
    public ResponseEntity<Object> getParcoursPagination(Pageable pageable) {
        Page<ParcourListDto> parcours = parcourService.getParcoursWithPagination(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(parcours);
    }

    @GetMapping("/filter")
    public ResponseEntity<Object> getParcoursFilter(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description
    ) {
        List<ParcourListDto> parcours = parcourService.getFilteredParcours(title, description);
        return ResponseEntity.status(HttpStatus.OK).body(parcours);
    }

    @PutMapping("/{parcourId}")
    public ResponseEntity<ParcourListDto> updateParcour(@PathVariable Long parcourId, @RequestBody Parcour parcourDetails) {
        ParcourListDto updatedParcour = parcourService.updateParcour(parcourId, parcourDetails);
        return new ResponseEntity<>(updatedParcour, HttpStatus.OK);
    }

    @DeleteMapping("/{parcourId}")
    public ResponseEntity<Void> deleteParcour(@PathVariable Long parcourId) {
        parcourService.deleteParcour(parcourId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping("/{parcourId}/events")
    public ResponseEntity<Object> addEventsToParcour(
            @PathVariable Long parcourId,
            @RequestBody List<Long> eventIds
    ) {
        ParcourListDto parcour = parcourService.getParcourById(parcourId);
        if (parcour == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parcour not found");
        }

        List<Event> events = eventService.getEventsByIds(eventIds);
        //parcour.getEvents().addAll(events);
        //parcourService.updateParcour()
        parcourService.updateParcourEvents(parcourId,events);
        return ResponseEntity.status(HttpStatus.OK).body("Events associated with parcour successfully");
    }

}

