package com.king.api.messageapis.group.audio;

import com.king.api.messageapis.group.AbstractGroupMessageApi;
import com.king.component.MyBot;
import com.king.model.CommonResponse;
import com.king.model.GroupId;
import com.king.model.MessageEventContext;
import com.king.model.QQFriendId;
import com.king.util.*;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.OnlineAudio;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.king.db.service.CommandPermissionService.GroupMessagePermissionMap;
import static com.king.db.service.CommandPermissionService.getKeyFromMessageDefault;

@Component
@Slf4j
public class ReSendMp3 extends AbstractGroupMessageApi {

    @Autowired
    private SendBackMp3 sendBackMp3;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return messageEventContext.getOnlineAudio()!=null;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        Group group = (Group) messageEvent.getSubject();
        OnlineAudio audio = messageEventContext.getOnlineAudio();
        String savePath =messageEvent.getSubject().getId() + "_" + DateFormateUtil.getCurYYYYMMDDTHHMMSS()+"_"+messageEvent.getSender().getId()+".slk";
        File silkFile = FileUtil.downLoadImage(audio.getUrlForDownload(), "download/audio/"+messageEvent.getSubject().getId()+"/"+savePath);
        File mp3File = AudioUtil.silkToMp3(silkFile);
//                        FileUtil.deleteFile(file);
        Group devGroup = messageEvent.getBot().getGroup(GroupId.DEV);
        if (devGroup==null) {
            devGroup = messageEvent.getBot().getGroup(GroupId.TEST);
        }
        if (devGroup!=null){
            if (mp3File!=null) {
                devGroup.getFiles().uploadNewFile(mp3File.getName(), ExternalResource.create(mp3File).toAutoCloseable());
            }else {
                devGroup.sendMessage(savePath);
            }
        }
        if (MyBot.bot!=null){
            Friend friend = MyBot.bot.getFriend(QQFriendId.ME);
            if (friend!=null){
                friend.sendMessage(NormalMemberUtil.getNormalMemberStr(group.get(messageEvent.getSender().getId())));
                AudioUtil.sendFriendAudioSilk(friend, silkFile);
            }
        }
        String keyFromMessageDefault = getKeyFromMessageDefault(sendBackMp3.commandName(), messageEventContext);
        Boolean permission = GroupMessagePermissionMap.getOrDefault(keyFromMessageDefault, false);
        if (permission){
            if (mp3File!=null) {
                group.getFiles().uploadNewFile(mp3File.getName(), ExternalResource.create(mp3File).toAutoCloseable());
            }else {
                log.info("upload mp3 failed");
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
        return "audio.resend";
    }

    @Override
    public boolean defaultStatus() {
        return false;
    }
}
