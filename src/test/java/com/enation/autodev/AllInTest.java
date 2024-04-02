package com.enation.autodev;

import com.enation.autodev.impl.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
@SpringBootTest
public class AllInTest {
    @Autowired
    ApiDeveloper apiDeveloper;
    @Autowired
    DatabaseDesign databaseDesign;

    @Autowired
    RequirementsAnalyst requirementsAnalyst;
    @Autowired
    SoftwareArchitect softwareArchitect;

    @Autowired
    SourceMerge sourcesMerge;
    @Autowired
    UIDesign uiDesign;

    @Autowired
    UIDeveloper uiDeveloper;

    @Test
    public void test() throws AIResAnalyzingException {

        WorkFlowContext.putTask(TaskType.UserIdea,"我想开发个电子商城系统");
        List<Role> roles = Arrays.asList(requirementsAnalyst,databaseDesign,softwareArchitect,apiDeveloper,uiDeveloper);

        for (Role role : roles) {
            role.run();
            System.out.println("已消耗token: "+WorkFlowContext.getTokenTotal());
        }

    }
}
