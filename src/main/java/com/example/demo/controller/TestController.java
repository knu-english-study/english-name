package com.example.demo.controller;


import com.example.demo.Object.word;
import com.example.demo.service.ChatService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
public class TestController {
    private final ChatService chatService;

    public TestController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/Wordpage_1")
    public String test(Model model) {
        String question = "어근 1개를 정한다음 1개에서 파생되는 단어3가지를 JSON형식으로 출력해줘" +
                "JSON키워드는 root, Id, Word, Meaning이고 root에는 단어의 어근을,  ID는 단어의 인덱스번호," +
                "Word는 너가 만든 영단어를, Meaning은 그 영단어의 뜻을 한글로 출력해줘" +
                "무조건 밑의 ex대로 JSON형식으로 출력하고 그 주위엔 1개의 문자도 출력해선 안돼고 예시의 단어는 항상 다르게 해줘" +
                "ex) [\n" +
                "  {\n" +
                "    \"root\": \"root1\",\n" +
                "    \"id\": 1,\n" +
                "    \"word\": \"derive\",\n" +
                "    \"meaning\": \"비롯되다\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"root\": \"root1\",\n" +
                "    \"id\": 2,\n" +
                "    \"word\": \"derivation\",\n" +
                "    \"meaning\": \"파생\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"root\": \"root1\",\n" +
                "    \"id\": 3,\n" +
                "    \"word\": \"derivative\",\n" +
                "    \"meaning\": \"유도물\"\n" +
                "  }\n" +
                "]";


        String jsonString = chatService.getChatResponse(question);


        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<word> words = objectMapper.readValue(jsonString, new TypeReference<List<word>>(){});
            model.addAttribute("words", words);
        } catch (IOException e) {
            log.error("An error occurred while processing the request: {}", e.getMessage());
            return "errorPage";
        }

        return "wordList";
    }
}

