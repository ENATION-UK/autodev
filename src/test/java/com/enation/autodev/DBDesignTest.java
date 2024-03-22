package com.enation.autodev;

import com.enation.autodev.domain.DatabaseStructure;
import com.enation.autodev.domain.Requirement;
import com.enation.autodev.impl.DatabaseDesign;
import com.google.gson.Gson;
import org.apache.commons.text.StringSubstitutor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static com.enation.autodev.FileUtils.readFile;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/

@SpringBootTest
public class DBDesignTest {

    @Autowired
    DatabaseDesign databaseDesign;

    @Test
    public void test2() throws AIResAnalyzingException {

        WorkFlowContext.putTask(TaskType.UserIdea,"我想开发一款电子商务系统");

        String prompt = readFile("/response/req-sort-out.txt");
        prompt = FileUtils.jsonExtract(prompt);
        Gson gson = new Gson();
        Requirement requirement = gson.fromJson(prompt, Requirement.class);


        WorkFlowContext.putTask(TaskType.RequirementAnalysis,requirement);
        databaseDesign.run();
        DatabaseStructure result = WorkFlowContext.getResult(TaskType.DatabaseDesign);
        System.out.println(result);

    }

    public static void main(String[] args) {
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("name", "Alice");
        String templateString = "Hello, ${name}!";
        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        String resolvedString = sub.replace(templateString);
        System.out.println(resolvedString);
    }


}
