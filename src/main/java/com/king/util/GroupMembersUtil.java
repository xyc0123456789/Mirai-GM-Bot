package com.king.util;

import com.king.db.pojo.Members;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.data.UserProfile;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class GroupMembersUtil {

    public static final List<String> TITLE=new ArrayList<String>(){{
        Collections.addAll(this, "qq号", "昵称", "群名片", "群头衔", "入群时间", "最后发言时间", "年龄", "性别", "邮箱", "个性签名", "level");
    }};

    public static ExcelData getMembers(Group group){
        ExcelData excelData = new ExcelData();
        excelData.setTitles(TITLE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        excelData.setRows(new ArrayList<>());
        for (NormalMember normalMember:group.getMembers()){
            UserProfile userProfile = normalMember.queryProfile();
            String nameCard = normalMember.getNameCard();//群名片
            String specialTitle = normalMember.getSpecialTitle();//群头衔

            long id = normalMember.getId();
            int joinTimestamp = normalMember.getJoinTimestamp();
            Date joinDate = new Date(joinTimestamp* 1000L);
            String joinDateStr = simpleDateFormat.format(joinDate);

            int lastSpeakTimestamp = normalMember.getLastSpeakTimestamp();
            Date lastSpeakDate = new Date(lastSpeakTimestamp* 1000L);
            String lastSpeakDateStr = simpleDateFormat.format(lastSpeakDate);

            int age = userProfile.getAge();
            String email = userProfile.getEmail();
            String nickname = userProfile.getNickname();
            String sex = userProfile.getSex().name();
            int qLevel = userProfile.getQLevel();
            String sign = userProfile.getSign();

            Object[] row = new Object[]{id,nickname,nameCard,specialTitle,joinDateStr,lastSpeakDateStr, age,sex,email,sign, qLevel};
            List<Object> list = new ArrayList<>();
            Collections.addAll(list, row);
            excelData.getRows().add(list);
        }

        return excelData;
    }


    public static ExcelData getMembers(List<Members> groupList){
        ExcelData excelData = new ExcelData();
        excelData.setTitles(TITLE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        excelData.setRows(new ArrayList<>());
        for (Members members:groupList){
            if (!members.getEnable()){
                continue;
            }
            String nameCard = members.getNameCard();//群名片
            String specialTitle = members.getSpecialTitle();//群头衔

            long id = members.getQqId();

            String joinDateStr="";
            if (members.getJoinDate()!=null) {
                joinDateStr = simpleDateFormat.format(members.getJoinDate());
            }
            String lastSpeakDateStr="";
            if (members.getLastSpeakDate()!=null) {
                lastSpeakDateStr = simpleDateFormat.format(members.getLastSpeakDate());
            }

            int age = members.getAge();
            String email = members.getEmail();
            String nickname = members.getNickName();
            String sex = members.getSex();
            int qLevel = members.getQqLevel();
            String sign = members.getSign();

            Object[] row = new Object[]{id,nickname,nameCard,specialTitle,joinDateStr,lastSpeakDateStr, age,sex,email,sign, qLevel};
            List<Object> list = new ArrayList<>();
            Collections.addAll(list, row);
            excelData.getRows().add(list);
        }
        return excelData;
    }

    public static void sendImgFromString(Contact contact, String message, QuoteReply quoteReply){
        File tmpImg = CommonMarkUtil.createMarkdownImg(message, false);
        sendImgFromString(contact, tmpImg, quoteReply);
    }

    public static void sendImgFromString(Contact contact, File tmpImg, QuoteReply quoteReply){
        MessageChain messages;
        MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
        if (quoteReply!=null){
            messageChainBuilder.append(quoteReply);
        }
        if (tmpImg == null) {
            messages = messageChainBuilder.append("图片生成发生错误").build();
        } else {
            Image image = Contact.uploadImage(contact, tmpImg);
            messages = messageChainBuilder.append(image).build();
            FileUtil.deleteFile(tmpImg);
        }
        contact.sendMessage(messages);
    }

}
