package com.king.api.messageapis.group.image;

import com.king.api.messageapis.group.AbstractGroupMessageApi;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.MyStringUtil;
import com.king.util.NormalMemberUtil;
import com.king.util.QrcodeUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Slf4j
public class ImageQrcodeDetect extends AbstractGroupMessageApi {
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return true;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        ArrayList<Image> images = messageEventContext.getImages();
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        for (Image image:images){
            String detect = QrcodeUtil.detectQrcode(image);
            if (!MyStringUtil.isEmpty(detect)){
                log.info(messageEvent.getMessage().serializeToMiraiCode()+" has qrcode");
                try {
                    MessageSource.recall(messageEventContext.getMessageSource());
                    messageEvent.getSubject().sendMessage("检测到二维码, 发送人:"+ NormalMemberUtil.getNormalMemberStr(messageEvent));
                }catch (Exception e){
                    log.error("QrcodeDetect recall err: {}", e.getMessage());
                }
                break;
            }
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 1;
    }

    @Override
    public String commandName() {
        return "image.qrcode.detect";
    }
}
