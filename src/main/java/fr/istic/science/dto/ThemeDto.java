package fr.istic.science.dto;

import fr.istic.science.model.Event;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ThemeDto {
    private String title;
    private String description;

}

