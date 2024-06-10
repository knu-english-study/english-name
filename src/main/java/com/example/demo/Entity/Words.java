package com.example.demo.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Words {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String root;
    private Long id;
    private String word;
    private String meaning;
}
