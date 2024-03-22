package com.enation.autodev.impl;

import com.enation.autodev.*;
import com.enation.autodev.domain.Requirement;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.openai.OpenAiImageModel;
import dev.langchain4j.model.output.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;


/**
 * UI设计
 *
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
@Service
public class UIDesign extends AIWorker implements Role {


    @Autowired
    WorkLog workLog;

    @Override
    public void run() {
        OpenAiImageModel model = OpenAiImageModel.builder().apiKey(Settings.apiKey).modelName("dall-e-3").style("natural").responseFormat("b64_json").build();
        String  userIdea = WorkFlowContext.getTask(TaskType.UserIdea);
        Requirement requirement = WorkFlowContext.getTask(TaskType.RequirementAnalysis);
        for (Requirement.Page page : requirement.getPages()) {
            String pageName = page.getPageName();
            String prompt = userIdea+"\n# 需要你帮我设计其中的一个"
                    + pageName + "\n"
                    + "# 只设计页面本身，不设计任何页面之外的功能和场景\n"
                    + "# 需要页面的正面设计图，不要倾斜\n"
                    + "# 功能要求:"+page.getRequirementDescription() + "\n"
                    + "# 字体要求：Microsoft YaHei,Hiragino Sans GB,sans-serif\n"
                    + "# 页面风格需求：\n"
                    + requirement.getPageStyle();
            System.out.println(prompt);
            Response<Image> generate = model.generate(prompt);
            String base64Data = generate.content().base64Data();

            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            try {
                Files.write(Paths.get(Settings.workingDir+ File.separator+"ui"+File.separator+ pageName +".png"), imageBytes, StandardOpenOption.CREATE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    public static void main(String[] args) {

        String prompt = "我需要正在开发一款Web应用，需要你帮我设计"
                + "结算页\n"
                + "# 功能需求:\n用户确认购买商品，填写收货地址，选择支付方式等。功能包括提交订单、选择优惠券、发票信息等\n"
                + "# 只设计页面本身，不设计任何页面之外的功能和场景\n"
                + "# 需要页面的正面设计图，不要倾斜\n"
                + "#页面风格需求：\n"
                + "- 字体要求： 12px/150% tahoma,arial,Microsoft YaHei,Hiragino Sans GB,\"\\u5b8b\\u4f53\",sans-serif\n"
                + "- 简洁明了：页面设计应避免过多复杂的元素，使用户能快速找到他们需要的信息。\n" +
                "- 用户友好：设计应考虑用户的使用习惯，使操作流程尽可能简单直观。\n" +
                " -一致性：所有页面的设计风格应保持一致，包括颜色、字体、布局等";
        Response<Image> generate = OpenAiImageModel.builder().apiKey(Settings.apiKey).modelName("dall-e-3").style("natural").responseFormat("b64_json").build().generate(prompt);
        String base64Data = generate.content().base64Data();

        byte[] imageBytes = Base64.getDecoder().decode(base64Data);

        try {
            Files.write(Paths.get(Settings.workingDir+"/output.png"), imageBytes, StandardOpenOption.CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
