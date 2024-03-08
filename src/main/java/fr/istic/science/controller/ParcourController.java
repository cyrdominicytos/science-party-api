package fr.istic.science.controller;

import fr.istic.science.model.Parcour;
import fr.istic.science.model.User;
import fr.istic.science.service.ParcourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parcours")
public class ParcourController {

    @Autowired
    private ParcourService parcourService;

    @PostMapping
    public ResponseEntity<Parcour> createParcour(@RequestBody Parcour parcour) {
        Parcour createdParcour = parcourService.createParcour(parcour);
        return new ResponseEntity<>(createdParcour, HttpStatus.CREATED);
    }

    @GetMapping("/{parcourId}")
    public ResponseEntity<Parcour> getParcourById(@PathVariable Long parcourId) {
        Parcour parcour = parcourService.getParcourById(parcourId);
        return new ResponseEntity<>(parcour, HttpStatus.OK);
    }

    // Other CRUD endpoints for Parcour
}

