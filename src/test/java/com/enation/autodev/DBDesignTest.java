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
import java.util.List;
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


        databaseDesign.run();
        List<DatabaseStructure>   result = WorkFlowContext.getResult(TaskType.DatabaseDesign);
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
