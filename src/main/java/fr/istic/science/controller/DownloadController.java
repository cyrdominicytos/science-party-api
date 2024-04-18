package fr.istic.science.controller;

import fr.istic.science.service.DownloadService;
import fr.istic.science.service.EventService;
import fr.istic.science.service.FileManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@RestController
@RequestMapping("/download")
public class DownloadController {
    @Autowired
    private DownloadService downloadService;

    @GetMapping("")
    public ResponseEntity<Object> importOpenData() {
        String url_json = "https://www.data.gouv.fr/fr/datasets/r/76cf3202-3357-4379-b47e-dd24bc72688a";
        //downloadService.loadOpenData(url_json, false);
        try {
            FileManagerService.downloadAndSaveImageFromUrl("https://cibul.s3.amazonaws.com/evfevent_ateliers-de-materiel-numerique_505_551707.jpg");
        } catch (IOException e) {
            System.out.println("Erreur:"+e.getMessage());
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Demande d'importation des données lancées avec succès !");
    }

    @GetMapping("/loadEventImage/{fileName}")
    public ResponseEntity<?> loadEventImage(@PathVariable String fileName) {
        byte[] imageData= new byte[0];
        try {
            imageData = FileManagerService.downloadImageFromFileSystem(fileName);
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            fileExtension = fileExtension.isEmpty() ?  "jpg" : fileExtension;
            System.out.println("extenxion =>"+fileExtension);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf("image/"+fileExtension))
                    .body(imageData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/test")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {

        String uploadImage = "Called !!";
        try {
            System.out.println("=============="+file.getOriginalFilename()+"============");
            uploadImage = FileManagerService.uploadImageToFileSystem(file);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Image enregistrée avec succès !");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }

    }

}

