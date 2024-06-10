package com.example.demo.Repository;

import com.example.demo.Entity.Word;
import com.example.demo.Entity.Words;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository_2 extends JpaRepository<Word, Integer> {

}
