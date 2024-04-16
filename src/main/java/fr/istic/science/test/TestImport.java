package fr.istic.science.test;

import fr.istic.science.service.DownloadService;

import java.io.File;
import java.io.IOException;

public class TestImport {

    public static void main(String[] args) {
        /*String url_zip = "https://www.data.gouv.fr/fr/datasets/r/c8f3b061-92ac-4640-bea8-20bee0f0879c";
        String url_json = "https://www.data.gouv.fr/fr/datasets/r/76cf3202-3357-4379-b47e-dd24bc72688a";
        String path = "src/data/";
        String filename = "fr-esr-fete-de-la-science-17";
        DownloadService downloadService = new DownloadService();
        //downloadService.downloadAndExtractZip(url_json,filename, path);
        downloadService.loadOpenData(url_json, false);*/

        File f = new File("src/data/event_default.avif");
        if(!f.exists()) {
            System.out.println("Fichier non trouvé : "+f.getAbsolutePath());

        }else  System.out.println("Fichier trouvé : "+f.getAbsolutePath());
    }
}
