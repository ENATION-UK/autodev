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
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.MessageManager;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
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


    protected    String  chataliyun(String sysPrompt, String userIdea)
    {
        System.out.println("======================sysPrompt : ======================\n\n" + sysPrompt);
        System.out.println("======================userMessage : ======================\n\n " + userIdea);


        MessageManager msgManager = new MessageManager(10);
        Message systemMsg =
                Message.builder().role(Role.SYSTEM.getValue()).content(sysPrompt).build();
        Message userMsg = Message.builder().role(Role.USER.getValue()).content(userIdea).build();

        msgManager.add(systemMsg);
        msgManager.add(userMsg);
        QwenParam param =
                QwenParam.builder().apiKey("sk-kkkkk").model("baichuan2-7b-chat-v1").messages(msgManager.get())
                        .resultFormat(QwenParam.ResultFormat.MESSAGE)
                        .temperature(0.1f)
                        .build();
        GenerationResult result = null;
        try {
            Generation  gen = new Generation();;
            result = gen.call(param);
        } catch (NoApiKeyException e) {
            throw new RuntimeException(e);
        } catch (InputRequiredException e) {
            throw new RuntimeException(e);
        }
        Integer inputTokens = result.getUsage().getInputTokens();
        Integer outputTokens = result.getUsage().getOutputTokens();
        Integer total= inputTokens + outputTokens;
        WorkFlowContext.sumTokenTotal(total);
        System.out.println("=======================response======================");
        String content = result.getOutput().getChoices().get(0).getMessage().getContent();
        System.out.println(content);
        return content;

    }

}
