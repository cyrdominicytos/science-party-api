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

    public Event updateEvent(Long eventId, Event eventDetails) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        event.setName(eventDetails.getName());
        event.setRate(eventDetails.getRate());

        return eventRepository.save(event);
    }

    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

    public Event rateEvent(Long eventId, int rating) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        // Check if the rating is within the valid range (1 to 5)
        // It should be controlled in the frontend with only 5 stars of course ;)
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        int currentRating = event.getRate();
        int totalRatings = event.getTotalRatings() + 1;
        int newRating = ((currentRating * event.getTotalRatings()) + rating) / totalRatings;

        event.setRate(newRating);
        event.setTotalRatings(totalRatings);

        return eventRepository.save(event);
    }
}

