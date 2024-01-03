/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package valonnue.javaproject.Models;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author valon
 */
@XmlRootElement(name = "movie")
@XmlAccessorType(XmlAccessType.FIELD)
public class Movie {

    @XmlTransient
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public Movie(int id, String title, String description, String bannerPath, String link, Date publishedDate, Date showingDate, Set<Actor> Actors, Set<Director> Directors) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.bannerPath = bannerPath;
        this.link = link;
        this.publishedDate = publishedDate;
        this.showingDate = showingDate;
        this.Actors = Actors;
        this.Directors = Directors;
    }

    public Movie(int id, String title, String description, String bannerPath, String link, Date publishedDate, Date showingDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.bannerPath = bannerPath;
        this.link = link;
        this.publishedDate = publishedDate;
        this.showingDate = showingDate;
    }

    public Movie(int id) {
        this.id = id;
    }

    private int id;
    private String title;
    private String description;
    private String bannerPath;
    private String link;
    private Date publishedDate;
    private Date showingDate;
    @XmlElementWrapper(name = "actors")
    @XmlElement(name="actor")
    private Set<Actor> Actors;
    @XmlElementWrapper(name = "directors")
    @XmlElement(name = "director")
    private Set<Director> Directors;

    public Set<Actor> getActors() {
        if(Actors==null) return new HashSet<Actor>();
        return Actors;
    }
    
    public void addActor(Actor actor){
        Actors.add(actor);
    }
    
    public void addDirector(Director director){
        Directors.add(director);
    }
    
    public void removeActor(Actor actor){
        Actors.remove(actor);
    }
    
    public void removeDirector(Director director){
        Directors.remove(director);
    }

    public Set<Director> getDirectors() {
        if(Directors==null) return new HashSet<Director>();
        return Directors;
    }

    public void setActors(Set<Actor> Actors) {
        this.Actors = Actors;
    }

    @Override
    public String toString() {
        return "Movie{" + "id=" + id + ", title=" + title + ", description=" + description + ", bannerPath=" + bannerPath + ", link=" + link + ", publishedDate=" + publishedDate + ", showingDate=" + showingDate + ", Actors=" + Actors + ", Directors=" + Directors + '}';
    }

    public void setDirectors(Set<Director> Directors) {
        this.Directors = Directors;
    }

    public Movie() {
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBannerPath() {
        return bannerPath;
    }

    public String getLink() {
        return link;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public Date getShowingDate() {
        return showingDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBannerPath(String bannerPath) {
        this.bannerPath = bannerPath;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void setShowingDate(Date showingDate) {
        this.showingDate = showingDate;
    }

}
