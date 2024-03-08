package fr.istic.science.controller;

import fr.istic.science.model.Theme;
import fr.istic.science.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    @Autowired
    private ThemeService themeService;

    @PostMapping
    public ResponseEntity<Theme> createTheme(@RequestBody Theme theme) {
        Theme createdTheme = themeService.createTheme(theme);
        return new ResponseEntity<>(createdTheme, HttpStatus.CREATED);
    }

    @GetMapping("/{themeId}")
    public ResponseEntity<Theme> getThemeById(@PathVariable Long themeId) {
        Theme theme = themeService.getThemeById(themeId);
        return new ResponseEntity<>(theme, HttpStatus.OK);
    }

    // Other CRUD endpoints for Theme
}
