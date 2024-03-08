package fr.istic.science.repository;

import fr.istic.science.model.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
    // You can define additional methods here if needed
}
