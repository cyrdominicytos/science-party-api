package fr.istic.science.service;

import fr.istic.science.dto.*;
import fr.istic.science.exception.ResourceNotFoundException;
import fr.istic.science.model.*;
import fr.istic.science.repository.*;
import jakarta.xml.bind.annotation.XmlType;
import org.hibernate.boot.model.source.internal.hbm.AttributesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
    @Autowired
    private TagRepository tagRepository;
    private final String DEFAULT_IMAGE_BASE_URL = FileManagerService .FOLDER_PATH+FileManagerService.DEFAULT_FILE;

    public EventListDto createEvent(EventDto event) {
        System.out.println("In ...createEvent");
        Event e = new Event();
        getEvent(e, event);
        return convertToEventListDto(eventRepository.save(e));
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
        e.setDescription(event.getDescription());
        e.setLatitude(event.getLatitude());
        e.setLongitude(event.getLongitude());
        e.setPhone(event.getPhone());
        e.setPublished(event.isPublished());
        e.setFreeEvent(event.isFreeEvent());
        e.setDateInit(event.getDateInit());
        e.setDateEnd(event.getDateEnd());
        e.setEmail(event.getEmail());
        e.setFacebookUrl(event.getFacebookUrl());
        e.setInstagramUrl(event.getInstagramUrl());
        e.setAmount(event.getAmount());
        e.setAddress(event.getAddress());


        e.setParty(party.get());
        e.setUser(u.get());
        e.setTheme(theme.get());

        if(event.getImage()!=null){
            try {
                String path =  FileManagerService.uploadImageToFileSystem(event.getImage());
                e.setImageUrl(path);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }else{
            System.out.println("Image not receive");
            System.out.println(event.getImage());
            e.setImageUrl(FileManagerService.DEFAULT_FILE);
        }

        List<Tag> tags = tagRepository.findAllById(event.getTags());
        System.out.println("==========Tag size "+tags.size());
        if(!tags.isEmpty()){
            if(e.getTags()==null)
                e.setTags(new ArrayList<>());

            for(Tag t : tags)
                e.getTags().add(t);
        }
        return e;
    }

    public static EventListDto convertToEventListDto(Event event) {
        EventListDto e = new EventListDto();
        e.setId(event.getId());
        e.setName(event.getName());
        e.setPhone(event.getPhone());
        e.setPublished(event.isPublished());
        e.setFreeEvent(event.isFreeEvent());
        e.setDateInit(event.getDateInit());
        e.setDateEnd(event.getDateEnd());
        e.setDateCreation(event.getDateCreation());
        e.setEmail(event.getEmail());
        e.setFacebookUrl(event.getFacebookUrl());
        e.setInstagramUrl(event.getInstagramUrl());
        e.setAmount(event.getAmount());
        e.setDescription(event.getDescription());
        e.setAddress(event.getAddress());
        e.setFullIndicator(event.getFullIndicator());
        e.setLatitude(event.getLatitude());
        e.setLongitude(event.getLongitude());
        e.setRate(event.getRate());
        if(event.getImageUrl()!=null && !event.getImageUrl().isEmpty())
            e.setImageUrl(FileManagerService.DEFAULT_IMAGE_BASE_URL+"/"+event.getImageUrl());
        else
            e.setImageUrl(FileManagerService.DEFAULT_IMAGE_BASE_URL+"/"+FileManagerService.DEFAULT_FILE);
        //set party
        Party p1 = event.getParty();
        if(p1!=null)
        {
            PartyListDto p = new PartyListDto();
            p.setDateCreation(p1.getDateCreation());
            p.setTagName(p1.getTagName());
            p.setDateEnd(p1.getDateEnd());
            p.setId(p1.getId());
           e.setParty(p);
        }

        //setTheme
        Theme th1 = event.getTheme();
        if(th1!=null){
            ThemeListDto th = new ThemeListDto();
            th.setId(th1.getId());
            th.setTitle(th1.getTitle());
            th.setDescription(th1.getDescription());
            th.setDateCreation(th1.getDateCreation());
            e.setTheme(th);
        }

        //set parcour
        Parcour parcour = event.getParcour();
        if(parcour!=null){
            e.setParcour_id(parcour.getId());
        }

        //set Tags
        if(event.getTags()!=null){
            List<TagListDto> taList = new ArrayList<>();
            for(Tag ta : event.getTags()){
                taList.add(TagService.convertToTagListDto(ta));
            }
            e.setTags(taList);
        }
        //set user
        if(event.getUser()!=null)
            e.setUser(event.getUser());
        return e;
    }

    public EventListDto getEventById(Long eventId) {
        Event e = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));
        return convertToEventListDto(e);
    }

    public List<EventListDto> getEvents() {
        List<Event> list = eventRepository.findAll();
        List<EventListDto> events = new ArrayList<>();
        for(Event e : list){
            EventListDto ev = convertToEventListDto(e);
            events.add(ev);
        }
        return events;
    }


    public Page<EventListDto> getEventsWithPagination(Pageable pageable) {
        System.out.println("=========== size TITI 1 =======");
        Page<EventListDto> list = new PageImpl<>(Collections.emptyList());
        System.out.println("=========== size TITI 2 =======");

        try{
            Page<Event> pages =  new PageImpl<>(Collections.emptyList());

            pages =   eventRepository.findAll(pageable);
            System.out.println("=========== size TITI 3 =======");
            System.out.println("===========size before "+pages.getSize());
            for(Event e : pages){
                System.out.println("E_name "+e.getName());
                System.out.println("E_name convert "+convertToEventListDto(e).getName());
                // Ajout d'un élément à la liste
                List<EventListDto> content = new ArrayList<>(list.getContent());
                content.add(convertToEventListDto(e));
                // Mise à jour de la liste dans l'objet Page
                 list = new PageImpl<>(content, list.getPageable(), list.getTotalElements());
            }


            System.out.println("===========size after "+list.getSize());
        }catch(Exception e){
            System.out.println("=========== size TITI error ======= "+e.getMessage());
        }
        return list;
    }

    public EventListDto updateEvent(Long eventId, EventDto eventDetails) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));
        getEvent(event, eventDetails);
        return convertToEventListDto(eventRepository.save(event));
    }

    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

    public EventListDto rateEvent(Long eventId, int rating) {
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

        return convertToEventListDto(eventRepository.save(event));
    }
 public EventListDto publishEvent(Long eventId, boolean value) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        event.setPublished(value);
        return convertToEventListDto(eventRepository.save(event));
    }

    public EventListDto indicateFullEvent(Long eventId, double value) {
        if(value < 0 || value > 100)
            throw new ResourceNotFoundException("Le champs value doit être compris entre 0 et 100", "id",eventId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));
        event.setFullIndicator(value);
        Event e =  eventRepository.save(event);
        return convertToEventListDto(e);
    }

    public List<EventListDto> getFilteredEvents(String name, String themeDescription, LocalDateTime dateCreation) {
        List<Event> list = new ArrayList<>();
        if (name != null && !name.isEmpty() && themeDescription != null && !themeDescription.isEmpty() && dateCreation != null) {
            list = eventRepository.findByNameAndThemeDescriptionAndDateCreation(name, themeDescription, dateCreation);
        } else if (name != null && !name.isEmpty() &&  themeDescription != null && !themeDescription.isEmpty()) {
            list =  eventRepository.findByNameAndThemeDescription(name, themeDescription);
        } else if (name != null && !name.isEmpty() && dateCreation != null) {
            list =  eventRepository.findByNameAndDateCreation(name, dateCreation);
        } else if (themeDescription != null && !themeDescription.isEmpty() && dateCreation != null) {
            list =  eventRepository.findByThemeDescriptionAndDateCreation(themeDescription, dateCreation);
        } else if (name != null && !name.isEmpty()) {
            list =  eventRepository.findByName(name);
        } else if (themeDescription != null && !themeDescription.isEmpty()) {
            list =  eventRepository.findByThemeDescription(themeDescription);
        } else if (dateCreation != null) {
            list =  eventRepository.findByDateCreation(dateCreation);
        } else {
            // If no filters are applied, return all events
            list =  eventRepository.findAll();
        }

        List<EventListDto> events = new ArrayList<>();
        for(Event e : list){
            EventListDto ev = convertToEventListDto(e);
            events.add(ev);
        }
        return events;
    }

    public List<Event> getEventsByIds(List<Long> eventIds) {
        return eventRepository.findAllById(eventIds);
    }
    public List<EventListDto> getEventsByIds2(List<Long> eventIds) {
        List<Event> list = eventRepository.findAllById(eventIds);
        List<EventListDto> events = new ArrayList<>();
        for(Event e : list){
            EventListDto ev = convertToEventListDto(e);
            events.add(ev);
        }
        return events;
    }

}