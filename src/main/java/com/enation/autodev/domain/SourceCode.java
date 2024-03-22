package com.enation.autodev.domain;

import lombok.Data;

/**
 * 源码
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/

@Data
public class SourceCode {
    private String fileName;
    private String filePath;
    private String fileDesc;
    private String fileContent;
}
