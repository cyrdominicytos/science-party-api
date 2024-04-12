package fr.istic.science.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tagName;
    private LocalDateTime dateCreation =  LocalDateTime.now();
    // Relationships
    //@ManyToMany(mappedBy = "tags")
    //private List<Event> events;
}

