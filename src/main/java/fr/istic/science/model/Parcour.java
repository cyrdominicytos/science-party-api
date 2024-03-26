package fr.istic.science.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Parcour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private boolean isPublished;
    private LocalDateTime dateCreation =  LocalDateTime.now();
    // Relationships
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "parcour")
    private List<Event> events;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

}

