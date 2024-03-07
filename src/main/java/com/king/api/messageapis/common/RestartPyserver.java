package com.king.api.messageapis.common;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.ShellExecutorUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RestartPyserver extends AbstractCommonMessageApi {
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return "#restart".equalsIgnoreCase(messageEventContext.getContent());
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        String message = "failed";
        String stop = ShellExecutorUtil.exe("cd ./pyserver&&./server.sh stop");
        if (stop.contains(") [OK]")) {
            try {Thread.sleep(5000);}catch (Exception ignored){}
            String start = ShellExecutorUtil.exe("cd ./pyserver&&./server.sh start");
            if (start.contains(") [OK]")){
                message = "OK";
            }
        }
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        QuoteReply quoteReply = messageEventContext.getQuoteReply();
        MessageChain messages = new MessageChainBuilder().append(quoteReply).append(message).build();
        messageEvent.getSubject().sendMessage(messages);

        return null;
    }

    @Override
    public int sortedOrder() {
        return 98;
    }

    @Override
    public String commandName() {
        return "pyserver.restart";
    }

    @Override
    public boolean defaultStatus() {
        return false;
    }
}
