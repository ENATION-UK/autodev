package com.enation.aimaster;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * AI返回解析异常
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
@Data
@AllArgsConstructor
public class AIResAnalyzingException extends Throwable {

    private String message;
    private String data;


}
