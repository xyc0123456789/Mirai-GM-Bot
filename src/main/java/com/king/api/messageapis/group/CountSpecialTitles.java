package com.king.api.messageapis.group;

import com.king.db.pojo.Members;
import com.king.db.service.MembersService;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.DateFormateUtil;
import com.king.util.GeneratePieHtmlUtil;
import com.king.util.GroupMembersUtil;
import com.king.util.MyStringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

/**
 * @description: 头衔统计
 * @author: xyc0123456789
 * @create: 2023/8/12 17:38
 **/
@Slf4j
@Component
public class CountSpecialTitles extends AbstractGroupMessageApi {

    @Autowired
    private MembersService membersService;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return "#sptitlecount".equalsIgnoreCase(messageEventContext.getContent());
    }

    @Data
    private static class SortObj{
        public SortObj(String key) {
            this.key = key;
            this.count = 0;
        }

        private String key;
        private Integer count;

        public SortObj add(){
            count = count +1;
            return this;
        }
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        Group group = (Group) messageEventContext.getMessageEvent().getSubject();
        List<Members> allEnableList = membersService.getAllEnable(group.getId());
        Map<String, SortObj> contentInt = new HashMap<>();
        for (Members members : allEnableList) {
            String specialTitle = members.getSpecialTitle();
            if (specialTitle == null) {
                specialTitle = "";
            }
            if (specialTitle.equals("")){
                specialTitle = "--无头衔--";
            }
            contentInt.put(specialTitle, contentInt.getOrDefault(specialTitle, new SortObj(specialTitle)).add());
        }
        // 获取 sortMap 的 values 集合
        List<SortObj> sortList = new ArrayList<>(contentInt.values());

        // 使用比较器对 sortList 进行降序排序
        sortList.sort((obj1, obj2) -> obj2.getCount().compareTo(obj1.getCount()));
        Map<String, String> content = new TreeMap<>();
        int i = 0;
        for (SortObj obj: sortList){
            content.put(MyStringUtil.getThreeStr(i++) + ". " + obj.getKey(), String.valueOf(obj.getCount()));
        }
        File pieImg = GeneratePieHtmlUtil.generatePieImg(content, DateFormateUtil.formatYYYYMMDD(new Date()) + " 头衔统计", "总计人数: " + allEnableList.size());
        GroupMembersUtil.sendImgFromString(group, pieImg, messageEventContext.getQuoteReply());
        return null;
    }

    @Override
    public int sortedOrder() {
        return 95;
    }

    @Override
    public String commandName() {
        return "special.title.count";
    }
}
