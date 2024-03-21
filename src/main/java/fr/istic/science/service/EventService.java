package fr.istic.science.service;

import fr.istic.science.exception.ResourceNotFoundException;
import fr.istic.science.model.Event;
import fr.istic.science.model.Parcour;
import fr.istic.science.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));
    }

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }
}

