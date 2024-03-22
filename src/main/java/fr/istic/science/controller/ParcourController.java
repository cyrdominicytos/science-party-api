package fr.istic.science.controller;

import fr.istic.science.exception.ResourceNotFoundException;
import fr.istic.science.model.Parcour;
import fr.istic.science.model.Tag;
import fr.istic.science.model.Theme;
import fr.istic.science.model.User;
import fr.istic.science.service.ParcourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parcours")
public class ParcourController {

    @Autowired
    private ParcourService parcourService;

    @PostMapping
    public ResponseEntity<Object> createParcour(@RequestBody Parcour parcour) {
        Parcour createdParcour = parcourService.createParcour(parcour);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdParcour);
    }

    @GetMapping("/{parcourId}")
    public ResponseEntity<Object> getParcourById(@PathVariable Long parcourId) {

        try{
            Parcour parcour = parcourService.getParcourById(parcourId);
            return ResponseEntity.status(HttpStatus.OK).body(parcour);
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Le parcour avec l'identifiant "+parcourId+" n'existe pas !");
        }
    }
    @GetMapping("")
    public ResponseEntity<Object> getUsers() {
        List<Parcour> parcours = parcourService.getParcours();
        return ResponseEntity.status(HttpStatus.OK).body(parcours);
    }

    @PutMapping("/{parcourId}")
    public ResponseEntity<Parcour> updateParcour(@PathVariable Long parcourId, @RequestBody Parcour parcourDetails) {
        Parcour updatedParcour = parcourService.updateParcour(parcourId, parcourDetails);
        return new ResponseEntity<>(updatedParcour, HttpStatus.OK);
    }

    @DeleteMapping("/{parcourId}")
    public ResponseEntity<Void> deleteParcour(@PathVariable Long parcourId) {
        parcourService.deleteParcour(parcourId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

