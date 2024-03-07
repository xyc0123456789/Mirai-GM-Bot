package com.king.model;

import com.king.component.MyBot;
import com.king.util.ZipUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DetectDirAndZip implements Runnable{

    private ScheduledExecutorService scheduledExecutorService;

    private long startTime;

    private String filePath;

    private int fileCount;

    public DetectDirAndZip(ScheduledExecutorService scheduledExecutorService, long startTime, String filePath, int fileCount) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.startTime = startTime;
        this.filePath = filePath;
        this.fileCount = fileCount;
    }

    @Override
    public void run() {
        boolean flag = true;
        try {
            File file = new File(filePath);
            if (file.exists()){
                File[] files = file.listFiles();
                if (files !=null&& files.length==fileCount){
                    flag=false;
                    File zipFile = ZipUtil.sourceToZip(filePath, filePath.substring(0, filePath.lastIndexOf("/")));
                    if (MyBot.bot!=null){
                        try {
                            Objects.requireNonNull(MyBot.bot.getGroup(GroupId.TEST)).getFiles().uploadNewFile(zipFile.getName(), ExternalResource.create(zipFile).toAutoCloseable());
                            log.info("{} upload success!", filePath);
                        }catch (Exception ignore){}
                    }
                }
            }
        }catch (Exception ignore){}
        if (flag && (System.currentTimeMillis()-startTime<3600000)){
            scheduledExecutorService.schedule(new DetectDirAndZip(scheduledExecutorService,startTime,filePath,fileCount),30, TimeUnit.SECONDS);
        }else {
            log.error("job terminaled: {}", filePath);
        }

    }
}
