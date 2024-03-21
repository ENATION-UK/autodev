package com.enation.aimaster;

import lombok.Data;

/**
 * 任务
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
@Data
public class Task {

    /**
     * 定义工作的内容
     * @return
     */
    private String task;

    /**
     * 工作结果
     */
    private String result;

}
