package com.king.util;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.OfflineAudio;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.File;
import java.io.IOException;

@Slf4j
public class AudioUtil {

    private static final String SILK_DIR = "/usr/local/silk/silk/";

    private static final String encoder = "encoder";
    private static final String decoder = "decoder";

    public synchronized static File mp3ToSilk(File mp3File){
        String mainName = FileUtil.mainName(mp3File);
        File pcmFile = new File(mp3File.getParentFile(), mainName + ".pcm");
        File silkFile = new File(mp3File.getParentFile(), mainName + ".silk");
        try {
            String mp32pcm = "ffmpeg -i "+mp3File.getAbsolutePath()+" -f s16le -ar 24000 -ac 1 -acodec pcm_s16le "+pcmFile.getAbsolutePath();
            ShellExecutorUtil.exe(mp32pcm, 0, false);
            if (!pcmFile.exists()){
                throw new RuntimeException(pcmFile.getAbsolutePath()+"is not exists");
            }
            String pcm2silk = SILK_DIR+encoder+" "+pcmFile.getAbsolutePath()+" "+silkFile.getAbsolutePath()+" -tencent";
            ShellExecutorUtil.exe(pcm2silk,10, false);
            return silkFile;
        }catch (Exception e){
            log.error("mp3ToSilk err",e);
        }finally {
            com.king.util.FileUtil.deleteFile(pcmFile);
        }
        return null;
    }

    public synchronized static File silkToMp3(File silkFile){
        String mainName = FileUtil.mainName(silkFile);
        File pcmFile = new File(silkFile.getParentFile(), mainName + ".pcm");
        File mp3File = new File(silkFile.getParentFile(), mainName + ".mp3");
        try {
            String silk2pcm = SILK_DIR+decoder+" "+silkFile.getAbsolutePath()+" "+pcmFile.getAbsolutePath();
            ShellExecutorUtil.exe(silk2pcm,10, false);
            if (!pcmFile.exists()){
                throw new RuntimeException(pcmFile.getAbsolutePath()+"is not exists");
            }
            Thread.sleep(100);
            String pcm2mp3 = "ffmpeg -y -f s16le -ar 24000 -ac 1 -i "+pcmFile.getAbsolutePath()+" "+mp3File.getAbsolutePath();
            ShellExecutorUtil.exe(pcm2mp3, 0, false);
            return mp3File;
        }catch (Exception e){
            log.error("silkToMp3 err",e);
        }finally {
            com.king.util.FileUtil.deleteFile(pcmFile);
        }
        return null;
    }

    public synchronized static File silkToMp3WithRuntime(File silkFile){
        String mainName = FileUtil.mainName(silkFile);
        File pcmFile = new File(silkFile.getParentFile(), mainName + ".pcm");
        File mp3File = new File(silkFile.getParentFile(), mainName + ".mp3");
        try {
            String silk2pcm = SILK_DIR+decoder+" "+silkFile.getAbsolutePath()+" "+pcmFile.getAbsolutePath();
            ShellExecutorUtil.exeRuntime(silk2pcm);
            if (!pcmFile.exists()){
                throw new RuntimeException(pcmFile.getAbsolutePath()+"is not exists");
            }
            String pcm2mp3 = "ffmpeg -y -f s16le -ar 24000 -ac 1 -i "+pcmFile.getAbsolutePath()+" "+mp3File.getAbsolutePath();
            ShellExecutorUtil.exeRuntime(pcm2mp3);
            return mp3File;
        }catch (Exception e){
            log.error("silkToMp3 err",e);
        }finally {
            com.king.util.FileUtil.deleteFile(pcmFile);
        }
        return null;
    }


    public static void sendFriendAudioSilk(MessageEvent messageEvent,File file){
        Friend friend = (Friend) messageEvent.getSubject();
        sendFriendAudioSilk(friend,file);
    }

    public static void sendFriendAudioSilk(Friend friend,File file){
        if (file.getAbsolutePath().endsWith("mp3")){
            file = mp3ToSilk(file);
        }
        if (file==null){
            log.error("sendFriendAudioSilk failed,file is null,may mp3ToSilk failed");
            return;
        }
        ExternalResource resource = ExternalResource.create(file);
        try {
            OfflineAudio audio = friend.uploadAudio(resource);
            friend.sendMessage(audio);
        }finally {
            try {
                resource.close();
            } catch (IOException e) {
                log.error("resource close failed", e);
            }
        }
    }

    public static void sendGroupAudioSilk(MessageEvent messageEvent,File file){
        Group group = (Group) messageEvent.getSubject();
        sendGroupAudioSilk(group,file);
    }

    public static void sendGroupAudioSilk(Group group,File file){
        ExternalResource resource = ExternalResource.create(file);
        try {
            OfflineAudio audio = group.uploadAudio(resource);
            group.sendMessage(audio);
        }finally {
            try {
                resource.close();
            } catch (IOException e) {
                log.error("resource close failed", e);
            }
        }
    }
}
