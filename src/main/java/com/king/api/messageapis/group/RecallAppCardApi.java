package com.king.api.messageapis.group;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.data.LightApp;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.RichMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description: richMessage
 * @author: xyc0123456789
 * @create: 2023/6/17 10:37
 **/
@Slf4j
@Component
public class RecallAppCardApi extends AbstractGroupMessageApi{
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return messageEventContext.getRichMessage()!=null;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        RichMessage richMessage = messageEventContext.getRichMessage();
        if (richMessage instanceof LightApp){
            String jsonContent = richMessage.getContent();
            Map map = JsonUtil.fromJson(jsonContent, Map.class);
            String prompt = (String) map.get("prompt");
            Map<String,Object> meta = (Map<String, Object>) map.get("meta");
            Map<String,Object> contact = (Map<String, Object>) meta.get("contact");
            String jumpUrl = (String) contact.get("jumpUrl");
            String tag = (String) contact.get("tag");
            log.info("prompt:{},jumpUrl:{},tag:{}",prompt, jumpUrl, tag);
            if ("推荐群聊".equals(prompt)){
                log.info("因为群聊推荐被撤回: {}", messageEventContext.getMessageEvent().getMessage().serializeToMiraiCode());
                MessageSource.recall(messageEventContext.getMessageSource());
            }
        }

        return null;
    }

    @Override
    public int sortedOrder() {
        return 15;
    }

    @Override
    public String commandName() {
        return "message.app.recall";
    }
}
