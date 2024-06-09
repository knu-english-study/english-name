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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
