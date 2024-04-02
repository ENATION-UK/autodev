package com.enation.autodev;

import com.enation.autodev.impl.SoftwareArchitect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
@SpringBootTest
public class SoftwareArchitectTest {

    @Autowired
    private SoftwareArchitect softwareArchitect;
    @Test
    public void test() throws AIResAnalyzingException
    {
        Mocker.mockRequirementAnalysis();
        softwareArchitect.run();

    }
}
