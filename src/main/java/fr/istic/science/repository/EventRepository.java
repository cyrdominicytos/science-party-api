package fr.istic.science.repository;

import fr.istic.science.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    // You can define additional methods here if needed
}

