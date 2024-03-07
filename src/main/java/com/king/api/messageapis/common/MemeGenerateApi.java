package com.king.api.messageapis.common;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.MemeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description: MemeGenerateApi
 * @author: xyc0123456789
 * @create: 2023/9/29 23:04
 **/
@Slf4j
@Component
public class MemeGenerateApi extends AbstractCommonMessageApi {
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        String content = messageEventContext.getContent();
        return "memes列表".equals(content) || content.startsWith("meme搜索") || MemeUtil.memes(messageEventContext)!=null;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        String content = messageEventContext.getContent();
        if ("memes列表".equals(content)){
            MemeUtil.memesList(messageEventContext);
        } else if (content.startsWith("meme搜索")) {
            MemeUtil.memesSearch(messageEventContext);
        }

        return null;
    }

    @Override
    public int sortedOrder() {
        return 56;
    }

    @Override
    public String commandName() {
        return "message.meme";
    }
}
