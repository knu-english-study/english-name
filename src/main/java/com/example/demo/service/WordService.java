package com.example.demo.service;

import com.example.demo.DAO.WordDAO;
import com.example.demo.DTO.WordDTO;
import com.example.demo.Entity.Words;
import com.example.demo.Object.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WordService implements WordServiceInterface {

    @Autowired
    private WordDAO wordDAO;

    public void saveWord(WordDTO wordDTO) {
        Words word = new Words();
        word.setWord(wordDTO.getWord());
        word.setMeaning(wordDTO.getMeaning());
        wordDAO.save(word);
    }
}

