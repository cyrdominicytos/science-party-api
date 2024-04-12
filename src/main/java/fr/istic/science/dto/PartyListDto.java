package fr.istic.science.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class PartyListDto {
    private Long id;
    private String tagName;
    private LocalDateTime dateCreation;
    private LocalDateTime dateInit;
    private LocalDateTime dateEnd;
}
