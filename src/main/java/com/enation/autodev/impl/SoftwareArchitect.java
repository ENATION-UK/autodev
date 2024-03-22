package com.enation.autodev.impl;

import com.enation.autodev.*;
import com.enation.autodev.domain.ApiArchitecture;
import com.enation.autodev.domain.ApiStandard;
import com.enation.autodev.domain.FunctionPoint;
import com.enation.autodev.domain.Requirement;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

import static com.enation.autodev.FileUtils.readFile;
import static com.enation.autodev.TaskType.Architect;

/**
 * 架构师
 *
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
@Service
public class SoftwareArchitect extends AIWorker implements Role {
    @Override
    public void run() throws AIResAnalyzingException {
        Requirement requirement = WorkFlowContext.getResult(TaskType.RequirementAnalysis);

        /**
         * -------------------------------------------------------
         *         梳理开发依赖，定义模块功能的开发流程
         * ------------------------------------------------------
         */

        String json = GsonHelper.getGson().toJson(requirement.getModules());

        String sysPrompt = readFile("/prompts/dev-flow.txt");
        String devFLowJson = chat(sysPrompt, json);
        devFLowJson = FileUtils.jsonExtract(devFLowJson);
        workLog.writeLog("dev-flow.txt", devFLowJson);

        Type listType = new TypeToken<List<FunctionPoint>>() {
        }.getType();
        List<FunctionPoint> devLow = GsonHelper.getGson().fromJson(devFLowJson, listType);


        /**
         * -------------------------------------------------------
         *        定义api规范
         * ------------------------------------------------------
         */
        sysPrompt = readFile("/prompts/api-design.txt");
        String apiStandardJson = chat(sysPrompt, json);
        apiStandardJson = FileUtils.jsonExtract(apiStandardJson);
        workLog.writeLog("api-design.txt", apiStandardJson);

        Type standType = new TypeToken<List<ApiStandard>>() {
        }.getType();

        List<ApiStandard> standards = GsonHelper.getGson().fromJson(apiStandardJson, standType);


        ApiArchitecture apiArchitecture = new ApiArchitecture();
        apiArchitecture.setApiStandardList(standards);
        apiArchitecture.setDevelopFlow(devLow);

        WorkFlowContext.putResult(Architect, apiArchitecture);
    }
}
