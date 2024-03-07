package com.king.util;

import com.king.component.MyBot;
import com.king.config.CommonConfig;
import com.king.db.pojo.Members;
import com.king.db.pojo.MessageRecord;
import com.king.db.service.BotMessageRecordService;
import com.king.db.service.MembersService;
import com.king.db.service.MessageRecordService;
import com.king.model.MessageEventContext;
import com.king.model.QQFriendId;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.event.events.MessageEvent;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import static com.king.config.CommonConfig.MDC_TRACE_ID;

/**
 * @description: 小美
 * @author: xyc0123456789
 * @create: 2023/5/13 21:36
 **/
@Slf4j
@Component
public class NewBotPoeXiaoMei{

    private static MessageRecordService messageRecordService;

    private static MembersService membersService;

    private static BotMessageRecordService botMessageRecordService;

    @Autowired
    public NewBotPoeXiaoMei(MessageRecordService messageRecordService, MembersService membersService, BotMessageRecordService botMessageRecordService){
        NewBotPoeXiaoMei.messageRecordService = messageRecordService;
        NewBotPoeXiaoMei.membersService = membersService;
        NewBotPoeXiaoMei.botMessageRecordService = botMessageRecordService;
    }

    private static final ThreadPoolExecutor threadPool = ThreadPoolExecutorUtil.getAQueueExecutor(10);

    public static String askXiaoMei(String input, String id){
        String mdc = MDC.get(MDC_TRACE_ID);
        String resp = "";
        try {
            Future<String> stringFuture = threadPool.submit(() -> {
                try {
                    CommonConfig.gptKeyLimiter.getByWait();
                    return ask(input, mdc, id);
                } finally {
                    MDC.remove(MDC_TRACE_ID);
                }
            });
            return stringFuture.get();
        } catch (RejectedExecutionException rejectedExecutionException) {
            resp = "小美: 被请求过快";
        } catch (Exception e) {
            log.error("chatGPT execute err", e);
            resp = "小美: " + e.getMessage();
        }
        Friend friend = MyBot.bot.getFriend(QQFriendId.ME);
        if (friend!=null){
            friend.sendMessage(resp);
        }
        return "";
    }


    private static String ask(String input, String mdc, String id) {
        int bot_id = 0;
        try {
            MDC.put(MDC_TRACE_ID, mdc);
            String url = "http://127.0.0.1:5000/chat";
            HttpClient httpClient = new HttpClient(url, 300000, 300000, false);
            Map<String, String> stringObjectMap = new HashMap<>();
            stringObjectMap.put("input", input);
            stringObjectMap.put("id", id);
            stringObjectMap.put("bot_index", String.valueOf(bot_id));
            log.info("xiao mei request:{} {}", bot_id, input);
            int status = httpClient.sendPost(stringObjectMap);
            String result = httpClient.getResult();
            if (result.contains("<!doctype html>") && result.contains("<title>500 Internal Server Error</title>")) {
                log.info("input:{},status:{}", input, status);
                throw new RuntimeException("网络错误，请重试");
            }else if (result.trim().startsWith("HTTPSConnectionPool")){
                throw new RuntimeException("网络代理连接异常，请稍后再试");
            }else if ("Response timed out.".equalsIgnoreCase(result)||"SendMessageMutation failed too many times.".equalsIgnoreCase(result)){
                log.error("Response timed out or SendMessageMutation failed too many times");
                try {Thread.sleep(60000);} catch (InterruptedException ignore) {}
            }
            return result;
        } catch (Exception e) {
            if (e instanceof RuntimeException){
                log.error(e.getMessage());
            }else {
                log.error("", e);
            }
            try {Thread.sleep(60000);} catch (InterruptedException ignore) {}
            return "";
        } finally {
            MDC.remove(MDC_TRACE_ID);
        }
    }


    public static String constructOneLineMessage(MessageEventContext messageEventContext){
        String textContent = messageEventContext.getContent();
        MessageEvent messageEvent = messageEventContext.getMessageEvent();

        long qqId = messageEvent.getSender().getId();
        long groupId = messageEvent.getSubject().getId();
        if (MyStringUtil.isEmpty(textContent)){
            return null;
        }
        Members members = membersService.selectMember(groupId, qqId);
        String nameCard;
        if (members==null){
            Friend friend = MyBot.bot.getFriend(qqId);
            if (friend==null){
                return null;
            }else {
                nameCard = friend.getNick();
            }
        }else {
            nameCard = members.getNameCard();
            if (MyStringUtil.isEmpty(nameCard)){
                nameCard = members.getNickName();
            }
        }
        return "["+qqId+"("+nameCard+")]: "+textContent;
    }

    public static String constructOneLineMessage(MessageRecord record){
        String textContent = record.getTextContent();
        Long qqId = record.getQqId();
        if (MyStringUtil.isEmpty(textContent)){
            return null;
        }
        Members members = membersService.selectMember(record.getGroupId(), qqId);
        String nameCard;
        if (members==null){
            Friend friend = MyBot.bot.getFriend(qqId);
            if (friend==null){
                return null;
            }else {
                nameCard = friend.getNick();
            }
        }else {
            nameCard = members.getNameCard();
            if (MyStringUtil.isEmpty(nameCard)){
                nameCard = members.getNickName();
            }
        }
        return "["+qqId+"("+nameCard+")]: "+textContent;
    }

    public static String constructRequest(List<MessageRecord> messageRecordList){
        StringBuilder stringBuilder = new StringBuilder();
        for (MessageRecord record: messageRecordList){
            String message = constructOneLineMessage(record);
            if (!MyStringUtil.isEmpty(message)) {
                stringBuilder.append(message).append("\n\n");
            }
        }
        return stringBuilder.toString().trim();
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String ask = askXiaoMei("[125(小明)]我叫什么？小红能@一下我吗\n[125(小红)]小明你真垃圾！", "0");
    }


}
