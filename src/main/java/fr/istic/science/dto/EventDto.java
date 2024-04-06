package fr.istic.science.dto;

import fr.istic.science.model.*;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventDto {
    private String name;
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
    private String instagramUrl;

}

