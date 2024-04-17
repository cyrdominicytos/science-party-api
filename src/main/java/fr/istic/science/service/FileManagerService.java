package fr.istic.science.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileManagerService {

    public final static  String FOLDER_PATH="src/data";
    public final static  String DEFAULT_FILE="/event_default.jpg";

    public static String uploadImageToFileSystem(MultipartFile file) throws IOException {
        String filePath=FOLDER_PATH+"/"+file.getOriginalFilename();

        if (file != null && !file.isEmpty()) {
           /* System.out.println("File found..."+filePath);
            File directory = new File(FOLDER_PATH);
            if (!directory.exists()) {
                directory.mkdirs();  // Crée le répertoire s'il n'existe pas
            }

            file.transferTo(new File("./"+filePath));
            return filePath;*/

            String fileName = file.getOriginalFilename();
            Path destinationPath = Paths.get(FOLDER_PATH, fileName);

            if (!Files.exists(Paths.get(FOLDER_PATH))) {
                Files.createDirectories(Paths.get(FOLDER_PATH)); // Crée le répertoire s'il n'existe pas
            }

            Files.copy(file.getInputStream(), destinationPath); // Copie le fichier dans le répertoire de destination
            System.out.println("File saved... " + destinationPath.toString());
            return destinationPath.toString();
        }else
            return FOLDER_PATH+DEFAULT_FILE;
    }

    public static byte[] downloadImageFromFileSystem(String filename) throws IOException {
        File f = new File(FOLDER_PATH+"/"+filename);
        if(!f.exists()) {
            System.out.println("not found "+filename);
             f = new File(FOLDER_PATH+DEFAULT_FILE);
        }
        return Files.readAllBytes(f.toPath());
    }

    /*public String uploadImage(MultipartFile file) throws IOException {
        ImageData imageData = repository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes())).build());
        if (imageData != null) {
            return "file uploaded successfully : " + file.getOriginalFilename();
        }
        return null;
    }*/



    /*public byte[] downloadImage(String fileName) {
        Optional<ImageData> dbImageData = repository.findByName(fileName);
        byte[] images = ImageUtils.decompressImage(dbImageData.get().getImageData());
        return images;
    }*/




    /*public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<FileData> fileData = fileDataRepository.findByName(fileName);
        String filePath=fileData.get().getFilePath();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }*/

}
