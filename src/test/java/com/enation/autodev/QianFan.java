package com.enation.autodev;

import com.baidubce.qianfan.Qianfan;
import com.baidubce.qianfan.core.auth.Auth;
import com.baidubce.qianfan.model.chat.ChatResponse;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
public class QianFan {
    public static void main(String[] args) {
        Qianfan qianfan = new Qianfan(Auth.TYPE_OAUTH,"key", "secrekey");
        ChatResponse response = qianfan.chatCompletion()
                .model("ERNIE-4.0-8K") // 使用model指定预置模型

                // .endpoint("completions_pro") // 也可以使用endpoint指定任意模型 (二选一)
                .addMessage("user", "你好") // 添加用户消息 (此方法可以调用多次，以实现多轮对话的消息传递)
                .temperature(0.7) // 自定义超参数
                .execute(); // 发起请求
        System.out.println(response.getResult());
    }
}
