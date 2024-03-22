package com.enation.autodev;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 流水线上下文
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
public class WorkFlowContext {

    @Getter
    private static int tokenTotal = 0;

    public static synchronized void sumTokenTotal(int number) {
        tokenTotal = tokenTotal +number;
    }

    private static Map<TaskType, Object> taskMap = new HashMap<>();

    private static Map<TaskType, Object> resultMap = new HashMap<>();

    public static <T> void  putTask(TaskType type,  T task) {
        taskMap.put(type, task);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getTask(TaskType type) {
        return (T) taskMap.get(type);
    }

    public static <T> void putResult(TaskType type, T result) {
        resultMap.put(type, result);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getResult(TaskType type) {
        return (T) resultMap.get(type);
    }
}
