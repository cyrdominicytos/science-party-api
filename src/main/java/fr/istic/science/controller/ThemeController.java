package fr.istic.science.controller;

import fr.istic.science.exception.ResourceNotFoundException;
import fr.istic.science.model.Theme;
import fr.istic.science.model.User;
import fr.istic.science.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    @Autowired
    private ThemeService themeService;

    @PostMapping
    public ResponseEntity<Object> createTheme(@RequestBody Theme theme) {
        Theme createdTheme = themeService.createTheme(theme);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTheme);
    }

    @GetMapping("/{themeId}")
    public ResponseEntity<Object> getThemeById(@PathVariable Long themeId) {
        try{
            Theme theme = themeService.getThemeById(themeId);
            return ResponseEntity.status(HttpStatus.OK).body(theme);
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Le theme avec l'identifiant "+themeId+" n'existe pas !");
        }
    }

    @GetMapping("")
    public ResponseEntity<Object> getUsers() {
        List<Theme> themes = themeService.getThemes();
        return ResponseEntity.status(HttpStatus.OK).body(themes);
    }

    @PutMapping("/{themeId}")
    public ResponseEntity<Theme> updateTheme(@PathVariable Long themeId, @RequestBody Theme themeDetails) {
        Theme updatedTheme = themeService.updateTheme(themeId, themeDetails);
        return new ResponseEntity<>(updatedTheme, HttpStatus.OK);
    }

    @DeleteMapping("/{themeId}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long themeId) {
        themeService.deleteTheme(themeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
