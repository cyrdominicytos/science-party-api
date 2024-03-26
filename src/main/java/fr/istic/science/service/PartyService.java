package fr.istic.science.service;

import fr.istic.science.dto.PartyDto;
import fr.istic.science.exception.ResourceNotFoundException;
import fr.istic.science.model.Party;
import fr.istic.science.repository.PartyRepository;
import fr.istic.science.repository.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartyService {
    @Autowired
    private PartyRepository partyRepository;

    public Party createParty(Party party) {
        return partyRepository.save(party);
    }

    public Party getPartyById(Long partyId) {
        return partyRepository.findById(partyId)
                .orElseThrow(() -> new ResourceNotFoundException("Party", "id", partyId));
    }
    public List<Party> getPartys() {
        return partyRepository.findAll();
    }

    public Party updateParty(Long partyId, PartyDto partyDetails) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new ResourceNotFoundException("Party", "id", partyId));

       party.setTagName(partyDetails.getTagName());
       party.setDateInit(partyDetails.getDateInit());
       party.setDateEnd(partyDetails.getDateEnd());

        return partyRepository.save(party);
    }

    public void deleteParty(Long partyId) {
        partyRepository.deleteById(partyId);
    }
}
