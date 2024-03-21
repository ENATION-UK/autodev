package com.enation.aimaster.domain;

import lombok.Data;

/**
 * 需求
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
@Data
public class Requirement {
    private Page[] pages;
    private Module[] modules;
    private String pageStyle;
    private String other;

    @Data
    public static
    class Page{
        private String pageName;
        private String requirementDescription;
    }

    @Data
   public  static
    class Module{
        private String moduleName;
        private String requirementDescription;
    }
}
