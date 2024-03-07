package com.king.api.messageapis.common;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.model.GroupId;
import com.king.util.PythonServerRequest;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.MessageSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class AniOffensive extends AbstractCommonMessageApi {

    private static final Set<Long> groups = new HashSet<>();
    static {
        groups.add(GroupId.TEST);//测试
        groups.add(GroupId.DEV);//开发测试
    }
    
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        long id = messageEventContext.getMessageEvent().getSubject().getId();
        return false;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        try {
            MessageEvent messageEvent = messageEventContext.getMessageEvent();
            String content = messageEventContext.getContent();
            MessageSource messageSource = messageEventContext.getMessageSource();

            String antiOffensiveResponse=null,filterSentiResponse=null;
            try {
                antiOffensiveResponse = PythonServerRequest.antiOffensive(content);
                filterSentiResponse = PythonServerRequest.filterSenti(content);
                log.info("{{}} 评分:antiOffensiveResponse {} filterSentiResponse {}",content, antiOffensiveResponse, filterSentiResponse);
            }catch (Exception e){
                if (e instanceof IOException||e.getMessage().contains("Connection refused")){
                    log.error("敏感词请求python失败,{}", e.getMessage());
                }else {
                    log.error("敏感词请求python失败", e);
                }
            }
            boolean antiFlag = "1".equals(antiOffensiveResponse);
            boolean filtFlag = "1".equals(filterSentiResponse);
            if (antiFlag ||filtFlag){
                try {
                    log.info("{}因为 antiOffensive|filterSenti 被撤回了",content);
                    At at = new At(messageEvent.getSender().getId());
                    MessageChain messageChain = new MessageChainBuilder().append(at).append("消息因包含歧视内容被撤回 anti["+antiOffensiveResponse+"]filt["+filterSentiResponse+"]").build();
                    ((Group) messageEvent.getSubject()).sendMessage(messageChain);
//                    MessageSource.recall(messageSource);
                }catch (Exception e){
                    if (!e.getMessage().contains("had already been recalled")){
                        log.error("撤回{"+content+"}失败", e);
                    }
                }

            }
        }catch (Exception e){
            log.error("", e);
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 50;
    }

    @Override
    public String commandName() {
        return "anti_offensive";
    }

    @Override
    public boolean defaultStatus() {
        return false;
    }
}
