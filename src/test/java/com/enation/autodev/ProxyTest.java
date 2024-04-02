package com.enation.autodev;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
public class ProxyTest {
    public static void main(String[] args) {
        Proxy proxy = new Proxy(Proxy.Type.SOCKS,new InetSocketAddress("127.0.0.1", 7890));
        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey(Settings.apiKey)// Please use your own OpenAI API key
                .modelName("gpt-4-turbo-preview")
                .timeout(Duration.ofMinutes(5))
//                .proxy(proxy)
//                .baseUrl("https://xxx" )
                .maxTokens(4096)
                .temperature(0D)
//                .responseFormat("{ \"type\": \"json_object\" }")
                .build();

        String generate = model.generate("hello");
        System.out.println(generate);
    }

}
