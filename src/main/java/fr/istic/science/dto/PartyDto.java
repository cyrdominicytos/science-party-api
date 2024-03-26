package fr.istic.science.dto;

import fr.istic.science.model.Event;
import fr.istic.science.model.Parcour;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class PartyDto {
    private String tagName;
    private LocalDateTime dateInit;
    private LocalDateTime dateEnd;
}
