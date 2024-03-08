package fr.istic.science.repository;

import fr.istic.science.model.Parcour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParcourRepository extends JpaRepository<Parcour, Long> {
    // You can define additional methods here if needed
}

