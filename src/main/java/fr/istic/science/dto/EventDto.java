package fr.istic.science.dto;

import fr.istic.science.model.*;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data

public class EventDto {

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
    private LocalDateTime dateInit;
    private LocalDateTime dateEnd;

}

