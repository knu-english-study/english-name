package com.example.demo.DAO;
import com.example.demo.Entity.Users;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDAO implements UserDAOInterface {

    @Autowired
    private UserRepository userRepository;

    public Users save(Users user) {
        return userRepository.save(user);
    }

    public Users findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
