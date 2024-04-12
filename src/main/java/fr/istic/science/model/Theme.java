package fr.istic.science.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Entity
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dateCreation =  LocalDateTime.now();
    // Relationships
    //@OneToMany(mappedBy = "theme")
    //private List<Event> events;

}

