package com.enation.aimaster;

/**
 * AI模型
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
public enum AIModel {

    gpt40125Preview("gpt-4-0125-preview"),
    gpt41106VisionPreview("gpt-4-1106-vision-preview");


    private String name;

    AIModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
