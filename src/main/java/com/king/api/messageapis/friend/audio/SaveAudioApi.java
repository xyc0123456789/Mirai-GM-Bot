package com.king.api.messageapis.friend.audio;

import com.king.api.messageapis.friend.AbstractFriendMessageApi;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.AudioUtil;
import com.king.util.DateFormateUtil;
import com.king.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.OnlineAudio;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;

@Component
@Slf4j
public class SaveAudioApi extends AbstractFriendMessageApi {
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return messageEventContext.getOnlineAudio()!=null;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        OnlineAudio audio = messageEventContext.getOnlineAudio();
        String savePath = messageEvent.getSender().getId() + "_" + DateFormateUtil.formatYYYYMMDDTHHMMSS(new Date())+".slk";
        File file = FileUtil.downLoadImage(audio.getUrlForDownload(), "download/audio/"+savePath);
        File mp3File = AudioUtil.silkToMp3(file);
        File silkFile = AudioUtil.mp3ToSilk(mp3File);
        AudioUtil.sendFriendAudioSilk(messageEvent, silkFile);
//        FileUtil.deleteFile(silkFile);
        return null;
    }

    @Override
    public int sortedOrder() {
        return 50;
    }

    @Override
    public String commandName() {
        return "audio.save";
    }
}
