package com.enation.autodev;

import com.enation.autodev.impl.RequirementsAnalyst;
import org.junit.jupiter.api.Test;

/**
 * 需求分析测试
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
public class ReqAnalysisTest {

    @Test
    public void test() throws AIResAnalyzingException {
        RequirementsAnalyst reqAnalysis = new RequirementsAnalyst();
        WorkFlowContext.putTask(TaskType.UserIdea,"我想开发一款blog");
        reqAnalysis.run();


    }



}
