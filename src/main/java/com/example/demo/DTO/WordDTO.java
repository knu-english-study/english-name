package com.example.demo.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WordDTO {
    private String root;
    private Long id;
    private String word;
    private String meaning;
}
