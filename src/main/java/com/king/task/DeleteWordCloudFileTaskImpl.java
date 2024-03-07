package com.king.task;

import com.king.config.CommonConfig;
import com.king.util.DateFormateUtil;
import com.king.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Date;

/**
 * @description: 删除某一天的词云txt
 * @author: xyc0123456789
 * @create: 2023/3/2 10:09
 **/
@Slf4j
public class DeleteWordCloudFileTaskImpl {

    public static void deleteWordCloudTxtFile(int offDate){
        File currentFile = new File("");
        currentFile = new File(currentFile.getAbsolutePath());
        File[] listFiles = currentFile.listFiles();
        if (listFiles ==null){
            return;
        }
        Date date = DateFormateUtil.offDate(offDate);
        String formatYYYYMMDD = DateFormateUtil.formatYYYYMMDD(date);
        log.info("deleteWordCloudTxtFile offDate:{}",formatYYYYMMDD);
        for (File file: listFiles){
            String name = file.getName();
            if (name.startsWith(formatYYYYMMDD) && name.endsWith("txt")){
                FileUtil.deleteFile(file);
            }
        }
    }

    public static void deleteMemeFile(int topCount){
        File currentFile = new File("");
        currentFile = new File(currentFile.getAbsolutePath());
        deleteDirFile(currentFile, topCount);
    }

    public static void deleteDirFile(File currentFile, int topCount){
        File[] listFiles = currentFile.listFiles();
        if (listFiles ==null){
            return;
        }
        int count = 0;
        for (File file: listFiles){
            String name = file.getName();
            if (name.startsWith("prememe") || name.startsWith("meme")){
                count +=1;
            }
        }
        log.info("topCount:{} count:{}",topCount, count);
        if (topCount <= 0 || count > topCount){
            for (File file: listFiles){
                String name = file.getName();
                if (name.startsWith("prememe") || name.startsWith("meme")){
                    FileUtil.deleteFile(file);
                }
            }
        }
    }




}
