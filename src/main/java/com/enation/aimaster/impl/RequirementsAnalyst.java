package com.enation.aimaster.impl;

import com.enation.aimaster.*;
import com.enation.aimaster.domain.Requirement;
import com.google.gson.Gson;
import dev.langchain4j.data.message.*;
import dev.langchain4j.model.output.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.enation.aimaster.FileUtils.readFile;


/**
 * 需求分析师
 *
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
@Service
public class RequirementsAnalyst extends AIWorker implements Role {


    @Autowired
    private  WorkLog workLog;

    @Override
    public void run() throws AIResAnalyzingException {

        //做出需求分析
        String userIdea = WorkFlowContext.getTask(TaskType.UserIdea);
        String sysPrompt = readFile("/prompts/req-analysis.txt");
        SystemMessage systemMessage = SystemMessage.from(sysPrompt);

        UserMessage userMessage = UserMessage.from(
                TextContent.from(userIdea)
        );

        List<ChatMessage> messageList = new ArrayList<>();
        messageList.add(systemMessage);
        messageList.add(userMessage);

        Response<AiMessage> response = model.generate(messageList);
        String answer = response.content().text();

        workLog.writeLog("req-analysis.txt", answer);
        WorkFlowContext.putResult(TaskType.RequirementAnalysis, answer);

        //将需求分析整理为json
        sysPrompt = readFile("/prompts/req-to-json.txt");
        systemMessage = SystemMessage.from(sysPrompt);

        userMessage = UserMessage.from(
                TextContent.from(answer)
        );
        messageList = new ArrayList<>();
        messageList.add(systemMessage);
        messageList.add(userMessage);

        response = model.generate(messageList);
        String text = response.content().text();

        workLog.writeLog("req-to-json.txt", text);

        text = FileUtils.jsonExtract(text);

        workLog.writeLog("req-json.txt", text);

        Gson gson = new Gson();
        Requirement requirement = gson.fromJson(text, Requirement.class);

        WorkFlowContext.putResult(TaskType.RequirementAnalysis, requirement);

    }
}
