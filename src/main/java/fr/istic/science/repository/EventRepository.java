package fr.istic.science.repository;

import fr.istic.science.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByNameAndThemeDescriptionAndDateCreation(String name, String themeDescription, LocalDateTime dateCreation);

    List<Event> findByNameAndThemeDescription(String name, String themeDescription);

    List<Event> findByNameAndDateCreation(String name, LocalDateTime dateCreation);

    List<Event> findByThemeDescriptionAndDateCreation(String themeDescription, LocalDateTime dateCreation);

    List<Event> findByName(String name);

    List<Event> findByThemeDescription(String themeDescription);

    List<Event> findByDateCreation(LocalDateTime dateCreation);
}

