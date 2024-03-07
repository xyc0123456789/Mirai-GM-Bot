package com.king.api.messageapis.common;

import com.king.config.CommonConfig;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.model.QQFriendId;
import com.king.util.PythonServerRequest;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.king.api.messageapis.common.ChatGPTRequest.getCommonResponse;
import static com.king.config.CommonConfig.botManager;


@Slf4j
@Component
public class ChatGPTRequestBan extends AbstractCommonMessageApi {
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
//        String content = messageEventContext.getContent();
//        boolean banCondition = "#ban".equalsIgnoreCase(content);
//        if (!banCondition){
//            return false;
//        }
//        return isManagerOrMe(messageEventContext);
        return false;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        Contact subject = messageEvent.getSubject();
        QuoteReply quoteReply = messageEventContext.getQuoteReply();
        StringBuffer answer = new StringBuffer();
        answer.append("prompt:").append(CommonConfig.prompt).append("\\\n");
        List<Thread> threadList = new ArrayList<>();
        for (int i=0;i<botManager.getSize();i++){
            int tmpId = i;
            Thread thread = new Thread(()->{
                String gpt;
                try {
                    gpt = PythonServerRequest.chatGPT(CommonConfig.prompt, String.valueOf(subject.getId()), tmpId);
                } catch (Exception e) {
                    log.error("ban err", e);
                    gpt = e.getMessage();
                }
                answer.append("[bot ").append(tmpId).append("]:").append(gpt).append("\\\n");
            });
            thread.start();
            threadList.add(thread);
        }
        for (Thread thread:threadList){
            try {
                thread.join();
            } catch (InterruptedException e) {
                log.error("thread join err", e);
            }
        }
        return getCommonResponse(messageEvent, quoteReply, answer.toString());
    }



    @Override
    public int sortedOrder() {
        return 50;
    }

    @Override
    public String commandName() {
        return "chatgpt.ban";
    }
}
