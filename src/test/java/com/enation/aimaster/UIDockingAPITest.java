package com.enation.aimaster;

import com.enation.aimaster.domain.DatabaseStructure;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Type;
import java.util.List;

import static com.enation.aimaster.FileUtils.readFile;
import static java.awt.SystemColor.text;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
@SpringBootTest
public class UIDockingAPITest {

    @Test
    public void test() throws AIResAnalyzingException {
        String prompt = readFile("/response/ddl-json.txt");
        prompt = FileUtils.jsonExtract(prompt);
        Gson gson = new Gson();
        Type personListType = new TypeToken<List<DatabaseStructure>>(){}.getType();
        List<DatabaseStructure> databaseStructure = gson.fromJson(prompt, personListType);
        WorkFlowContext.putResult(TaskType.DatabaseDesign, databaseStructure);



    }
}
