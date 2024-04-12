package fr.istic.science.service;

import fr.istic.science.dto.*;
import fr.istic.science.exception.ResourceNotFoundException;
import fr.istic.science.model.*;
import fr.istic.science.repository.EventRepository;
import fr.istic.science.repository.ParcourRepository;
import fr.istic.science.repository.PartyRepository;
import fr.istic.science.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ParcourService {

    @Autowired
    private ParcourRepository parcourRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private UserRepository userRepository;

    public Parcour createParcour(ParcourDto parcour) {

        Optional<Party> party = partyRepository.findById(parcour.getParty_id());
        if(party.isEmpty())
            throw new ResourceNotFoundException("Le party avec cet id n'existe pas !", "id", parcour.getParty_id());

        Optional<User> u = userRepository.findById(parcour.getUser_id());
        if(u.isEmpty())
            throw new ResourceNotFoundException("Le User avec cet id n'existe pas !", "id", parcour.getUser_id());

        Parcour p = new Parcour();
        p.setPublished(parcour.isPublished());
        p.setTitle(parcour.getTitle());
        p.setDescription(parcour.getDescription());
        p.setParty(party.get());
        p.setUser(u.get());

        return parcourRepository.save(p);
    }

    public ParcourListDto getParcourById(Long parcourId) {
      Parcour p =  parcourRepository.findById(parcourId)
                .orElseThrow(() -> new ResourceNotFoundException("Parcour", "id", parcourId));
        return  convertToParcourListDto(p);
    }

    public List<ParcourListDto> getParcours() {
        List<ParcourListDto> list = new ArrayList<>();
        for(Parcour p : parcourRepository.findAll()){
            list.add(convertToParcourListDto(p));
        }
        return list;
    }

    public Page<ParcourListDto> getParcoursWithPagination(Pageable pageable) {
        Page<ParcourListDto> list = new PageImpl<>(Collections.emptyList());
        Page<Parcour> pages =   parcourRepository.findAll(pageable);
        for(Parcour p : pages)
            list.getContent().add(convertToParcourListDto(p));
        //return parcourRepository.findAll(pageable);
        return list;
    }

    public ParcourListDto updateParcour(Long parcourId, Parcour parcourDetails) {
        Parcour parcour = parcourRepository.findById(parcourId)
                .orElseThrow(() -> new ResourceNotFoundException("Parcour", "id", parcourId));

        parcour.setTitle(parcourDetails.getTitle());
        parcour.setDescription(parcourDetails.getDescription());
        return convertToParcourListDto(parcourRepository.save(parcour));
    }

    public ParcourListDto updateParcourEvents(Long parcourId, List<Event> events) {
        Parcour parcour = parcourRepository.findById(parcourId)
                .orElseThrow(() -> new ResourceNotFoundException("Parcour", "id", parcourId));
        if (events.isEmpty()) {
            throw new ResourceNotFoundException("Vous devez envoyer des ids d'évènement valids", "id",null);
        }
        //System.out.println("===========b "+events.size());
        for(Event e : events){
            e.setParcour(parcour);
            eventRepository.save(e);
        }
        // parcour.getEvents().addAll(events);
        //System.out.println("===========a "+parcour.getEvents().size());
        return convertToParcourListDto(parcourRepository.saveAndFlush(parcour));
    }

    public void deleteParcour(Long parcourId) {
        parcourRepository.deleteById(parcourId);
    }


    public List<ParcourListDto> getFilteredParcours(String title, String description) {
        List<Parcour> list = new ArrayList<>();
        if (title != null && !title.isEmpty() &&  description != null && !description.isEmpty()) {
            list =  parcourRepository.findByTitleAndDescription(title, description);
        } else if (title != null && !title.isEmpty()) {
            list =  parcourRepository.findByTitle(title);
        } else if (description != null && !description.isEmpty()) {
            list =  parcourRepository.findByDescription(description);
        } else {
            // If no filters are applied, return all parcours
            list =  parcourRepository.findAll();
        }
        List<ParcourListDto> list1 = new ArrayList<>();
        for(Parcour p : list)
            list1.add(convertToParcourListDto(p));
        return  list1;
    }


    private ParcourListDto convertToParcourListDto(Parcour parcour) {
        ParcourListDto p = new ParcourListDto();
        p.setId(parcour.getId());
        p.setTitle(parcour.getTitle());
        p.setDescription(parcour.getDescription());

        //set party
        Party p1 = parcour.getParty();
        if(p1!=null)
        {
            PartyListDto p2 = new PartyListDto();
            p2.setDateCreation(p1.getDateCreation());
            p2.setTagName(p1.getTagName());
            p2.setDateEnd(p1.getDateEnd());
            p2.setId(p1.getId());
            p.setParty(p2);
        }

        //setEvent list
        List<Event> list = eventRepository.findAll();
        List<EventListDto> events = new ArrayList<>();
        for(Event e : parcour.getEvents()){
            EventListDto ev = EventService.convertToEventListDto(e);
            events.add(ev);
        }
        p.setEvents(events);
        //set user
        p.setUser(parcour.getUser());
        return p;
    }
}

