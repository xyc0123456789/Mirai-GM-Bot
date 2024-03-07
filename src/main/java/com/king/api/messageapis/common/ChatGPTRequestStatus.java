package com.king.api.messageapis.common;

import com.king.model.BotStatusDTO;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.king.api.messageapis.common.ChatGPTRequest.getCommonResponse;
import static com.king.config.CommonConfig.botManager;


@Slf4j
@Component
public class ChatGPTRequestStatus extends AbstractCommonMessageApi {
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return false;
//        return "#botstatus".equalsIgnoreCase(messageEventContext.getContent());
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        QuoteReply quoteReply = messageEventContext.getQuoteReply();
        StringBuilder answer = new StringBuilder();
        List<BotStatusDTO> botStatusDTOList = botManager.getBotStatusDTOList();
        for (int i=0;i<botManager.getSize();i++){
            BotStatusDTO botStatusDTO = botStatusDTOList.get(i);
            answer.append("[bot ").append(i).append("] 剩余请求数: ").append(botStatusDTO.getRemainRequest().get())
                    .append("个。 离上次失败时间:").append(botStatusDTO.getDeltaSeconds()).append("秒").append("\\\n");
        }
        return getCommonResponse(messageEvent, quoteReply, answer.toString());
    }



    @Override
    public int sortedOrder() {
        return 20;
    }

    @Override
    public String commandName() {
        return "chatgpt.botstatus";
    }
}
