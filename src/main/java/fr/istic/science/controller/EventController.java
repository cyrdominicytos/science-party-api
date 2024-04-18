package fr.istic.science.controller;

import fr.istic.science.dto.EventDto;
import fr.istic.science.dto.EventListDto;
import fr.istic.science.exception.ResourceNotFoundException;
import fr.istic.science.model.Event;
import fr.istic.science.model.Tag;
import fr.istic.science.model.Theme;
import fr.istic.science.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    //public ResponseEntity<Object> createEvent(@RequestBody EventDto event, @RequestParam("file") MultipartFile file) {
    public ResponseEntity<Object> createEvent(@ModelAttribute EventDto event) {
        try{
            System.out.println(event);
            System.out.println(event.getName());
            System.out.println(event.getImage());
            if(event.getImage()!=null)
                System.out.println("Image not null: "+event.getImage().getName() + " "+event.getImage().getSize());

            EventListDto createdEvent = eventService.createEvent(event);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erreur:"+e.getMessage());
        }
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Object> getEventById(@PathVariable Long eventId) {
        try{
            EventListDto event = eventService.getEventById(eventId);
            return ResponseEntity.status(HttpStatus.OK).body(event);
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'évènement avec l'identifiant "+eventId+" n'existe pas !");
        }
    }
    @GetMapping("")
    public ResponseEntity<Object> getEvents() {
        List<EventListDto> events = eventService.getEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
        //ResponseEntity.status(HttpStatus.OK).body(events);
    }

    @GetMapping("/pagination")
    public ResponseEntity<Object> getEventsPagination(Pageable pageable) {
        Page<EventListDto> eventsPage = eventService.getEventsWithPagination(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(eventsPage);
    }

    @GetMapping("/filter")
    public ResponseEntity<Object> getEventsFilter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String themeDescription,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateCreation
    ) {
        List<EventListDto> events = eventService.getFilteredEvents(name, themeDescription, dateCreation);
        return ResponseEntity.status(HttpStatus.OK).body(events);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Object> updateEvent(@PathVariable Long eventId, @ModelAttribute EventDto eventDetails) {
        try{
            EventListDto updatedEvent = eventService.updateEvent(eventId, eventDetails);
            return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erreur:"+e.getMessage());
        }

    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/rate/{eventId}")
    public ResponseEntity<EventListDto> rateEvent(@PathVariable Long eventId, @RequestParam int rating) {
        EventListDto ratedEvent = eventService.rateEvent(eventId, rating);
        return new ResponseEntity<>(ratedEvent, HttpStatus.OK);
    }

    @PutMapping("/publish/{eventId}")
    public ResponseEntity<EventListDto> publishEvent(@PathVariable Long eventId, @RequestParam boolean value) {
        EventListDto ratedEvent = eventService.publishEvent(eventId, value);
        return new ResponseEntity<>(ratedEvent, HttpStatus.OK);
    }

    @PutMapping("/indicateFull/{eventId}")
    public ResponseEntity<Object> indicateFullEvent(@PathVariable Long eventId, @RequestParam double value) {
        EventListDto ratedEvent = eventService.indicateFullEvent(eventId, value);
        return new ResponseEntity<>(ratedEvent, HttpStatus.OK);
    }

}

