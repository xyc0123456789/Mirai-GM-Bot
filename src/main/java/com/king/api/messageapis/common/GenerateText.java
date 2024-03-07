package com.king.api.messageapis.common;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.PythonServerRequest;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GenerateText extends AbstractCommonMessageApi {
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        String content = messageEventContext.getContent();
        return content.startsWith("#文本生成");
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        try {
            String content = messageEventContext.getContent();
            MessageEvent messageEvent = messageEventContext.getMessageEvent();
            String substring = content.substring(6);
            int i = substring.indexOf(" ");
            String length = "50";
            String input = substring;
            if (i!=-1){
                length = substring.substring(0,i).trim();
                try {
                    long l = Long.parseLong(length);
                    input = substring.substring(i+1);
                }catch (Exception e) {
                    length = "50";
                }
            }

            String response;
            try {
                response = PythonServerRequest.textGenerator(input, length);
            }catch (Exception e){
                response = "生成失败";
            }
            QuoteReply quoteReply = messageEventContext.getQuoteReply();
            MessageChain messages = new MessageChainBuilder().append(quoteReply).append(response).build();
            messageEvent.getSubject().sendMessage(messages);
        }catch (Exception e){
            log.error("", e);
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 30;
    }

    @Override
    public String commandName() {
        return "generate.text";
    }

    @Override
    public boolean defaultStatus() {
        return false;
    }
}
