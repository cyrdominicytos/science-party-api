package fr.istic.science.service;

import fr.istic.science.dto.ParcourDto;
import fr.istic.science.exception.ResourceNotFoundException;
import fr.istic.science.model.Parcour;
import fr.istic.science.model.Party;
import fr.istic.science.model.Tag;
import fr.istic.science.model.User;
import fr.istic.science.repository.ParcourRepository;
import fr.istic.science.repository.PartyRepository;
import fr.istic.science.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParcourService {

    @Autowired
    private ParcourRepository parcourRepository;
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private UserRepository userRepository;

    public Parcour createParcour(ParcourDto parcour) {

        Optional<Party> party = partyRepository.findById(parcour.getParty_id());
        if(party.isEmpty())
            throw new ResourceNotFoundException("Le party avec cet id n'existe pas !", "id", parcour.getParty_id());

        Optional<User> u = userRepository.findById(parcour.getUser_id());
        if(u.isEmpty())
            throw new ResourceNotFoundException("Le User avec cet id n'existe pas !", "id", parcour.getUser_id());

        Parcour p = new Parcour();
        p.setPublished(parcour.isPublished());
        p.setTitle(parcour.getTitle());
        p.setDescription(parcour.getDescription());
        p.setParty(party.get());
        p.setUser(u.get());

        return parcourRepository.save(p);
    }

    public Parcour getParcourById(Long parcourId) {
        return parcourRepository.findById(parcourId)
                .orElseThrow(() -> new ResourceNotFoundException("Parcour", "id", parcourId));
    }

    public List<Parcour> getParcours() {
        return parcourRepository.findAll();
    }

    public Parcour updateParcour(Long parcourId, Parcour parcourDetails) {
        Parcour parcour = parcourRepository.findById(parcourId)
                .orElseThrow(() -> new ResourceNotFoundException("Parcour", "id", parcourId));

        parcour.setTitle(parcourDetails.getTitle());
        parcour.setDescription(parcourDetails.getDescription());
        return parcourRepository.save(parcour);
    }

    public void deleteParcour(Long parcourId) {
        parcourRepository.deleteById(parcourId);
    }

}

