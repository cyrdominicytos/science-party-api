package fr.istic.science.service;

import fr.istic.science.dto.EventDto;
import fr.istic.science.exception.ResourceNotFoundException;
import fr.istic.science.model.*;
import fr.istic.science.repository.EventRepository;
import fr.istic.science.repository.PartyRepository;
import fr.istic.science.repository.ThemeRepository;
import fr.istic.science.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ThemeRepository themeRepository;

    public Event createEvent(EventDto event) {
        System.out.println("In ...createEvent");
        Event e = new Event();
        System.out.println("In ...createEvent 2");
        getEvent(e, event);
        System.out.println("In ...createEvent 3");
        return eventRepository.save(e);
    }

    private Event getEvent(Event e, EventDto event) {
        Optional<Party> party = partyRepository.findById(event.getParty_id());
        if (party.isEmpty())
            throw new ResourceNotFoundException("Le party avec cet id n'existe pas !", "id", event.getParty_id());

        Optional<User> u = userRepository.findById(event.getUser_id());
        if (u.isEmpty())
            throw new ResourceNotFoundException("Le User avec cet id n'existe pas !", "id", event.getUser_id());

        Optional<Theme> theme = themeRepository.findById(event.getTheme_id());
        if (theme.isEmpty())
            throw new ResourceNotFoundException("Le theme avec cet id n'existe pas !", "id", event.getTheme_id());
        e.setName(event.getName());
        e.setPhone(event.getPhone());
        e.setPublished(event.isPublished());
        e.setFreeEvent(event.isFreeEvent());
        e.setDateInit(event.getDateInit());
        e.setDateEnd(event.getDateEnd());
        e.setEmail(event.getEmail());
        e.setFacebookUrl(event.getFacebookUrl());
        e.setInstagramUrl(event.getInstagramUrl());
        e.setAmount(event.getAmount());
        //e.setImageUrl(event.getImage().getOriginalFilename());
        e.setParty(party.get());
        e.setUser(u.get());
        e.setTheme(theme.get());
        e.setAddress(event.getAddress());
        return e;
    }

    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));
    }

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    public Event updateEvent(Long eventId, EventDto eventDetails) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        getEvent(event, eventDetails);
        event.setName(eventDetails.getName());
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
        int totalRatings = event.getTotalRating() + 1;
        int newRating = ((currentRating * event.getTotalRating()) + rating) / totalRatings;
        event.setRate(newRating);
        event.setTotalRating(totalRatings);

        return eventRepository.save(event);
    }
 public Event publishEvent(Long eventId, boolean value) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        event.setPublished(value);
        return eventRepository.save(event);
    }

    public Event indicateFullEvent(Long eventId, double value) {
        if(value < 0 || value > 100)
            throw new ResourceNotFoundException("Le champs value doit être compris entre 0 et 100", "id",eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));
        event.setFullIndicator(value);
        return eventRepository.save(event);
    }

}