package com.example.demo.service;

import com.example.demo.Repository.UserRepository;
import com.example.demo.DAO.UserDAO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.Entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserServiceInterface{

    @Autowired
    private UserDAO userDAO;

    public Users registerUser(UserDTO userDTO) {
        Users user = new Users();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        return userDAO.save(user);
    }

    public Users findUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    @Autowired
    private UserRepository userRepository;

    public boolean authenticateUser(String username, String password) { //로그인 서비스
        Users user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return true; // 인증 성공
        }
        return false; // 인증 실패
    }
}