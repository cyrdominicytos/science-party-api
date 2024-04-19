package fr.istic.science.test;

import fr.istic.science.service.DownloadService;
import fr.istic.science.service.FileManagerService;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TestImport {

    public static void main(String[] args) {

       /* String dateString = "mercredi 11 octobre 2017";
        LocalDateTime localDateTime = convertStringToLocalDateTime(dateString);
        System.out.println("LocalDateTime: " + localDateTime);*/

       /* String dateTimeString = "2017-10-11T14:00:00+02:00-2017-10-11T16:00:00+02:00";
        LocalDateTime[] dateTimes = extractDateTimes(dateTimeString);

        System.out.println("Start LocalDateTime: " + dateTimes[0]);
        System.out.println("End LocalDateTime: " + dateTimes[1]);*/

        testImageUploadOne();

    }

    private  void testImageUpload(){
        String url_zip = "https://www.data.gouv.fr/fr/datasets/r/c8f3b061-92ac-4640-bea8-20bee0f0879c";
        String url_json = "https://www.data.gouv.fr/fr/datasets/r/76cf3202-3357-4379-b47e-dd24bc72688a";
        String path = "src/data/";
        String filename = "fr-esr-fete-de-la-science-17";
        DownloadService downloadService = new DownloadService();
        //downloadService.downloadAndExtractZip(url_json,filename, path);
        downloadService.loadOpenData(url_json, false);
    }
    private  static void testImageUploadOne(){
        String url = "https://cibul.s3.amazonaws.com/evfevent_travaux-de-chercheurs-la-science-en-realite-virtuelle_26596.jpg";

        try {
           String name =  FileManagerService.downloadAndSaveImageFromUrl(url);
            System.out.println("Success "+name);
        } catch (IOException e) {
            System.out.println("Error =>"+e.getMessage());
           // throw new RuntimeException(e);
        }
    }

    private void testImagesPath(){
        File f = new File("src/data/event_default.avif");
        if(!f.exists()) {
            System.out.println("Fichier non trouvé : "+f.getAbsolutePath());

        }else  System.out.println("Fichier trouvé : "+f.getAbsolutePath());
    }

    public static LocalDateTime convertStringToLocalDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy", Locale.FRENCH);
        return LocalDateTime.parse(dateString, formatter);
    }

    public static LocalDateTime[] extractDateTimes(String dateTimeString) {
        String[] dateTimeParts = dateTimeString.split("-");
        String begin = dateTimeParts[0]+"-"+ dateTimeParts[1]+"-"+ dateTimeParts[2];
        String end = dateTimeParts[3]+"-"+ dateTimeParts[4]+"-"+ dateTimeParts[5];
        LocalDateTime startDateTime = LocalDateTime.parse(begin, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        LocalDateTime endDateTime = LocalDateTime.parse(end, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        return new LocalDateTime[] {startDateTime, endDateTime};
    }
}
