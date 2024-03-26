package fr.istic.science.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tagName;
    private LocalDateTime dateCreation =  LocalDateTime.now();
    private LocalDateTime dateInit;
    private LocalDateTime dateEnd;

    // Relationships
    @OneToMany(mappedBy = "party")
    private List<Event> events;

    @OneToMany(mappedBy = "party")
    private List<Parcour> parcours;
}
