package com.king.api.messageapis.friend;

import com.king.db.pojo.Members;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.DateFormateUtil;
import com.king.util.NormalMemberUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class DetectForceApi extends AbstractFriendMessageApi {
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return messageEventContext.getContent().startsWith("#detect");
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        String content = messageEventContext.getContent();
        String substring = content.substring(7, 8);
        String groupId = content.substring(8).trim();
        Date offset;
        if ("f".equals(substring)) {
            offset = DateFormateUtil.getOffsetDate000000(1000);
        } else if ("n".equals(substring)) {
            offset = new Date();
        }else {
            offset = DateFormateUtil.getOffsetDate000000(1);
            groupId = content.substring(7).trim();
        }
        long groupIdLong = -1;
        try {
            groupIdLong = Long.parseLong(groupId);
        }catch (Exception ignored){}

        if (groupIdLong == -1){
            messageEventContext.getMessageEvent().getSubject().sendMessage("格式错误");
            return null;
        }

        List<Long> longList = membersService.detectMembers(groupIdLong, offset);
        if (CollectionUtils.isEmpty(longList)){
            messageEventContext.getMessageEvent().getSubject().sendMessage("未找到群或者都有头衔");
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Long l:longList){
            Members members = membersService.selectMember(groupIdLong, l);
            stringBuilder.append(NormalMemberUtil.getNormalMemberStr(members)).append(" , ");
        }
        stringBuilder.append("\n以上成员没有头衔");
        messageEventContext.getMessageEvent().getSubject().sendMessage(stringBuilder.toString());

        return null;
    }

    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "detect.other.group";
    }
}
