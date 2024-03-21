package com.enation.aimaster.impl;

import com.enation.aimaster.*;
import com.enation.aimaster.domain.DatabaseStructure;
import com.enation.aimaster.domain.Requirement;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.langchain4j.data.message.*;
import dev.langchain4j.model.output.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.enation.aimaster.FileUtils.readFile;

/**
 * 数据库设计
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
@Service
public class DatabaseDesign extends AIWorker implements Role {

    @Autowired
    private  WorkLog workLog;

    @Override
    public void run() throws AIResAnalyzingException {
        String sysPrompt = readFile("/prompts/database-design.txt");
        SystemMessage systemMessage = SystemMessage.from(sysPrompt);

        Requirement requirement = WorkFlowContext.getTask(TaskType.RequirementAnalysis);

        Gson gson = new Gson();
        String moduleJson = gson.toJson(requirement.getModules());
        System.out.println(moduleJson);

        UserMessage userMessage = UserMessage.from(   moduleJson  );

        List<ChatMessage> messageList = new ArrayList<>();
        messageList.add(systemMessage);
        messageList.add(userMessage);

        Response<AiMessage> response = model.generate(messageList);

        String text = response.content().text();

        workLog.writeLog("database-design.txt", text);

        text = FileUtils.jsonExtract(text);

        workLog.writeLog("ddl-json.txt", text);
        Type personListType = new TypeToken<List<DatabaseStructure>>(){}.getType();
        List<DatabaseStructure> databaseStructure = gson.fromJson(text, personListType);
        WorkFlowContext.putResult(TaskType.DatabaseDesign, databaseStructure);

    }
}
