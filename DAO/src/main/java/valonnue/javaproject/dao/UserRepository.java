/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package valonnue.javaproject.dao;

import java.util.List;
import java.util.Optional;
import valonnue.javaproject.Models.User;


public interface UserRepository {

    int createUser(User user) throws Exception;

    void updateUser(int id, User user) throws Exception;

    void deleteUser(int id) throws Exception;

    Optional<User> selectUser(int id) throws Exception;

    List<User> selectUsers() throws Exception;

}
