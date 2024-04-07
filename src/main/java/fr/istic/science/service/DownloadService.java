package fr.istic.science.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.istic.science.model.Event;
import fr.istic.science.model.Party;
import fr.istic.science.model.User;
import fr.istic.science.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class DownloadService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PartyRepository partyRepository;
    private final BlockingQueue<JsonNode> outputQueue = new LinkedBlockingQueue<>();
    final static int NB_OF_THREADS = 4;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private int nb_of_readed = 0;
    private final List<List<Event>> events = new ArrayList<>();
    private User user;
    private Party party;

    private void initDatas() {
        try {
            Optional<User> defautUser = userRepository.findById(1L);
            if(defautUser.isPresent()){
                System.out.println("User found !");
                user = defautUser.get();
            }else{
                User u = new User();
                u.setSurname("System");
                u.setPseudo("System");
                u.setPassword("System");
                u.setEmail("system@gmail.com");
                user = userRepository.save(u);
            }
        } catch (Exception e) {
            System.out.println("initDatas, Exception=>"+e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Async
    public CompletableFuture<Void> saveEvenstBatch(List<Event> events) {
        // Insérer les objets Event dans la base de données
        eventRepository.saveAll(events);
        return CompletableFuture.completedFuture(null);
    }

    public void processFileInDatabase() throws IOException {
        System.out.println("In  processFileInDatabase: ");

        // Découper le contenu du fichier en plusieurs et constitué en paralèlle des tableau des Events
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for(int i = 0; i < NB_OF_THREADS; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(this::divideIntoBatches, executor);
            futures.add(future);
        }

        // Attendre la terminaison de toutes les threads construisant les tableaux d'évènement
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> {
                    System.out.println("=========   Constitution des objets events en DB terminée ! ===========");
                    // Une fois toutes les Thread terminées, lancer en parallèle de manière asynchrone l'enregristrement des events en BD
                    List<CompletableFuture<Void>> futures2 = new ArrayList<>();
                    for (List<Event> batch : events) {
                        System.out.println("===> Lot de : "+batch.size());
                        CompletableFuture<Void> future = saveEvenstBatch(batch);
                        futures.add(future);
                    }
                    // Attendre la fin de toutes les insertions en BD
                    CompletableFuture.allOf(futures2.toArray(new CompletableFuture[0])).join();
                    System.out.println("Insertion en DB terminée !");
                })
                .join();


        // Arrêter l'executor après avoir terminé
        executor.shutdown();
        System.out.println("executor terminer...");
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            System.out.println("Les clusters sont fermés !");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private void divideIntoBatches() {
        int size = outputQueue.size();
        List<Event> local_events = new ArrayList<>();
        System.out.println("In  divideIntoBatches: "+size);
        while(nb_of_readed < size) {
            try {
                System.out.println("Before JsonNode");
                JsonNode val = outputQueue.take();
                nb_of_readed++;
                System.out.println("Before  Event e = new Event()");
                Event e = new Event();
                System.out.println("line==== 1");
                e.setName(String.valueOf(val.get("titre_fr")));
                System.out.println("line==== 2");
                e.setDescription(String.valueOf(val.get("description_fr")));
                System.out.println("line==== 3");
                e.setFreeEvent(true);
                e.setPublished(true);
                System.out.println("line==== 4");
                e.setAddress(String.valueOf(val.get("adresse")));
                System.out.println("line==== 5");
                e.setPhone(String.valueOf(val.get("telephone_du_lieu")));
                System.out.println("line==== 6");
                e.setLatitude(String.valueOf(val.get("geolocalisation").get("lat")));
                e.setLongitude(String.valueOf(val.get("geolocalisation").get("lon")));

                //System.out.println("line==== 7 "+convertToLocalDateTime(String.valueOf(val.get("date_debut"))));
                //e.setDateInit(convertToLocalDateTime(String.valueOf(val.get("date_debut"))));
                System.out.println("line==== 8");
                //e.setDateEnd(convertToLocalDateTime(String.valueOf(val.get("date_fin"))));

                //e.setTheme(); thematiques []
                //e.setTags(); mots_cles_fr[]
                //e.setImageUrl(); image_source
                System.out.println("Before  relations 1");
                e.setUser(user);
                System.out.println("Before  relations 2");
                e.setParty(party);
                System.out.println("Before  relations 3");
                local_events.add(e);
                System.out.println("Add =>"+e.getName()+" Count=>" +nb_of_readed);
            } catch (InterruptedException e) {
                System.out.println("Erreur divideIntoBatches : " + e.getMessage());
                e.printStackTrace();
            }
        }

        if(!local_events.isEmpty())
            events.add(local_events);

    }

    private LocalDateTime convertToLocalDateTime(String text){
        // Définir un formateur pour parser la chaîne
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy");
        // Parser la chaîne en un objet LocalDate
        LocalDate date = LocalDate.parse(text, formatter);
        return date.atStartOfDay();
    }


    public void loadOpenData(String jsonUrl, boolean update_last) {
        try {
            // Télécharger le contenu JSON depuis l'URL
            RestTemplate restTemplate = new RestTemplate();
            String jsonContent = restTemplate.getForObject(jsonUrl, String.class);

            // Créer un objet ObjectMapper de Jackson pour lire le contenu JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonContent);

            // Vérifier si le contenu JSON est un tableau d'objets
            if (rootNode.isArray()) {
                initDatas();
                // Parcourir chaque objet du tableau
                System.out.println("Le fichier téléchargé contient : " + rootNode.size());
                Collections.addAll(outputQueue, objectMapper.treeToValue(rootNode, JsonNode[].class));

                if(update_last){
                    Optional<Party> loacl_party = partyRepository.findById(1L);
                    loacl_party.ifPresent(value -> party = value);
                }else{
                    party = new Party();
                    party.setTagName("#FDS"+ Year.now().getValue());
                    for (JsonNode node : rootNode){
                        for(JsonNode dateString : node.get("dates")){
                            LocalDate date = LocalDate.parse(dateString.asText());
                            LocalDateTime localDateTime = date.atStartOfDay();
                            party.setDateInit(localDateTime);
                            party.setDateEnd(localDateTime);
                            break;
                        }
                        break;
                    }
                    System.out.println("Before saving party");
                    party = partyRepository.save(party);
                    System.out.println("After saving party");
                }
                this.processFileInDatabase();
            } else {
                System.out.println("Le contenu JSON n'est pas un tableau d'objets.");
            }
        } catch (RestClientException | IOException e) {
            System.out.println("Une erreur s'est produite lors du téléchargement ou de la lecture du fichier JSON : " + e.getMessage());
        }
    }

















    public boolean downloadAndExtractZip(String zipUrl,String filename, String destinationFolder) throws IOException {
        // Télécharger le fichier ZIP
        RestTemplate restTemplate = new RestTemplate();
        byte[] zipContent = restTemplate.getForObject(zipUrl, byte[].class);

        //L'url est incorrecte ou de nouvelle données ne sont pas encore publiées sur OpenData
        if(zipContent==null){
            System.out.println("ZipFilURL not found !!");
            return false;
        }
        //System.out.println("ZipFilURL content : "+ Arrays.toString(zipContent));
        System.out.println("ZipFilURL loading...");
        // Créer un flux d'entrée pour le fichier ZIP
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(zipContent);
        ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream);

        // Créer le répertoire de destination s'il n'existe pas
        File destDir = new File(destinationFolder);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        System.out.println("Destination path "+destDir.getAbsolutePath());
        // Extraire les fichiers du ZIP
        ZipEntry entry;
        while ((entry = zipInputStream.getNextEntry()) != null) {
            String filePath = destinationFolder + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                extractFile(zipInputStream, filePath);
                System.out.println("Fichier extrait : " + entry.getName());
            } else {
                File dir = new File(filePath);
                dir.mkdirs();
                System.out.println("Dir extrait : " + entry.getName());
            }
            zipInputStream.closeEntry();
        }
        zipInputStream.close();

        //processZipFile(destinationFolder);
        return  true;
    }

    public boolean downloadAndExtractZip2(String zipUrl, String destinationFolder) {
        try {
            // Télécharger le fichier ZIP
            RestTemplate restTemplate = new RestTemplate();
            byte[] zipContent = restTemplate.getForObject(zipUrl, byte[].class);

            // Vérifier si le contenu téléchargé est vide
            if (zipContent == null || zipContent.length == 0) {
                System.out.println("Le fichier ZIP est vide ou n'a pas pu être téléchargé.");
                return false;
            }

            System.out.println("Téléchargement du fichier ZIP réussi.");

            // Créer un flux d'entrée pour le fichier ZIP
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(zipContent);
            ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream);

            // Créer le répertoire de destination s'il n'existe pas
            File destDir = new File(destinationFolder);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }

            System.out.println("Chemin de destination : " + destDir.getAbsolutePath());

            // Extraire les fichiers du ZIP
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String filePath = destinationFolder + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    extractFile(zipInputStream, filePath);
                    System.out.println("Fichier extrait : " + entry.getName());
                } else {
                    File dir = new File(filePath);
                    dir.mkdirs();
                    System.out.println("Dossier extrait : " + entry.getName());
                }
                zipInputStream.closeEntry();
            }
            zipInputStream.close();

            return true;
        } catch (RestClientException | IOException e) {
            System.out.println("Une erreur s'est produite lors du téléchargement ou de l'extraction du fichier ZIP : " + e.getMessage());
            return false;
        }
    }

    private void extractFile(ZipInputStream zipInputStream, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[1024];
        int read;
        while ((read = zipInputStream.read(bytesIn)) != -1) {
            System.out.println("Byte readed : "+read);
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}
