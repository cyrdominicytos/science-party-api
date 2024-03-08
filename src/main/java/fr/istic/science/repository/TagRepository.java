package fr.istic.science.repository;

import fr.istic.science.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    // You can define additional methods here if needed
}

