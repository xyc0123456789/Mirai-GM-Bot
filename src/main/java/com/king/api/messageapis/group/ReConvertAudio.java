package com.king.api.messageapis.group;

import com.king.config.CommonConfig;
import com.king.model.CommonResponse;
import com.king.model.GroupId;
import com.king.model.MessageEventContext;
import com.king.util.AudioUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class ReConvertAudio extends AbstractGroupMessageApi {
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return messageEventContext.getContent().startsWith("#convertaudio");
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        if (messageEvent.getSubject().getId() == GroupId.TEST||messageEvent.getSubject().getId() == GroupId.DEV){
            String fileName = messageEventContext.getContent().substring(13).trim();
            if (fileName.contains(".")){
                fileName = fileName.substring(0,fileName.indexOf("."));
            }

            String[] s = fileName.split("_");
            String dir = s[0];
            File silk = new File(CommonConfig.getWorkingDir() + "download/audio/"+dir+"/"+fileName+".slk");
            if (silk.exists()){
                File mp3File = AudioUtil.silkToMp3(silk);
                if (mp3File!=null) {
//                    FileUtil.deleteFile(silk);
                    Group devGroup = (Group) messageEvent.getSubject();
                    devGroup.getFiles().uploadNewFile(mp3File.getName(), ExternalResource.create(mp3File).toAutoCloseable());
                }
            }else {
                messageEvent.getSubject().sendMessage(fileName + " not exist");
            }
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "audio.reconvert";
    }
}
