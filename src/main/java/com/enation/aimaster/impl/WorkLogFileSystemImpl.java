package com.enation.aimaster.impl;

import com.enation.aimaster.Settings;
import com.enation.aimaster.WorkLog;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
@Service
public class WorkLogFileSystemImpl implements WorkLog {
    @Override
    public void writeLog(String fileName, String log) {
        String filepath = Settings.workingDir + File.separator + fileName;
        try {
            FileUtils.write(new File(filepath), log, "utf-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
