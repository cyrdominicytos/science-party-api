package fr.istic.science.dto;

import fr.istic.science.model.*;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventListDto {
    /*private  Long id;
    private String name;
    private String longitude;
    private String latitude;
    private String phone;
    private String address;
    private boolean isFreeEvent;
    private boolean isPublished;
    //private MultipartFile image;
    private LocalDateTime dateInit;
    private LocalDateTime dateEnd;
    private  Long user_id;
    private Long party_id;
    private Long theme_id;

    private String email;
    private float amount;
    private String facebookUrl;
    private String instagramUrl;*/


    private Long id;
    private String name;
    private String description;
    private String longitude;
    private String latitude;
    private int rate;
    private int totalRating;
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
    private LocalDateTime dateCreation;
    private LocalDateTime dateInit;
    private LocalDateTime dateEnd;


    private Parcour parcour;
    private User user;

    private ThemeListDto theme;
    private List<Tag> tags;

    private PartyListDto party;

}

