package fr.istic.science.dto;


import lombok.Data;


@Data
public class ParcourDto {
    private String title;
    private String description;
    private boolean isPublished = false;
    private  Long user_id;
    private Long party_id;

}

