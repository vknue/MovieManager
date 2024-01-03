/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package valonnue.javaproject.dao;

import java.util.Set;
import valonnue.javaproject.Models.Actor;

/**
 *
 * @author valon
 */
public interface ActorRepository {
    int createActor(Actor actor) throws Exception;
    Set<Actor> selectActors() throws Exception;
    Actor selectActor(int id) throws Exception;
}
