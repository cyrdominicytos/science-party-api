package fr.istic.science.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileManagerService {

    public final static  String FOLDER_PATH="src/data";
    public final static  String DEFAULT_FILE="event_default.jpg";
    public final static String DEFAULT_IMAGE_BASE_URL = "download/loadEventImage";
    private static final RestTemplate restTemplate = new RestTemplate();
    public static String  uploadImageToFileSystem(MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {

            String fileName = file.getOriginalFilename();
            Path destinationPath = Paths.get(FOLDER_PATH, fileName);

            Path path = Paths.get(FOLDER_PATH);
            if (!Files.exists(path)) {
                Files.createDirectories(path); // Crée le répertoire s'il n'existe pas
            }

            Files.copy(file.getInputStream(), destinationPath); // Copie le fichier dans le répertoire de destination
            System.out.println("File saved... " + destinationPath.toString());
            return fileName;
        }else
            return DEFAULT_FILE;
    }

    public static byte[] downloadImageFromFileSystem(String filename) throws IOException {
        File f = new File(FOLDER_PATH+"/"+filename);
        if(!f.exists()) {
            System.out.println("not found "+filename);
             f = new File(FOLDER_PATH+"/"+DEFAULT_FILE);
        }
        return Files.readAllBytes(f.toPath());
    }


    public static String downloadAndSaveImageFromUrl(String imageUrl) throws IOException {
        System.out.println("1:"+imageUrl);
        if(imageUrl!=null && !imageUrl.isEmpty() && !imageUrl.equals("null")) {
            System.out.println("11_in_:"+imageUrl);
            try {
                //imageUrl = perfectURL(imageUrl);
                byte[] imageBytes = restTemplate.getForObject(URLDecoder.decode(imageUrl, "UTF-8"), byte[].class);

                System.out.println("2: " + imageBytes.length);

                String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                System.out.println("3: " + fileName);
                String filePath = FOLDER_PATH +"/"+ fileName;
                System.out.println("4: " + filePath);

                FileOutputStream fos = new FileOutputStream(filePath);
                fos.write(imageBytes);
                fos.close();

                return fileName;
            } catch (Exception e) {
                System.out.println("Error downloading image: " + e.getMessage()+" =>"+imageUrl);
                return DEFAULT_FILE;
            }

           /* RestTemplate restTemplate = new RestTemplate();
            System.out.println("12_in_:"+imageUrl);
            byte[] imageBytes = restTemplate.getForObject(imageUrl, byte[].class);
            System.out.println("2:"+imageBytes.length);
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            System.out.println("3:"+fileName);
            String filePath = FOLDER_PATH +"/"+ fileName;
            System.out.println("4:"+filePath);
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(imageBytes);
            fos.close();
            return fileName;*/
        }else{
            System.out.println("11111: "+imageUrl+"  is in else case");
            return FileManagerService.DEFAULT_FILE;
        }
    }

    public static String perfectURL(String url){
        return url.replaceAll("\"", "");
    }

    public static String removeQuote(String text){
        return text.replaceAll("\"", "");
    }
}
