package fr.istic.science.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int rate;
    private int totalRating;
    private LocalDateTime dateCreation;
    private String place;
    private String address;
    private LocalDateTime dateInit;
    private LocalDateTime dateFin;
    private String fullIndicator;
    private boolean isPublished;
    private String phone;
    private String email;
    private String facebookUrl;
    private String instagramUrl;
    private String image;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "parcour_id")
    private Parcour parcour;

    @ManyToOne
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @ManyToMany
    private List<Tag> tags;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getTotalRatings() {
        return totalRating;
    }

    public void setTotalRatings(int totalRating) {
        this.totalRating = totalRating;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getDateInit() {
        return dateInit;
    }

    public void setDateInit(LocalDateTime dateInit) {
        this.dateInit = dateInit;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public String getFullIndicator() {
        return fullIndicator;
    }

    public void setFullIndicator(String fullIndicator) {
        this.fullIndicator = fullIndicator;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Parcour getParcour() {
        return parcour;
    }

    public void setParcour(Parcour parcour) {
        this.parcour = parcour;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getters and setters
    // Constructors
    // Other fields and methods as needed
}

