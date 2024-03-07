package com.king.api.messageapis.group;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.GroupJudgeUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.data.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 撤回群id
 * @author: xyc0123456789
 * @create: 2023/6/17 13:55
 **/
@Slf4j
@Component
public class RecallGroupId extends AbstractGroupMessageApi{
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return !CollectionUtils.isEmpty(getArray(messageEventContext.getContent()));
    }

    private static List<Long> getArray(String str){
        String pattern = "\\d{5,}"; // 匹配大于等于6位的数字

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        ArrayList<Long> matches = new ArrayList<>();
        while (m.find()) {
            matches.add(Long.parseLong(m.group()));
        }
        return matches;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        List<Long> array = getArray(messageEventContext.getContent());
        for (Long groupId: array){
            boolean exist = GroupJudgeUtil.judgeGroupIdExist(groupId);
            if (exist){
                try {
                    MessageSource.recall(messageEventContext.getMessageSource());
                }catch (Exception ignored){}
                break;
            }
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 16;
    }

    @Override
    public String commandName() {
        return "message.groupId.recall";
    }

    public static void main(String[] args) {
        List<Long> abc12345def67890xyz = getArray("abc12345def67890xyz");
        System.out.println(abc12345def67890xyz);
    }
}
