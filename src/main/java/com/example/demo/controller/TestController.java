package com.example.demo.controller;

import com.example.demo.Object.Sentence;
import com.example.demo.Object.word;
import com.example.demo.service.ChatService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Collections;
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
        String wordQuestion = "어근 1개를 정한다음 1개에서 파생되는 단어3가지를 JSON형식으로 출력해줘" +
                "JSON키워드는 root, Id, Word, Meaning이고 root에는 단어의 어근을,  ID는 단어의 인덱스번호," +
                "Word는 너가 만든 영단어를, Meaning은 그 영단어의 뜻을 한글로 출력해줘" +
                "무조건 밑의 ex대로 JSON형식으로 출력하고 그 주위엔 1개의 문자도 출력해선 안되고 예시의 단어는 항상 다르게 해줘" +
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

        String wordJsonString = chatService.getChatResponse(wordQuestion);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<word> words = objectMapper.readValue(wordJsonString, new TypeReference<List<word>>() {});
            model.addAttribute("words", words);
            model.addAttribute("wordsJson", wordJsonString);  // JSON 문자열도 모델에 추가

            // 문장 생성을 위한 질문
            String sentenceQuestion = "영어 문장을 3개 만들어줘 이때 밑의 조건을 따라 문장을 적고 그 밑에 문장 전체의 뜻을 적어줘" +
                    "JSON키워드는 Id, Sentence, Meaning이고 ID는 문장의 인덱스번호," +
                    "Sentence는 너가 만든 영어 문장을," +
                    "Meaning은 영어 문장 전체의 뜻을 한글로 출력해줘." +
                    "무조건 밑의 ex대로 JSON형식으로 출력하고 예시의 문장은 항상 다르게 해줘" +
                    "ex) [\n" +
                    "  {\n" +
                    "    \"id\": 1,\n" +
                    "    \"sentence\": \"The actor performed brilliantly on stage last night.\",\n" +
                    "    \"meaning\": \"배우가 어제 밤 무대에서 뛰어난 연기를 했습니다.\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"id\": 2,\n" +
                    "    \"sentence\": \"We need to activate the emergency alarm immediately.\",\n" +
                    "    \"meaning\": \"즉시 비상 경보를 작동시켜야 해요.\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"id\": 3,\n" +
                    "    \"sentence\": \"She always reacts quickly in emergency situations.\",\n" +
                    "    \"meaning\": \"그녀는 비상 상황에서 항상 빨리 반응해요.\"\n" +
                    "  }\n" +
                    "]";

            String sentenceJsonString = chatService.getChatResponse(sentenceQuestion);
            List<Sentence> sentences = objectMapper.readValue(sentenceJsonString, new TypeReference<List<Sentence>>() {});
            model.addAttribute("sentences", sentences);
            model.addAttribute("sentencesJson", sentenceJsonString);  // JSON 문자열도 모델에 추가
        } catch (IOException e) {
            log.error("An error occurred while processing the request: {}", e.getMessage());
            return "errorPage";
        }

        return "wordList";
    }

    @GetMapping("/Wordpage_2")
    public String wordQuiz(@RequestParam("wordsJson") String wordsJson, Model model) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<word> words = objectMapper.readValue(wordsJson, new TypeReference<List<word>>() {});
            Collections.shuffle(words);  // 단어 목록을 셔플
            model.addAttribute("words", words);
            model.addAttribute("wordsJson", wordsJson);  // JSON 문자열도 모델에 추가
        } catch (IOException e) {
            log.error("An error occurred while processing the request: {}", e.getMessage());
            return "errorPage";
        }
        return "wordQuiz";
    }

    @PostMapping("/checkAnswers")
    public String checkAnswers(@RequestParam List<String> userAnswers, @RequestParam("wordsJson") String wordsJson, Model model) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<word> words = objectMapper.readValue(wordsJson, new TypeReference<List<word>>() {});
            int score = 0;
            for (int i = 0; i < words.size(); i++) {
                // 사용자가 제출한 답변과 단어의 뜻을 비교합니다.
                if (words.get(i).getMeaning().equals(userAnswers.get(i))) {
                    score++;
                }
            }
            // 점수를 모델에 추가합니다.
            model.addAttribute("score", score);
            // 모든 단어를 모델에 추가합니다.
            model.addAttribute("words", words);
        } catch (IOException e) {
            log.error("An error occurred while processing the request: {}", e.getMessage());
            return "errorPage";
        }
        // 결과 페이지로 이동합니다.
        return "wordQuizResult";
    }

}