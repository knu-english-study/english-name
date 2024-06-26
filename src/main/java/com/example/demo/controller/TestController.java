package com.example.demo.controller;

import com.example.demo.DTO.UserDTO;
import com.example.demo.DTO.WordDTO;
import com.example.demo.Object.Sentence;
import com.example.demo.Object.Word;
import com.example.demo.service.ChatService;
import com.example.demo.service.UserService;
import com.example.demo.service.UserWordService;
import com.example.demo.service.WordService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
@Slf4j
public class TestController {
    private final ChatService chatService;

    public TestController(ChatService chatService) {
        this.chatService = chatService;
    }

    @Autowired
    private WordService wordService;
    @Autowired
    private UserWordService userWordService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(UserDTO userDTO) {
        userService.registerUser(userDTO);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        if (userService.authenticateUser(username, password)) {
            // 인증 성공 시 세션에 사용자 정보 저장
            session.setAttribute("username", username);
            return "redirect:/Wordpage_1";
        } else {
            // 인증 실패 시 로그인 페이지로 다시 이동
            return "redirect:/login?error";
        }
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
            List<Word> words = objectMapper.readValue(wordJsonString, new TypeReference<List<Word>>() {
            });

            List<WordDTO> wordDTOs = objectMapper.readValue(wordJsonString, new TypeReference<List<WordDTO>>() {});
            for (WordDTO wordDTO : wordDTOs) {
                wordService.saveWord(wordDTO); // 이미 정의된 saveWord 메소드 호출
            }


            model.addAttribute("words", words);
            model.addAttribute("wordsJson", wordJsonString);  // JSON 문자열도 모델에 추가

            // 문장 생성을 위한 질문
            String sentenceQuestion = words + "위의 데이터에 있는 단어를 사용해서 영어 문장을 3개 만들어줘 이때 밑의 조건을 따라 문장을 적고 그 밑에 문장 전체의 뜻을 적어줘" +
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
            List<Sentence> sentences = objectMapper.readValue(sentenceJsonString, new TypeReference<List<Sentence>>() {
            });
            model.addAttribute("sentences", sentences);
            model.addAttribute("sentencesJson", sentenceJsonString);
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
            List<Word> words = objectMapper.readValue(wordsJson, new TypeReference<List<Word>>() {
            });
            Collections.shuffle(words);  // 단어 목록을 셔플
            model.addAttribute("words", words);
            model.addAttribute("wordsJson", wordsJson);  // JSON 문자열도 모델에 추가
        } catch (IOException e) {
            log.error("An error occurred while processing the request: {}", e.getMessage());
            return "errorPage";
        }
        return "wordQuiz";
    }

    @GetMapping("/Sentencepage_2")
    public String sentenceQuiz(@RequestParam("sentencesJson") String sentencesJson, Model model) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // JSON 문자열을 Sentence 객체 리스트로 변환
            List<Sentence> sentences = objectMapper.readValue(sentencesJson, new TypeReference<List<Sentence>>() {});
            String quizQuestion = sentences.toString() + " 위 문장들을 기반으로 3문제를 만들어줘 이때 문제의 포맷은 다음과 같고 밑의 예시는 다른 예제로 대체해.\n" +
                    "JSON키워드는 id, question, sentence, blank, onym이고 id는 문장의 인덱스번호,\n" +
                    "question은 단어 맞추기 문제와 오지선다 문제를 번갈아가며 출력해줘.\n" +
                    "sentence는 위에 만든 문장을 가지고 와줘.\n" +
                    "blank는 위에서 가져온 단어를 빈칸으로 설정해줘.\n" +
                    "onym은 가져온 단어와 비슷한 유의어와 반의어를 4개 가져와서 문제의 정답과 함께 5문제로 설정하되 문제의 정답이 첫번째만 나오는게 아니라 순서를 무작위로 설정해줘.\n" +
                    "무조건 밑의 ex대로 JSON형식으로 출력하고 단어 맞추기 문제와 오지선다 문제를 번갈아가면서 출력 해줘.\n" +
                    "ex) [\n" +
                    "  {\n" +
                    "    \"id\": 1,\n" +
                    "    \"question\": \"단어 맞추기 문제\",\n" +
                    "    \"sentence\": \"The king issued an _____ demanding higher taxes from his subjects.\",\n" +
                    "    \"blank\": \"edict\",\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"id\": 2,\n" +
                    "    \"question\": \"오지선다 문제\",\n" +
                    "    \"sentence\": \"The priest offered a __________ to the newlyweds for a happy marriage.\",\n" +
                    "    \"blank\": \"benediction\",\n" +
                    "    \"onym\": [\"benediction\", \"blessing\", \"prayer\", \"curse\", \"wish\"]\n" +
                    "  }\n" +
                    "]";

            // ChatGPT의 응답을 받음
            String quizJsonString = chatService.getChatResponse(quizQuestion);

            // 응답이 JSON 형식인지 확인하고 처리
            if (quizJsonString.trim().startsWith("{") || quizJsonString.trim().startsWith("[")) {
                // JSON 형식으로 파싱
                List<Object> quiz = objectMapper.readValue(quizJsonString, new TypeReference<List<Object>>() {});
                model.addAttribute("quiz", quiz);
                model.addAttribute("sentences", sentences);
                model.addAttribute("sentencesJson", sentencesJson);  // JSON 문자열도 모델에 추가
            } else {
                log.error("Invalid JSON response received from ChatGPT: {}", quizJsonString);
                return "errorPage";
            }
        } catch (IOException e) {
            log.error("An error occurred while processing the request: {}", e.getMessage());
            return "errorPage";
        }
        return "sentenceQuiz";
    }

    @GetMapping("/checkSentenceAnswers")
    public String checkSentenceAnswers(@RequestParam List<String> userAnswers, @RequestParam("sentencesJson") String sentencesJson, Model model) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Sentence> sentences = objectMapper.readValue(sentencesJson, new TypeReference<List<Sentence>>() {});
            String quizQuestionAnswers = sentences.toString() + " 위에서 만든 문장의 정답과 해설을 나타내는 방식으로 단어 맞추기 문제는 정답을 알려주고 오지선다 문제는 정답을 굵게 표시하고 해설을 추가해줘.\n" +
                    "JSON키워드는 id, question, sentence, blank, onym, answer, commentary이고 id는 문장의 인덱스번호,\n" +
                    "question은 단어 맞추기 문제와 오지선다 문제를 번갈아가며 출력해줘.\n" +
                    "sentence는 위에 만든 문장을 가지고 오되 문제의 빈칸을 올바르게 설정해줘.\n" +
                    "blank는 위에서 가져온 단어를 빈칸으로 설정해줘.\n" +
                    "onym은 가져온 단어와 비슷한 유의어와 반의어를 4개 가져와서 문제의 정답과 함께 5문제로 설정하되 문제의 정답이 첫번째만 나오는게 아니라 순서를 무작위로 설정해줘.\n" +
                    "answers는 문제의 정답을 표시하되 빈칸 문제와 오지선다의 정답을 굵게 표시해줘.\n" +
                    "commentary는 빈칸 문제는 null 값이기에 출력하지 말고 오지선다 문제의 해설의 포맷은 '문장을 해석하면 ~~ 인데 이때 빈칸에  ~~의미의 단어가 들어가야 함으로 ~가 답입니다'라고 설정해줘. \n" +
                    "무조건 밑의 ex대로 JSON형식으로 출력하고 단어 맞추기 문제와 오지선다 문제를 번갈아가면서 출력 해줘.\n" +
                    "ex) [\n" +
                    "  {\n" +
                    "    \"id\": 1,\n" +
                    "    \"question\": \"단어 맞추기 문제\",\n" +
                    "    \"sentence\": \"The king issued an _____ demanding higher taxes from his subjects.\n" +
                    "    \"blank\": \"edict\",\n" +
                    "    \"answer\": \"edict\",\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"id\": 2,\n" +
                    "    \"question\": \"오지선다 문제\",\n" +
                    "    \"sentence\": \"The king issued an _____ demanding higher taxes from his subjects.\n" +
                    "    \"blank\": \"benediction\",\n" +
                    "    \"onym\": [\"edict\", \"유의어1\", \"반의어2\", \"유의어3\", \"유의어4\"],\n" +
                    "    \"answer\": \"edict\",\n" +
                    "    \"commentary\": \"문장을 해석하면 \"왕은 그의 백성들에게 더 높은 세금을 요구하는 _____을 발표했다\" 인데 이때 빈칸에 '공식 명령'의 의미의 단어가 들어가야 함으로 edict가 답입니다.”\"\n" +
                    "  }\n" +
                    "]";

            String quizJsonString = chatService.getChatResponse(quizQuestionAnswers);

            if (quizJsonString.trim().startsWith("[") && quizJsonString.trim().endsWith("]")) {
                List<Map<String, Object>> quiz = objectMapper.readValue(quizJsonString, new TypeReference<List<Map<String, Object>>>() {});
                int score = 0;

                for (int i = 0; i < quiz.size(); i++) {
                    Map<String, Object> quizItem = quiz.get(i);
                    String userAnswer = userAnswers.get(i);

                    if ("단어 맞추기 문제".equals(quizItem.get("question"))) {
                        String correctAnswer = (String) quizItem.get("blank");
                        if (correctAnswer.equalsIgnoreCase(userAnswer)) {
                            score++;
                        }
                    } else if ("오지선다 문제".equals(quizItem.get("question"))) {
                        List<String> onym = (List<String>) quizItem.get("onym");
                    }
                }

                model.addAttribute("score", score);
                model.addAttribute("quiz", quiz);
            } else {
                log.error("Invalid JSON response received from ChatGPT: {}", quizJsonString);
                return "errorPage";
            }
        } catch (IOException e) {
            log.error("An error occurred while processing the request: {}", e.getMessage());
            return "errorPage";
        }
        return "sentenceQuizResult";
    }



    @GetMapping("/checkWordAnswers")
    public String checkWordAnswers(@RequestParam List<String> userAnswers, @RequestParam("wordsJson") String wordsJson, Model model) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Word> words = objectMapper.readValue(wordsJson, new TypeReference<List<Word>>() {
            });
            int score = 0;
            for (int i = 0; i < words.size(); i++) {
                if (words.get(i).getMeaning().equals(userAnswers.get(i))) {
                    score++;
                }
            }
            model.addAttribute("score", score);
            model.addAttribute("words", words);
        } catch (IOException e) {
            log.error("An error occurred while processing the request: {}", e.getMessage());
            return "errorPage";
        }
        return "wordQuizResult";
    }
}
