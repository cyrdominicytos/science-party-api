package fr.istic.science.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private String name;
    private String surname;
    private String pseudo;
    private String email;
    private String password;
}
