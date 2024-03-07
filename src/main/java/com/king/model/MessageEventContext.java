package com.king.model;

import lombok.Data;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

@Data
public class MessageEventContext extends CommonEventContext {

    public MessageEventContext() {
    }

    public MessageEventContext(String content, int txtCount, ArrayList<Image> images, MessageEvent messageEvent, MessageSource messageSource) {
        this.content = content;
        this.txtCount = txtCount;
        this.images = images;
        this.messageEvent = messageEvent;
        this.messageSource = messageSource;
    }

    private String content;
    private int txtCount;
    private ArrayList<Image> images;
    private ArrayList<At> atList;
    private OnlineAudio onlineAudio;
    private MessageEvent messageEvent;
    private MessageSource messageSource;

    private QuoteReply quoteReply;

    private boolean isGroup = true;

    private Map<String, Object> payLoad = new Hashtable<>();

    private RichMessage richMessage;

}
