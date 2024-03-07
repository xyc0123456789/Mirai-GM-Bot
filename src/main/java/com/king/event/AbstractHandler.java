package com.king.event;

import com.king.config.CommonConfig;
import com.king.model.MessageEventContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;
import org.slf4j.MDC;

import java.util.ArrayList;

@Slf4j
public abstract class AbstractHandler extends SimpleListenerHost {

    public static MessageSource getMessageSource(MessageEvent messageEvent){
        for (Message message:messageEvent.getMessage()){
            if (message instanceof MessageSource){
                return (MessageSource) message;
            }
        }
        return null;
    }

    public static MessageEventContext constructFriendContext(MessageEvent messageEvent){
        MessageEventContext messageEventContext = constructGroupContext(messageEvent);
        messageEventContext.setGroup(false);
        return messageEventContext;
    }
    public static MessageEventContext constructGroupContext(MessageEvent messageEvent){
        StringBuilder content = new StringBuilder();
        ArrayList<Image> images = new ArrayList<>();
        ArrayList<At> atList = new ArrayList<>();
        OnlineAudio audio=null;
        RichMessage richMessage = null;
        int count = 0;
        for (SingleMessage singleMessage:messageEvent.getMessage()){
            try {
                if (singleMessage instanceof PlainText){
                    String plainText = ((PlainText) singleMessage).getContent();
                    if (count==0){
                        content.append(plainText);
                    }else {
                        content.append(plainText);
                    }
                    count++;
                }
                if (singleMessage instanceof Image){
                    Image image = (Image) singleMessage;
                    String queryUrl = Image.queryUrl(image);
                    images.add(image);
                    log.info("seriCode:{},imageType:{},imageId:{},url:{}",image.serializeToMiraiCode(),image.getImageType(),image.getImageId(),queryUrl);
                }
                if (singleMessage instanceof OnlineAudio){
                    audio = (OnlineAudio) singleMessage;
                }
                if (singleMessage instanceof At){
                    atList.add((At) singleMessage);
                }
                if (singleMessage instanceof RichMessage){
                    richMessage = (RichMessage) singleMessage;
                }
            }catch (Exception e){
                log.error("singleMessage处理失败", e);
            }
        }
        MessageEventContext messageEventContext = new MessageEventContext();
        messageEventContext.setTxtCount(count);
        messageEventContext.setMessageEvent(messageEvent);
        messageEventContext.setContent(content.toString().trim());
        messageEventContext.setMessageSource(getMessageSource(messageEvent));
        messageEventContext.setQuoteReply(MessageSource.quote(messageEvent.getMessage()));
        messageEventContext.setTraceName(MDC.get(CommonConfig.MDC_TRACE_ID));
        messageEventContext.setImages(images);
        messageEventContext.setOnlineAudio(audio);
        messageEventContext.setAtList(atList);
        messageEventContext.setRichMessage(richMessage);
        return messageEventContext;
    }

}
