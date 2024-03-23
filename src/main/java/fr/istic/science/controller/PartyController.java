package fr.istic.science.controller;

import fr.istic.science.exception.ResourceNotFoundException;
import fr.istic.science.model.Party;
import fr.istic.science.service.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/partys")
public class PartyController {

    @Autowired
    private PartyService partyService;

    @PostMapping
    public ResponseEntity<Object> createParty(@RequestBody Party party) {
        Party createdParty = partyService.createParty(party);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdParty);
    }

    @GetMapping("/{partyId}")
    public ResponseEntity<Object> getPartyById(@PathVariable Long partyId) {
        try{
            Party party = partyService.getPartyById(partyId);
            return ResponseEntity.status(HttpStatus.OK).body(party);
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Le party avec l'identifiant "+partyId+" n'existe pas !");
        }
    }

    @GetMapping("")
    public ResponseEntity<Object> gePartys() {
        List<Party> partys = partyService.getPartys();
        return ResponseEntity.status(HttpStatus.OK).body(partys);
    }

    @PutMapping("/{partyId}")
    public ResponseEntity<Party> updateParty(@PathVariable Long partyId, @RequestBody Party partyDetails) {
        Party updatedParty = partyService.updateParty(partyId, partyDetails);
        return new ResponseEntity<>(updatedParty, HttpStatus.OK);
    }

    @DeleteMapping("/{partyId}")
    public ResponseEntity<Void> deleteParty(@PathVariable Long partyId) {
        partyService.deleteParty(partyId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

