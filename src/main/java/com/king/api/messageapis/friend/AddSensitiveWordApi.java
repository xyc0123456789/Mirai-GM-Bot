package com.king.api.messageapis.friend;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.DFAUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AddSensitiveWordApi extends AbstractFriendMessageApi{

    @Autowired
    private DFAUtil dfaUtil;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return messageEventContext.getContent().startsWith("#add");
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        String content = messageEventContext.getContent();
        String word = content.substring(4).trim();
        String result = dfaUtil.addWord(word);
        messageEventContext.getMessageEvent().getSubject().sendMessage(result);
        return null;
    }

    @Override
    public int sortedOrder() {
        return 98;
    }

    @Override
    public String commandName() {
        return "sensitive.word.add";
    }
}
