package com.enation.autodev.domain;

import com.enation.autodev.ProjectType;
import lombok.Data;

import java.util.List;

/**
 * 项目
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
@Data
public class Project {

    /**
     * 项目类型
     */
    private ProjectType type;

    /**
     * 源代码文件列表
     */
    private List<SourceCode> files;



}
