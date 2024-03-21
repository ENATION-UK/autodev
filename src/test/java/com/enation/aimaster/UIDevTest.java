package com.enation.aimaster;


import com.enation.aimaster.domain.Requirement;
import com.enation.aimaster.impl.UIDeveloper;
import com.google.gson.Gson;
import dev.langchain4j.data.message.*;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.enation.aimaster.FileUtils.readFile;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
@SpringBootTest
public class UIDevTest {

    @Autowired
    UIDeveloper uiDeveloper;

    @Test
    public void dev() throws AIResAnalyzingException {

        WorkFlowContext.putTask(TaskType.UserIdea,"我想开发一款电子商务系统");

        String prompt = readFile("/response/req-sort-out.txt");
        prompt = FileUtils.jsonExtract(prompt);
        Gson gson = new Gson();
        Requirement requirement = gson.fromJson(prompt, Requirement.class);
        System.out.println(requirement);

        WorkFlowContext.putTask(TaskType.RequirementAnalysis,requirement);

        uiDeveloper.run();

    }
    @Test
    public void test3() {

        String prompt = readFile("/prompt/ui-api.txt");
        String htmlPrompt = readFile("/prompt/ui-html.txt");
        String reqPrompt = readFile("/prompt/req.txt");

        ChatLanguageModel model = OpenAiChatModel.builder()
                .baseUrl("https://chatgpt.javashop.cloud/v1/")
                .apiKey(Settings.apiKey)// Please use your own OpenAI API key
                .modelName("gpt-4-0125-preview")
                .maxTokens(4096)
                .temperature(0D)
                .timeout(Duration.ofSeconds(1000))
                .logRequests(true)

                .build();


        SystemMessage systemMessage = SystemMessage.from(prompt);

        UserMessage userMessage = UserMessage.from(
                TextContent.from(htmlPrompt),
                TextContent.from(reqPrompt)
        );
        List<ChatMessage> messageList = new ArrayList<>();
        messageList.add(systemMessage);
        messageList.add(userMessage);

        Response<AiMessage> response = model.generate(messageList);

        System.out.println(response.content().text());

    }

    @Test
    public void test2() {

        String prompt = readFile("/prompt/ui-api.txt");
        String htmlPrompt = readFile("/prompt/ui-html.txt");
        String modifyPrompt = readFile("/prompt/modify.txt");

        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey(Settings.apiKey)// Please use your own OpenAI API key
                .modelName("gpt-4-0125-preview")
                .maxTokens(4096)
                .temperature(0D)
                .build();


        SystemMessage systemMessage = SystemMessage.from(prompt);

        UserMessage userMessage = UserMessage.from(
                TextContent.from(htmlPrompt),
                TextContent.from(modifyPrompt)
        );
        List<ChatMessage> messageList = new ArrayList<>();
        messageList.add(systemMessage);
        messageList.add(userMessage);

        Response<AiMessage> response = model.generate(messageList);

        System.out.println(response.content().text());

     }
    @Test
    public void test() {

        String prompt = readFile("/prompts/ui-dev.txt");
        String htmlPrompt = readFile("/prompt/ui-html.txt");
        String reqPrompt = readFile("/prompt/req.txt");

        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey(Settings.apiKey)// Please use your own OpenAI API key
                .modelName("gpt-4-0125-preview")
                .maxTokens(4096)
                .temperature(0D)
                .build();

        SystemMessage systemMessage = SystemMessage.from(prompt);

        UserMessage userMessage = UserMessage.from(
                TextContent.from(htmlPrompt),
                TextContent.from(reqPrompt)
        );
        List<ChatMessage> messageList = new ArrayList<>();
        messageList.add(systemMessage);
        messageList.add(userMessage);

        Response<AiMessage> response = model.generate(messageList);

        System.out.println(response.content().text());
    }



}
