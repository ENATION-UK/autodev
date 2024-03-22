package com.enation.autodev.impl;

import com.enation.autodev.*;
import com.enation.autodev.domain.Requirement;
import dev.langchain4j.data.message.*;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.enation.autodev.FileUtils.convertBase64;
import static com.enation.autodev.FileUtils.readFile;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_VISION_PREVIEW;


/**
 * 前端开发工程师
 *
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
@Service
public class UIDeveloper extends AIWorker  implements Role {

    @Autowired
    protected WorkLog workLog;

    
    public void run() throws AIResAnalyzingException {
        //把基本页面生成
        this.runStep2();

    }


    public void runStep2() {
        Requirement requirement = WorkFlowContext.getResult(TaskType.RequirementAnalysis);

        String sysPrompt = readFile("/prompts/vue_tailwind.txt");

        for (Requirement.Page page : requirement.getPages()) {
            String pageName = page.getPageName();

            String baseDir = Settings.workingDir + File.separator + "ui" + File.separator + pageName;

            String answer =chat(sysPrompt, "页面:'"+page.getPageName()+"'\n功能描述:" + page.getRequirementDescription());

            workLog.writeLog("html/"+page.getPageName()+".html", answer);
        }


    }

    public void runStep1(){

        String sysPrompt = readFile("/prompts/vue_tailwind.txt");
        //获取需求
        Requirement requirement = WorkFlowContext.getTask(TaskType.RequirementAnalysis);

        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey(Settings.apiKey) // Please use your own OpenAI API key
                .modelName(GPT_4_VISION_PREVIEW)
                .timeout(Duration.ofMinutes(5))
                .maxTokens(4096)
                .temperature(0D)
                .logRequests(true)
                .build();

        for (Requirement.Page page : requirement.getPages()) {
            String pageName = page.getPageName();

            String baseDir = Settings.workingDir + File.separator + "ui" + File.separator + pageName;

            String base64 = convertBase64(baseDir+".png");

            ImageContent imageContent = ImageContent.from(base64, "image/png");

            UserMessage userMessage = UserMessage.from(
                    TextContent.from("Please generate code based on the image"),
                    imageContent
            );
//
            UserMessage userMessage1 = UserMessage.from("功能描述:" + page.getRequirementDescription());
            SystemMessage systemMessage = SystemMessage.from(sysPrompt);

            List<ChatMessage> messageList = new ArrayList<>();
            messageList.add(systemMessage);
            messageList.add(userMessage1);
            messageList.add(userMessage);

            Response<AiMessage> response = model.generate(messageList);
            String answer = response.content().text();

            workLog.writeLog("html/"+page.getPageName()+".html", answer);
        }


    }

    interface Assistant {

        String chat(String message);
    }


}
