package com.king.api.messageapis.common;

import com.king.model.ChatGPTResponse;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.model.RolyPlayPresetText;
import com.king.util.PythonServerRequest;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

import static com.king.api.messageapis.common.ChatGPTRequest.getCommonResponse;
import static com.king.api.messageapis.common.ChatGPTRequest.getPreProcess;
import static com.king.model.RolyPlayPresetText.RolyMap;
import static com.king.model.RolyPlayPresetText.justOne;


@Slf4j
@Component
public class ChatGPTRolePlayRequest extends AbstractCommonMessageApi {
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
//        return messageEventContext.getContent().startsWith("#preset");
        return false;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        QuoteReply quoteReply = messageEventContext.getQuoteReply();
        String content = messageEventContext.getContent();
        String answer="请求格式错误, 请输入”#preset+角色名“。角色列表："+RolyMap.keySet();

        String idStr = String.valueOf(messageEvent.getSubject().getId());

        ChatGPTResponse response = getPreProcess(content, 7);
        String rolyPlayText = null;
        String roly = response.getRequestContent().trim();
        boolean oneReq = false;
        for (String key:RolyMap.keySet()){
            if (key.equalsIgnoreCase(roly)){
                rolyPlayText = RolyMap.get(key);
                oneReq = justOne.containsKey(key);
                break;
            }
        }

        if (rolyPlayText!=null && response.isOk()){
            int botIndex = response.getBotIndex();

            try {
                if (oneReq){
                    Future<String> command0 = PythonServerRequest.chatGPTSubmit("#reset", idStr, botIndex);
                    String res = command0.get();
                    log.info("#reset: {}", res);
                    Future<String> command1 = PythonServerRequest.chatGPTSubmit(rolyPlayText, idStr, botIndex);
                    answer = command1.get();
                }else {
                    StringBuilder stringBuilder = new StringBuilder();
                    Future<String> command0 = PythonServerRequest.chatGPTSubmit("#reset", idStr, botIndex);
                    Future<String> command1 = PythonServerRequest.chatGPTSubmit(RolyPlayPresetText.CommonStart1, idStr, botIndex);
                    Future<String> command2 = PythonServerRequest.chatGPTSubmit(RolyPlayPresetText.CommonStart2, idStr, botIndex);
                    Future<String> command3 = PythonServerRequest.chatGPTSubmit(rolyPlayText, idStr, botIndex);
                    String res = command0.get();
                    log.info("#reset: {}", res);
                    String answer1 = command1.get();
                    getCommonResponse(messageEvent, quoteReply, answer1, response.isMath());
                    String answer2 = command2.get();
                    String answer3 = command3.get();
                    stringBuilder.append("you: ").append(RolyPlayPresetText.CommonStart2).append("\\\n");
                    stringBuilder.append("ChatGPT: ").append(answer2).append("\\\n");
                    stringBuilder.append("you: ").append(rolyPlayText).append("\\\n");
                    stringBuilder.append("ChatGPT: ").append(answer3);
                    answer = stringBuilder.toString();
                }
            } catch (Exception e) {
                log.error("ChatGPTRolePlayRequest err", e);
                answer = "加载角色异常: " + e.getMessage();
            }
        }
        getCommonResponse(messageEvent, quoteReply, answer, response.isMath());
        return null;
    }

    @Override
    public int sortedOrder() {
        return 53;
    }

    @Override
    public String commandName() {
        return "chatgpt.rolyplay";
    }
}
