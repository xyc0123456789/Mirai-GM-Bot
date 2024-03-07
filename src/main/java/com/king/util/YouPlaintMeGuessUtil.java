package com.king.util;

import com.king.model.MessageEventContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.king.util.MyStringUtil.getSubString;

/**
 * @description: 我画你猜工具
 * @author: xyc0123456789
 * @create: 2023/5/10 20:42
 **/
@Slf4j
@Component
public class YouPlaintMeGuessUtil {

    //1、群友私聊机器人，我们开启我画你猜 put()
    //2、群友猜中了 guess()
    //3、超时处理

    private static final String UUIDYouPlaintMeGuess = "UUIDYouPlaintMeGuess";

    private static final String GuessKey = "GuessKey";

    private static final String EarlyStop = "EarlyStop";

    private static final String NewContext = "NewContext";

    private static final String StartKey = "我画你猜";

    private static final TimeoutCache<String, MessageEventContext> timeoutCache = new TimeoutCache<>();

    private static final TimeoutCache.TimeoutCallback<MessageEventContext> callback = new TimeoutCache.TimeoutCallback<MessageEventContext>() {
        @Override
        public void onTimeout(MessageEventContext messageEventContext) {
            Map<String, Object> payLoad = messageEventContext.getPayLoad();
            String uuid = (String) payLoad.get(UUIDYouPlaintMeGuess);
            String answer = (String) payLoad.get(GuessKey);
            Group group = getGroup(messageEventContext);
            MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
            messageChainBuilder.append("uuid:").append(uuid).append(" 时间到！公布我画你猜答案: ").append(answer);
            group.sendMessage(messageChainBuilder.build());
        }

        @Override
        public void onAccess(MessageEventContext messageEventContext) {
            Map<String, Object> payLoad = messageEventContext.getPayLoad();
            String uuid = (String) payLoad.get(UUIDYouPlaintMeGuess);
            String answer = (String) payLoad.get(GuessKey);
            Group group = getGroup(messageEventContext);
            MessageEventContext newContext = (MessageEventContext) payLoad.get(NewContext);
            long id = messageEventContext.getMessageEvent().getSender().getId();
            QuoteReply quoteReply = newContext.getQuoteReply();
            boolean isEarlyStop = "true".equals(payLoad.get(EarlyStop));

            MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
            if (isEarlyStop) {
                messageChainBuilder.append(quoteReply).append(new At(id)).append("提前公布uuid:").append(uuid).append(" 的答案: ").append(answer);
            } else {
                messageChainBuilder.append(quoteReply).append(new At(id)).append("恭喜这位被回复的群友猜出了uuid:").append(uuid).append(" 的答案: ").append(answer);
            }
            group.sendMessage(messageChainBuilder.build());
        }
    };

    public static String getYouPlaintMeGuessKey(MessageEventContext messageEventContext, int commandLen) {
        String content = getSubString(messageEventContext, commandLen);
        if (messageEventContext.isGroup()) {
            Group group = getGroup(messageEventContext);
            long groupId = group.getId();
            return groupId + "-" + content;
        }
        return null;
    }

