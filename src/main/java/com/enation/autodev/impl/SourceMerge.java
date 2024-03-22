package com.enation.autodev.impl;

import com.enation.autodev.*;
import com.enation.autodev.domain.SourceCode;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

@Service
public class SourceMerge extends AIWorker implements Role {


    @Override
    public void run() throws AIResAnalyzingException {
        String sysPrompt = readFile("/prompts/file-merge.txt");

        List<SourceCode> sourceCodeList = WorkFlowContext.getResult(TaskType.APIDevelopment);
        Map<String, List<SourceCode>> map = summarize(sourceCodeList);

        List<SourceCode> mergedCodeList = new ArrayList<>();

        map.forEach((fileName, codeList) -> {

            if (!codeList.isEmpty()) {

                String fileContent = "";
                int index = 1;
                for (SourceCode sourceCode : codeList) {
                     fileContent =fileContent+ "version";
                    fileContent = fileContent + index + ":\n";
                    fileContent = fileContent + "```\n";
                    fileContent = fileContent + sourceCode.getFileContent();
                    fileContent = fileContent + "```\n";
                    index++;
                }

                //让ai合并各个版本的code，并放到一个新的完整代码中
                String mergedCode = chat(sysPrompt, fileContent);
                try {
                    mergedCode = FileUtils.jsonExtract(mergedCode);
                } catch (AIResAnalyzingException e) {
                    throw new RuntimeException(e);
                }
                SourceCode fullSource = codeList.get(0);
                fullSource.setFileContent(mergedCode);
                mergedCodeList.add(fullSource);

            }


        });
        WorkFlowContext.putResult(TaskType.APIDevelopment, mergedCodeList);

        for (SourceCode sourceCode : mergedCodeList) {

            String filePath = Settings.workingDir +"/api/project/src/main/java"+ sourceCode.getFilePath();

            File file = new File(filePath);
            try {
                org.apache.commons.io.FileUtils.write(file, sourceCode.getFileContent(), "UTF-8");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }


    private Map<String, List<SourceCode>> summarize(List<SourceCode> sourceCodeList) {
        Map<String, List<SourceCode>> map = new HashMap<>();
        for (SourceCode sourceCode : sourceCodeList) {
            List<SourceCode> list = map.get(sourceCode.getFileName());
            if (list == null) {
                list = new ArrayList<>();
                map.put(sourceCode.getFileName(), list);
            }
            list.add(sourceCode);
        }

        return map;

    }


}
