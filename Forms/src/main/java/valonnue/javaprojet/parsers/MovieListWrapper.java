/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package valonnue.javaprojet.parsers;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import valonnue.javaproject.Models.Movie;

/**
 *
 * @author valon
 */
@XmlRootElement(name = "movies")
@XmlAccessorType(XmlAccessType.FIELD)
public class MovieListWrapper {

    @XmlElement(name = "movie")
    private List<Movie> movies;

    public MovieListWrapper() {
        movies = new ArrayList<>();
    }
    

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
