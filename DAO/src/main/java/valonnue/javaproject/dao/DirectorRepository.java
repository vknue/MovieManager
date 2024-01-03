/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package valonnue.javaproject.dao;

import java.util.Set;
import valonnue.javaproject.Models.Director;

/**
 *
 * @author valon
 */
public interface DirectorRepository {
    int createDirector(Director director) throws Exception;
    Set<Director> selectDirectors() throws Exception;
    Director selectDirector(int id) throws Exception;
}
