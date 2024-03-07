package com.king.api.messageapis.group;

import com.king.config.CommonConfig;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.DateFormateUtil;
import com.king.util.FileUtil;
import com.king.util.MyStringUtil;
import com.king.util.ThreadPoolExecutorUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;
import java.util.concurrent.ThreadPoolExecutor;

import static com.king.config.CommonConfig.redisFlag;

/**
 * 记录所有message到Redis，保存时间24h，并用线程池向数据库修改计数
 *
 */
@Slf4j
@Component
public class MessageContentSaveApi extends AbstractGroupMessageApi{

    private static final ThreadPoolExecutor writePool = ThreadPoolExecutorUtil.getAQueueExecutor(100);

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        String content = messageEventContext.getContent();
        return !MyStringUtil.isEmpty(content)&&!CommonConfig.VIDEO_MESSAGE.equals(content);
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        String groupId = String.valueOf(messageEvent.getSubject().getId());
        String content = messageEventContext.getContent()+"\n";
        String txtFile = DateFormateUtil.formatYYYYMMDD(new Date()) + "_" + groupId + ".txt";
        writePool.submit(()->{
            FileUtil.appendToFile(content, new File(txtFile));
        });
        return null;
    }




    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "message.content.save";
    }

    @Override
    public boolean defaultStatus() {
        return false;
    }
}
