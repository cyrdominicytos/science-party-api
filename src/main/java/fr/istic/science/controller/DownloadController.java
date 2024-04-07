package fr.istic.science.controller;

import fr.istic.science.service.DownloadService;
import fr.istic.science.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/download")
public class DownloadController {
    @Autowired
    private DownloadService downloadService;

    @GetMapping("")
    public ResponseEntity<Object> ImportOpenData() {
        String url_json = "https://www.data.gouv.fr/fr/datasets/r/76cf3202-3357-4379-b47e-dd24bc72688a";
        downloadService.loadOpenData(url_json, false);
        return ResponseEntity.status(HttpStatus.OK).body("Demande d'importation des données lancées avec succès !");
    }


}

