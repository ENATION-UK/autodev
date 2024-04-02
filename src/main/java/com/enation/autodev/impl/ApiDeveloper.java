package com.enation.autodev.impl;

import com.enation.autodev.*;
import com.enation.autodev.domain.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.enation.autodev.FileUtils.readFile;

/**
 * api 开发
 *
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
@Service
public class ApiDeveloper extends AIWorker implements Role {


    @Autowired
    SourceMerge sourceMerge;

    @Override
    public void run() throws AIResAnalyzingException {
        ApiArchitecture apiArchitecture = WorkFlowContext.getResult(TaskType.Architect);
        List<FunctionPoint> developFlow = apiArchitecture.getDevelopFlow();
        List<SourceCode> sourceCodeList = new ArrayList<>();

        for (FunctionPoint mf : developFlow) {
            dev(mf.getModule(), mf.getFunction(), apiArchitecture,sourceCodeList);
        }

        WorkFlowContext.putResult(TaskType.APIDevelopment,sourceCodeList);
        sourceMerge.run();
    }

    /**
     * 项目初始化
     */

    private void projectInit() {
        URL url = this.getClass().getClassLoader().getResource("project-template/springboot");

        File srcDir = new File(url.getPath());
        File destDir = new File(Settings.workingDir+"/api/project");
        try {
            org.apache.commons.io.FileUtils.copyDirectory(srcDir, destDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dev(String module, String function, ApiArchitecture apiArchitecture, List<SourceCode> apiCodeList) throws AIResAnalyzingException {

        String apiStandard = findModuleStandard(module, apiArchitecture);

        String sysPrompt = readFile("/prompts/api-dev.txt");

        sysPrompt = sysPrompt.replaceAll("#api-standard#", apiStandard);

        String ddl = convertDll();
        sysPrompt = sysPrompt.replaceAll("#all-dll#", ddl);


        String userPrompt = module + "的" + function + "功能";
        String result = chat(sysPrompt, userPrompt);
        result= FileUtils.jsonExtract(result);
        workLog.writeLog("api/api-" + module + "-" + function + ".txt", result);

        //转换为源码类，整体存入源码列表
        List<SourceCode> sourceCodeList = convertSource(result);
        apiCodeList.addAll(sourceCodeList);

    }


    private String convertDll() {
        List<DatabaseStructure>   databaseStructures = WorkFlowContext.getResult(TaskType.DatabaseDesign);
        String ddl = "";
        for (DatabaseStructure databaseStructure : databaseStructures) {
            ddl+="\n"+databaseStructure.getModuleName()+"\n```"+databaseStructure.getDdl()+"\n```\n"+databaseStructure.getDescription()+"\n";
        }

        return ddl;
    }

    private   List<SourceCode> convertSource(String source) {
        Type personListType = new TypeToken<List<SourceCode>>(){}.getType();
        List<SourceCode> sourceCodeList = GsonHelper.getGson().fromJson(source, personListType);
        return sourceCodeList;
    }

    private String findModuleStandard(String module, ApiArchitecture apiArchitecture) {
        for (ApiStandard apiStandard : apiArchitecture.getApiStandardList()) {
            if (apiStandard.getModule().equals(module)) {
                return GsonHelper.getGson().toJson(apiStandard);
            }
        }
        throw new RuntimeException("未找到模块" + module + "的api标准");
    }


}
