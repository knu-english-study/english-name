package com.example.demo.Repository;

import com.example.demo.Entity.Words;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<Words, Long> {

}
