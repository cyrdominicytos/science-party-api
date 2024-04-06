package fr.istic.science.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int rate;
    private int totalRating;
    //private String place;
    private String address;
    private double fullIndicator;
    private boolean isPublished;
    private String phone;
    private String email;
    private String facebookUrl;
    private String instagramUrl;
    private String imageUrl;
    private boolean isFreeEvent;
    private float amount;
    private LocalDateTime dateCreation =  LocalDateTime.now();
    private LocalDateTime dateInit;
    private LocalDateTime dateEnd;
    // Relationships
    @ManyToOne
    @JoinColumn(name = "parcour_id", nullable = true)
    private Parcour parcour;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @ManyToMany
    private List<Tag> tags;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;
}

