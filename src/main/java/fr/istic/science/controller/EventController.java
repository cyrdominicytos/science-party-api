package fr.istic.science.controller;

import fr.istic.science.dto.EventDto;
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

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<Object> createEvent(@RequestBody EventDto event) {
        Event createdEvent = eventService.createEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Object> getEventById(@PathVariable Long eventId) {
        try{
            Event event = eventService.getEventById(eventId);
            return ResponseEntity.status(HttpStatus.OK).body(event);
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'évènement avec l'identifiant "+eventId+" n'existe pas !");
        }
    }
    @GetMapping("")
    public ResponseEntity<Object> getEvents() {
        List<Event> events = eventService.getEvents();
        return ResponseEntity.status(HttpStatus.OK).body(events);
    }

    @GetMapping("/pagination")
    public ResponseEntity<Object> getEventsPagination(Pageable pageable) {
        Page<Event> eventsPage = eventService.getEventsWithPagination(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(eventsPage);
    }

    @GetMapping("/filter")
    public ResponseEntity<Object> getEventsFilter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String themeDescription,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateCreation
    ) {
        List<Event> events = eventService.getFilteredEvents(name, themeDescription, dateCreation);
        return ResponseEntity.status(HttpStatus.OK).body(events);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId, @RequestBody EventDto eventDetails) {
        Event updatedEvent = eventService.updateEvent(eventId, eventDetails);
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/rate/{eventId}")
    public ResponseEntity<Event> rateEvent(@PathVariable Long eventId, @RequestParam int rating) {
        Event ratedEvent = eventService.rateEvent(eventId, rating);
        return new ResponseEntity<>(ratedEvent, HttpStatus.OK);
    }

    @PutMapping("/publish/{eventId}")
    public ResponseEntity<Event> publishEvent(@PathVariable Long eventId, @RequestParam boolean value) {
        Event ratedEvent = eventService.publishEvent(eventId, value);
        return new ResponseEntity<>(ratedEvent, HttpStatus.OK);
    }

    @PutMapping("/indicateFull/{eventId}")
    public ResponseEntity<Event> indicateFullEvent(@PathVariable Long eventId, @RequestParam double value) {
        Event ratedEvent = eventService.indicateFullEvent(eventId, value);
        return new ResponseEntity<>(ratedEvent, HttpStatus.OK);
    }

}

