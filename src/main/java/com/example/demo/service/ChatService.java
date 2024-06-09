package com.example.demo.service;

import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class ChatService{

    @Value("${openai.api.key}")
    private String openaiApiKey;

    public String getChatResponse(String prompt) {
        OpenAiService openAiService = new OpenAiService(openaiApiKey);
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .model("gpt-4o")  // 사용할 모델 지정
                .messages(Collections.singletonList(new ChatMessage("user", prompt)))
                .maxTokens(1350)
                .build();

        ChatCompletionResult completionResult = openAiService.createChatCompletion(completionRequest);
        return completionResult.getChoices().get(0).getMessage().getContent().trim();
    }
}