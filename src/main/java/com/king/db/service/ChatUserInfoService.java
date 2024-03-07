package com.king.db.service;


import com.king.db.pojo.ChatUserInfo;
import com.king.db.pojo.ChatUserInfoExample;
import com.king.db.subdao.SubChatUserInfoMapper;
import com.king.util.MyStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ChatUserInfoService {

    @Autowired
    private SubChatUserInfoMapper subChatUserInfoMapper;

    public static String getRequestNo(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static final String DEFAULT_SYSTEM_PROMPT = "";


    public ChatUserInfo getOrAddOne(String userId){
        ChatUserInfo chatUserInfo = subChatUserInfoMapper.selectByPrimaryKey(userId);
        if (chatUserInfo==null){
            chatUserInfo = new ChatUserInfo(userId, DEFAULT_SYSTEM_PROMPT, getRequestNo());
            subChatUserInfoMapper.insert(chatUserInfo);
        }
        return chatUserInfo;
    }

    public void update(ChatUserInfo chatUserInfo){
        subChatUserInfoMapper.updateByPrimaryKey(chatUserInfo);
    }

    public void updateWithReset(ChatUserInfo chatUserInfo){
        if (MyStringUtil.isEmpty(chatUserInfo.getPrompt())){
            chatUserInfo.setPrompt(DEFAULT_SYSTEM_PROMPT);
        }
        chatUserInfo.setLastRequestNo(getRequestNo());
        subChatUserInfoMapper.updateByPrimaryKey(chatUserInfo);
    }

    public void updateWithResetAll(){
        List<ChatUserInfo> chatUserInfos = subChatUserInfoMapper.selectByExample(new ChatUserInfoExample());
        for (ChatUserInfo chatUserInfo:chatUserInfos){
            updateWithReset(chatUserInfo);
        }
    }
}