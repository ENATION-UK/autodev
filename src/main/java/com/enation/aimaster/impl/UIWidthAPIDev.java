//package com.enation.aimaster.impl;
//
//import com.enation.aimaster.*;
//import com.enation.aimaster.domain.DatabaseStructure;
//import com.google.gson.reflect.TypeToken;
//import dev.langchain4j.data.message.*;
//import dev.langchain4j.model.output.Response;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.enation.aimaster.FileUtils.readFile;
//
///**
// * @author kingapex
// * @version 1.0
// * @data 2022/12/23 12:02
// * @since 7.3.0
// **/
//@Service
//public class UIWidthAPIDev extends AIWorker implements Role {
//
//    @Autowired
//    private  WorkLog workLog;
//
//
//    @Override
//    public void run() throws AIResAnalyzingException {
//
//        String sysPrompt = readFile("/prompts/UI-docking-API.txt");
//        SystemMessage systemMessage = SystemMessage.from(sysPrompt);
//
//
//
//        UserMessage userMessage = UserMessage.from(
//                TextContent.from(userIdea)
//        );
//
//        List<ChatMessage> messageList = new ArrayList<>();
//        messageList.add(systemMessage);
//        messageList.add(userMessage);
//
//        Response<AiMessage> response = model.generate(messageList);
//        String answer = response.content().text();
//
//        DatabaseStructure result = WorkFlowContext.getResult(TaskType.DatabaseDesign);
//
//    }
//}
