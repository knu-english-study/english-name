package com.example.demo.Object;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Data
@AllArgsConstructor
@ToString
public class Sentence {
    private int id;
    private String sentence;
    private String meaning;
    private String answer;
}