package fr.istic.science.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class FileManagerService {

    public final static  String FOLDER_PATH="src/data";
    public final static  String DEFAULT_FILE="/event_default.jpg";

    public static String uploadImageToFileSystem(MultipartFile file) throws IOException {
        String filePath=FOLDER_PATH+file.getOriginalFilename();
       /* FileData fileData=fileDataRepository.save(FileData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath).build());*/

        if (file != null) {
            file.transferTo(new File(filePath));
            return filePath;
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
