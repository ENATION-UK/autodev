package com.enation.aimaster.impl;

import com.enation.aimaster.FileUtils;
import com.enation.aimaster.Settings;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;

import java.util.Arrays;
import java.util.List;

public class ServiceWithToolsExample {


    static class ProductSearch {

        @Tool("添加代码")
        void writeCode(String filePath,String lineNumber,String code) {
            System.out.println("写入代码到文件：" + filePath);
            System.out.println("行号：" + lineNumber);
            System.out.println("代码如下：");
            System.out.println(code);
        }

        @Tool("获取已有代码")
        String getCode(String filePath) {
            System.out.println("获取已有代码：" + filePath);
            return "import org.springframework.web.bind.annotation.RequestMapping;\n" +
                    "import org.springframework.web.bind.annotation.RestController;\n" +
                    "\n" +
                    "@RestController\n" +
                    "@RequestMapping(\"/user\")\n" +
                    "public class UserController {\n" +
                    "\n" +
                    "}\n";
        }


    }

    public static void main(String[] args) {

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(OpenAiChatModel.withApiKey(Settings.apiKey))
                .tools(new ProductSearch())
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();

//        String question = "价格100元的热水壶毛重是多少";
//        String question = "九阳热水壶的价格都是多少钱";

        String question = "用户模块的登录功能";

        String answer = assistant.chat(question);

        System.out.println(answer);
        // The square root of the sum of the number of letters in the words "hello" and "world" is approximately 3.162.
    }



    interface Assistant {

        @SystemMessage("你是一名专业的Java开发工程师，你将这样编程:\n" +
                "# 在我已有代码基础上修改\n" +
                "# 通过获取已有代码来得到已有的代码\n" +
                "# 分析在哪行添加哪些代码，确保添加后的代码编译正确\n"+
                "# 通过添加代码方法对代码做出修改\n" +
                "# 你必须按照如下框架来实现: SpringBoot+MybatisPlus +Lombok\n" +
                "# 我的工程中目前已经有了如下代码\n" +
                "- /com/company/controller/UserController.java : 用户控制器类，处理用户相关的请求\n" +
                "# api的规范为: /user/*\n")
        String chat(String userMessage);
    }

}