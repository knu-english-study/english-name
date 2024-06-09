package com.example.demo.service;
import com.example.demo.DTO.UserDTO;
import com.example.demo.Entity.Users;

public interface UserServiceInterface {

    Users registerUser(UserDTO userDTO);

    Users findUserByUsername(String username);

    boolean authenticateUser(String username, String password);
}

