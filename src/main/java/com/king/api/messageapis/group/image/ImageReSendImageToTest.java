package com.king.api.messageapis.group.image;

import com.king.api.messageapis.group.AbstractGroupMessageApi;
import com.king.component.MyBot;
import com.king.model.CommonResponse;
import com.king.model.GroupId;
import com.king.model.MessageEventContext;
import com.king.util.DateFormateUtil;
import com.king.util.NormalMemberUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.message.data.Image;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Slf4j
public class ImageReSendImageToTest extends AbstractGroupMessageApi {
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return true;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {

        Group testGroup = MyBot.bot.getGroup(GroupId.DEV);
        if (testGroup==null){
            return null;
        }

        long id = messageEventContext.getMessageEvent().getSender().getId();
        Group group = (Group) messageEventContext.getMessageEvent().getSubject();
        NormalMember normalMember = group.get(id);

        String curYYYYMMDDTHHMMSS = DateFormateUtil.getCurYYYYMMDDTHHMMSS();
        String normalMemberStr = NormalMemberUtil.getNormalMemberStr(normalMember);
        String groupStr = NormalMemberUtil.getGroupStr(group);

        ArrayList<Image> images = messageEventContext.getImages();
        for (Image img:images) {
            testGroup.sendMessage(curYYYYMMDDTHHMMSS + groupStr + normalMemberStr + ": " + Image.queryUrl(img));
        }

        return null;
    }

    @Override
    public int sortedOrder() {
        return 0;
    }

    @Override
    public String commandName() {
        return "image.resend";
    }

    @Override
    public boolean defaultStatus() {
        return false;
    }
}
