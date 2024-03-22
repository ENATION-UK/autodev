package com.enation.autodev.impl;

import com.enation.autodev.Settings;
import com.enation.autodev.WorkFlowContext;
import com.enation.autodev.WorkLog;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
public class AIWorker {

    protected ChatLanguageModel model;

    @Autowired
    protected WorkLog workLog;


    public AIWorker() {
        model = OpenAiChatModel.builder()
                .apiKey(Settings.apiKey)// Please use your own OpenAI API key
                .modelName("gpt-4-turbo-preview")
                .timeout(Duration.ofMinutes(5))
                .maxTokens(4096)
                .temperature(0D)
//                .responseFormat("{ \"type\": \"json_object\" }")
                .build();

    }

    protected String chat(String sysPrompt, String userIdea) {

        System.out.println("sysPrompt : " + sysPrompt);
        System.out.println("userMessage : " + userIdea);
        System.out.println("=============================================");
        SystemMessage systemMessage = SystemMessage.from(sysPrompt);
        UserMessage userMessage = UserMessage.from(userIdea );

        List<ChatMessage> messageList = new ArrayList<>();
        messageList.add(systemMessage);
        messageList.add(userMessage);

        Response<AiMessage> response = model.generate(messageList);
        TokenUsage tokenUsage = response.tokenUsage();
        Integer total = tokenUsage.totalTokenCount();
        WorkFlowContext.sumTokenTotal(total);

        String answer = response.content().text();
        return answer;
    }


}
