package com.example.demo.Object;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @Data @AllArgsConstructor
@ToString
public class word {
    private String root;
    private String id;
    private String word;
    private String meaning;
}
