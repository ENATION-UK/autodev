package com.enation.aimaster.impl;

import com.enation.aimaster.Settings;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.time.Duration;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
public class AIWorker {

    protected ChatLanguageModel model;

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

}
