package fr.istic.science.dto;

import fr.istic.science.model.Event;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TagListDto {
        private Long id;
        private String tagName;
        private LocalDateTime dateCreation;
}
