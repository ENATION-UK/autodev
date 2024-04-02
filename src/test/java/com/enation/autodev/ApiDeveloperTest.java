package com.enation.autodev;

import com.enation.autodev.domain.ApiArchitecture;
import com.enation.autodev.domain.ApiStandard;
import com.enation.autodev.domain.FunctionPoint;
import com.enation.autodev.domain.SourceCode;
import com.enation.autodev.impl.ApiDeveloper;
import com.enation.autodev.impl.SourceMerge;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.enation.autodev.FileUtils.readFile;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
@SpringBootTest
public class ApiDeveloperTest {

    @Autowired
    private ApiDeveloper apiDeveloper;

    @Autowired
    private SourceMerge sourceMerge;

    public void mock() throws AIResAnalyzingException {
        Mocker.mockDatabaseDesign();

        String devFlowText = readFile("/response/dev-flow.txt");

        Type listType = new TypeToken<List<FunctionPoint>>(){}.getType();
        List<FunctionPoint> devLow = GsonHelper.getGson().fromJson(devFlowText, listType);

        String apiStand = readFile("/response/api-stand.txt");
        Type standType = new TypeToken<List<ApiStandard>>() {
        }.getType();

        List<ApiStandard> standards = GsonHelper.getGson().fromJson(apiStand, standType);


        ApiArchitecture apiArchitecture = new ApiArchitecture();
        apiArchitecture.setApiStandardList(standards);
        apiArchitecture.setDevelopFlow(devLow);

        WorkFlowContext.putResult(TaskType.Architect,apiArchitecture);
    }
    @Test
    public void test() throws AIResAnalyzingException {
        this.mock();
        apiDeveloper.run();

    }
    private   List<SourceCode> convertSource(String source) {
        Type personListType = new TypeToken<List<SourceCode>>(){}.getType();
        List<SourceCode> sourceCodeList = GsonHelper.getGson().fromJson(source, personListType);
        return sourceCodeList;
    }
    @Test
    public void testMerge() throws AIResAnalyzingException {

        this.mock();

        ApiArchitecture apiArchitecture = WorkFlowContext.getResult(TaskType.Architect);
        List<FunctionPoint> developFlow = apiArchitecture.getDevelopFlow();
        List<SourceCode> allCodeList = new ArrayList<>();
        for (FunctionPoint functionPoint : developFlow) {
            String fileJson = readFile("/response/api-code/api-"+functionPoint.getModule()+"-"+functionPoint.getFunction()+".txt");
            String sourceCode = FileUtils.jsonExtract(fileJson);
            List<SourceCode> sourceCodeList = convertSource(sourceCode);
            allCodeList.addAll(sourceCodeList);
        }
        WorkFlowContext.putResult(TaskType.APIDevelopment,allCodeList);
        sourceMerge.run();

    }

}
