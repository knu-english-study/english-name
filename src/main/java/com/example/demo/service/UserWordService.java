package com.example.demo.service;


import com.example.demo.Entity.User;
import com.example.demo.Entity.UserCorrectWord;
import com.example.demo.Entity.UserIncorrectWord;
import com.example.demo.Entity.Word;
import com.example.demo.Repository.*;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class UserWordService {
    @Autowired
    private UserRepository_2 userRepository_2;

    @Autowired
    private WordRepository_2 wordRepository_2;

    @Autowired
    private UserCorrectWordRepository userCorrectWordRepository;

    @Autowired
    private UserIncorrectWordRepository userIncorrectWordRepository;

    @Transactional
    public void addCorrectWord(int userId, int wordId) {
        User user = userRepository_2.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Word word = wordRepository_2.findById(wordId).orElseThrow(() -> new RuntimeException("Word not found"));

        UserCorrectWord userCorrectWord = new UserCorrectWord();
        userCorrectWord.setUser(user);
        userCorrectWord.setWord(word);
        userCorrectWordRepository.save(userCorrectWord);
    }

    @Transactional
    public void addIncorrectWord(int userId, int wordId) {
        User user = userRepository_2.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Word word = wordRepository_2.findById(wordId).orElseThrow(() -> new RuntimeException("Word not found"));

        UserIncorrectWord userIncorrectWord = new UserIncorrectWord();
        userIncorrectWord.setUser(user);
        userIncorrectWord.setWord(word);
        userIncorrectWordRepository.save(userIncorrectWord);
    }
}
