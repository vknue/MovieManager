/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package valonnue.javaproject.dao;

import java.util.List;
import valonnue.javaproject.Models.Movie;

/**
 *
 * @author valon
 */
public interface MovieRepository {
    int createMovie(Movie movie) throws Exception;
    List<Movie> selectMovies() throws Exception;
    Movie selectMovie(int id) throws Exception;
    void updateMovie(int id, Movie movie) throws Exception;
    void deleteMovie(int id) throws Exception;
}
