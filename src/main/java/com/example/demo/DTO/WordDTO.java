package com.example.demo.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WordDTO {
    private String root;
    private int id;
    private String word;
    private String meaning;
}
