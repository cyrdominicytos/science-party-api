package fr.istic.science.repository;

import fr.istic.science.model.Event;
import fr.istic.science.model.Parcour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ParcourRepository extends JpaRepository<Parcour, Long> {

    List<Parcour> findByTitleAndDescription(String title, String description);

    List<Parcour> findByTitle(String title);

    List<Parcour> findByDescription(String description);
}

