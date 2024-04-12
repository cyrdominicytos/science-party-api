package fr.istic.science.dto;


import fr.istic.science.model.Event;
import fr.istic.science.model.Party;
import fr.istic.science.model.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class ParcourListDto {

    private Long id;
    private String title;
    private String description;
    private boolean isPublished;
    private LocalDateTime dateCreation;

    // Relationships
    private User user;
    private List<EventListDto> events;
    private PartyListDto party;

}

