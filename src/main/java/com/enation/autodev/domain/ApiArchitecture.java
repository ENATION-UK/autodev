package com.enation.autodev.domain;

import lombok.Data;

import java.util.List;

/**
 * API架构
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
@Data
public class ApiArchitecture {

    /**
     *  模块开发顺序：
     *  {"用户模块":"注册"},
     * {"用户模块":"登录"}
     * {"文章模块":"发布"},
     * {"文章模块":"评论"}"
     */


    private List<FunctionPoint> developFlow;

    private List<ApiStandard>  apiStandardList;
}
