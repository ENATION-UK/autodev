package com.enation.autodev;

import com.enation.autodev.domain.DatabaseStructure;
import com.enation.autodev.domain.Requirement;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.List;

import static com.enation.autodev.FileUtils.readFile;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
public class Mocker {
    public static void mockRequirementAnalysis() throws AIResAnalyzingException {
        WorkFlowContext.putTask(TaskType.UserIdea,"我想开发一款电子商务系统");

        String prompt = readFile("/response/req-to-json.txt");
        prompt = FileUtils.jsonExtract(prompt);
        Gson gson = new Gson();
        Requirement requirement = gson.fromJson(prompt, Requirement.class);

        WorkFlowContext.putResult(TaskType.RequirementAnalysis,requirement);
    }

    public static void mockDatabaseDesign() throws AIResAnalyzingException {
        String prompt = readFile("/response/ddl-json.txt");
        prompt = FileUtils.jsonExtract(prompt);
        Gson gson = new Gson();
        Type personListType = new TypeToken<List<DatabaseStructure>>(){}.getType();
        List<DatabaseStructure> databaseStructure = gson.fromJson(prompt, personListType);
        WorkFlowContext.putResult(TaskType.DatabaseDesign, databaseStructure);

    }

    @Test
    public void testDatabaseDesignMock() throws AIResAnalyzingException {
        mockDatabaseDesign();
        List<DatabaseStructure> databaseStructure = WorkFlowContext.getResult(TaskType.DatabaseDesign);
        System.out.println(databaseStructure);
    }

    @Test
    public void testRequirementMock() throws AIResAnalyzingException {
        mockRequirementAnalysis();
        Requirement requirement = WorkFlowContext.getResult(TaskType.RequirementAnalysis);
        System.out.println(requirement);
    }
}
