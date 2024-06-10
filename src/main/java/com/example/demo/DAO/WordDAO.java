package com.example.demo.DAO;


import com.example.demo.Entity.Words;
import com.example.demo.Repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WordDAO implements WordDAOInterface{

    @Autowired
    private WordRepository wordRepository;

    public Words save(Words word){
        return wordRepository.save(word);
    }
}