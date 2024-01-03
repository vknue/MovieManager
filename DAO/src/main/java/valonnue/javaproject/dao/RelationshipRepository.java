/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package valonnue.javaproject.dao;

import java.util.Set;
import valonnue.javaproject.Models.Actor;
import valonnue.javaproject.Models.Director;

/**
 *
 * @author valon
 */
public interface RelationshipRepository {
    void addActorToMovie(int actorID, int movieID) throws Exception;
    void addDirectorToMovie(int directorID, int movieID) throws Exception;
    Set<Actor> selectActorsForMovie(int movieID) throws Exception;
    Set<Director> selectDirectorsForMovie(int movieID) throws Exception;
    void removeActorFromMovie(int actorID, int movieID) throws Exception;
    void removeDirectorFromMovie(int directorID, int movieID) throws Exception;
}
