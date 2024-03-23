package fr.istic.science.repository;

import fr.istic.science.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository   extends JpaRepository<Party, Long> {
}
