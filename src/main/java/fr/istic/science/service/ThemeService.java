package fr.istic.science.service;

import fr.istic.science.dto.ThemeDto;
import fr.istic.science.exception.ResourceNotFoundException;
import fr.istic.science.model.Theme;
import fr.istic.science.model.User;
import fr.istic.science.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeService {

    @Autowired
    private ThemeRepository themeRepository;

    public Theme createTheme(Theme theme) {
        return themeRepository.save(theme);
    }

    public Theme getThemeById(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new ResourceNotFoundException("Theme", "id", themeId));
    }

    public List<Theme> getThemes() {
        return themeRepository.findAll();
    }

    public Theme updateTheme(Long themeId, ThemeDto themeDetails) {
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ResourceNotFoundException("Theme", "id", themeId));

        theme.setTitle(themeDetails.getTitle());
        theme.setDescription(themeDetails.getDescription());

        return themeRepository.save(theme);
    }

    public void deleteTheme(Long themeId) {
        themeRepository.deleteById(themeId);
    }
}

