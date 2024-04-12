package fr.istic.science.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ThemeListDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dateCreation;
}

