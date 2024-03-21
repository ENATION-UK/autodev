package com.enation.aimaster;

import com.enation.aimaster.domain.Requirement;
import com.enation.aimaster.impl.UIDesign;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.enation.aimaster.FileUtils.readFile;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/

@SpringBootTest
public class UIDesignTest {

    @Autowired
    UIDesign uiDesign;

    @Test
    public void test2() throws AIResAnalyzingException {

        WorkFlowContext.putTask(TaskType.UserIdea,"我想开发一款电子商务系统");

        String prompt = readFile("/response/req-sort-out.txt");
        prompt = FileUtils.jsonExtract(prompt);
        Gson gson = new Gson();
        Requirement requirement = gson.fromJson(prompt, Requirement.class);
        System.out.println(requirement);

        WorkFlowContext.putTask(TaskType.RequirementAnalysis,requirement);

        uiDesign.run();

    }


}