    public static Group getGroup(MessageEventContext messageEventContext) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        Group group;
        if (messageEvent instanceof GroupTempMessageEvent) {
            GroupTempMessageEvent groupTempMessageEvent = (GroupTempMessageEvent) messageEvent;
            group = groupTempMessageEvent.getGroup();
        } else {
            group = (Group) messageEvent.getSubject();
        }
        return group;
    }


    public static boolean put(MessageEventContext messageEventContext) {
        if (!messageEventContext.getContent().startsWith(StartKey)) {
            return false;
        }
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        String answer = getSubString(messageEventContext, 4);
        if (MyStringUtil.isEmpty(answer)){
            messageEvent.getSubject().sendMessage("\"我画你猜 GuessKey\" GuessKey不能为空");
            return true;
        }
        Map<String, Object> payLoad = messageEventContext.getPayLoad();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        payLoad.put(UUIDYouPlaintMeGuess, uuid);
        payLoad.put(GuessKey, answer);
        Group group = getGroup(messageEventContext);
        long id = messageEvent.getSender().getId();
        String key = group.getId() + "-" + answer;

        timeoutCache.put(key, messageEventContext, 600000, callback);

        //发送开启通知
        MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
        messageChainBuilder.append(new At(id)).append("开启了我画你猜, uuid:").append(uuid).append("\n");
        messageChainBuilder.append("提前终止可以使用 #whncstop ").append(uuid);
        group.sendMessage(messageChainBuilder.build());
        messageEvent.getSubject().sendMessage(answer);
        return true;
    }


    public static void guess(MessageEventContext messageEventContext) {
        String youPlaintMeGuessKey = getYouPlaintMeGuessKey(messageEventContext, 0);
        if (MyStringUtil.isEmpty(youPlaintMeGuessKey)) {
            return;
        }
        MessageEventContext oriEventContext = timeoutCache.read(youPlaintMeGuessKey);
        if (oriEventContext != null) {
            Map<String, Object> payLoad = oriEventContext.getPayLoad();
            if (!payLoad.containsKey(NewContext)) {
                payLoad.put(EarlyStop, "false");
                payLoad.put(NewContext, messageEventContext);
            }
        }
        timeoutCache.get(youPlaintMeGuessKey);
    }

    public static void stop(MessageEventContext messageEventContext) {
        if (!messageEventContext.getContent().startsWith("#whncstop")) {
            return;
        }
        String uuid = getSubString(messageEventContext, 9);
        if (MyStringUtil.isEmpty(uuid)) {
            return;
        }
        List<MessageEventContext> uuids = timeoutCache.getUuids();
        String youPlaintMeGuessKey = null;
        for (MessageEventContext oriEventContext : uuids) {
            if (oriEventContext != null) {
                Map<String, Object> payLoad = oriEventContext.getPayLoad();
                if (uuid.equals(payLoad.get(UUIDYouPlaintMeGuess))) {
                    payLoad.put(EarlyStop, "true");
                    payLoad.put(NewContext, messageEventContext);
                    youPlaintMeGuessKey = (String) payLoad.get(GuessKey);
                    break;
                }
            }
        }

        if (youPlaintMeGuessKey == null) {
            MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
            messageChainBuilder.append(messageEventContext.getQuoteReply()).append("无效的uuid");
            messageEventContext.getMessageEvent().getSubject().sendMessage(messageChainBuilder.build());
        } else {
            Group group = getGroup(messageEventContext);
            String newKey = group.getId() + "-" + youPlaintMeGuessKey;
            MessageEventContext context = timeoutCache.get(newKey);
            if (context==null) {
                MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
                messageChainBuilder.append(messageEventContext.getQuoteReply()).append("无效的uuid");
                messageEventContext.getMessageEvent().getSubject().sendMessage(messageChainBuilder.build());
            }
        }
    }

    public static boolean status(MessageEventContext messageEventContext) {
        if (!messageEventContext.getContent().startsWith("#whncstatus")) {
            return false;
        }

        List<MessageEventContext> uuids = timeoutCache.getUuids();
        Group group = getGroup(messageEventContext);
        ArrayList<String> ans = new ArrayList<>();
        for (MessageEventContext context : uuids) {
            if (getGroup(context).getId() == group.getId()) {
                String s = (String) context.getPayLoad().get(UUIDYouPlaintMeGuess);
                if (!MyStringUtil.isEmpty(s)) {
                    ans.add(s);
                }
            }
        }
        MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
        messageChainBuilder.append(messageEventContext.getQuoteReply()).append("当前我画你猜剩余任务:").append(ans.toString());
        messageEventContext.getMessageEvent().getSubject().sendMessage(messageChainBuilder.build());
        return true;
    }
}
