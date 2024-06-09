package com.example.demo.DAO;

import com.example.demo.Entity.Users;

public interface UserDAOInterface {
    public Users save(Users user);
    public Users findByUsername(String username);
}
