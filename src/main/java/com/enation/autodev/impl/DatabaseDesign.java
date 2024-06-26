package com.enation.autodev.impl;

import com.enation.autodev.*;
import com.enation.autodev.domain.DatabaseStructure;
import com.enation.autodev.domain.Requirement;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.langchain4j.data.message.*;
import dev.langchain4j.model.output.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.enation.autodev.FileUtils.readFile;

/**
 * 数据库设计
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
@Service
public class DatabaseDesign extends AIWorker implements Role {

    

    @Override
    public void run() throws AIResAnalyzingException {
        String sysPrompt = readFile("/prompts/database-design.txt");

        Requirement requirement = WorkFlowContext.getResult(TaskType.RequirementAnalysis);

        Gson gson = new Gson();
        String moduleJson = gson.toJson(requirement.getModules());
        System.out.println(moduleJson);


        String text = chat(sysPrompt, moduleJson);

//        workLog.writeLog("database-design.txt", text);

        text = FileUtils.jsonExtract(text);

        workLog.writeLog("ddl-json.txt", text);
        Type personListType = new TypeToken<List<DatabaseStructure>>(){}.getType();
        List<DatabaseStructure> databaseStructure = gson.fromJson(text, personListType);
        WorkFlowContext.putResult(TaskType.DatabaseDesign, databaseStructure);

    }
}
