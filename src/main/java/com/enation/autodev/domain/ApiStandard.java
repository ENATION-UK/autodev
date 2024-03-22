package com.enation.autodev.domain;

import lombok.Data;

import java.util.List;

/**
 * api规范
 *
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
@Data
public class ApiStandard {

    private String module;
    private String apiStandard;
    private List<File> files;


    @Data
    public static class File {
        private String fileName;
        private String filePath;
        private String description;

    }
}
