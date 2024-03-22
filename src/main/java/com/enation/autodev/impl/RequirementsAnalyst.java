package com.enation.autodev.impl;

import com.enation.autodev.*;
import com.enation.autodev.domain.FunctionPoint;
import com.enation.autodev.domain.Requirement;
import com.google.gson.reflect.TypeToken;
import dev.langchain4j.data.message.*;
import dev.langchain4j.model.output.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.enation.autodev.FileUtils.readFile;


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


    @Override
    public void run() throws AIResAnalyzingException {

        /**
         * -------------------------------------------------------
         *                 做出需求分析
         * ------------------------------------------------------
         */

        String userIdea = WorkFlowContext.getTask(TaskType.UserIdea);
        String sysPrompt = readFile("/prompts/req-analysis.txt");

        String answer = chat(sysPrompt, userIdea);



        workLog.writeLog("req-analysis.txt", answer);
        WorkFlowContext.putResult(TaskType.RequirementAnalysis, answer);


        /**
         * -------------------------------------------------------
         *                 将需求分析整理为json
         * ------------------------------------------------------
         */
        sysPrompt = readFile("/prompts/req-to-json.txt");

        String text = chat(sysPrompt, answer);

        workLog.writeLog("req-to-json.txt", text);

        text = FileUtils.jsonExtract(text);

        workLog.writeLog("req-json.txt", text);

        Requirement requirement =  GsonHelper.getGson().fromJson(text, Requirement.class);



        WorkFlowContext.putResult(TaskType.RequirementAnalysis, requirement);
    }


}
