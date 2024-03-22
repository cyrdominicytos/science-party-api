package fr.istic.science.controller;

import fr.istic.science.exception.ResourceNotFoundException;
import fr.istic.science.model.Event;
import fr.istic.science.model.Tag;
import fr.istic.science.model.Theme;
import fr.istic.science.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<Object> createEvent(@RequestBody Event event) {
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
    public ResponseEntity<Object> getUsers() {
        List<Event> events = eventService.getEvents();
        return ResponseEntity.status(HttpStatus.OK).body(events);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId, @RequestBody Event eventDetails) {
        Event updatedEvent = eventService.updateEvent(eventId, eventDetails);
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{eventId}/rate")
    public ResponseEntity<Event> rateEvent(@PathVariable Long eventId, @RequestParam int rating) {
        Event ratedEvent = eventService.rateEvent(eventId, rating);
        return new ResponseEntity<>(ratedEvent, HttpStatus.OK);
    }
}

