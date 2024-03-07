package com.king.db.service;


import com.king.db.pojo.ChatRequestRecord;
import com.king.db.pojo.ChatRequestRecordExample;
import com.king.db.subdao.SubChatRequestRecordMapper;
import com.king.util.DateFormateUtil;
import com.king.util.MyStringUtil;
import com.king.util.openai.component.OpenAIRequestContext;
import com.unfbx.chatgpt.entity.chat.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class ChatRequestRecordService {

    @Autowired
    private SubChatRequestRecordMapper subChatRequestRecordMapper;

    private static final AtomicInteger suffixIdex = new AtomicInteger(0);

    private static int getLastId(){
        int andAdd = suffixIdex.getAndAdd(1);
        if (andAdd>=1000){
            suffixIdex.getAndSet(0);
            return suffixIdex.getAndAdd(1);
        }
        return andAdd;
    }

    private static String getRequestDate(){
        return DateFormateUtil.formatYYYYMMDD(new Date());
    }

    private static String getRequestTime(){
        String format =  DateFormateUtil.formatHHMMSS(new Date()) + "%03d";
        return String.format(format, getLastId());
    }

    public ChatRequestRecord addOne(String requestNo, String messageId, String userId, String role, String content) {
        if (MyStringUtil.isEmpty(messageId)){
            messageId = requestNo;
        }
        ChatRequestRecord chatRequestRecord = new ChatRequestRecord();
        chatRequestRecord.setRequestDate(getRequestDate());
        chatRequestRecord.setRequestTime(getRequestTime());
        chatRequestRecord.setRequestNo(requestNo);
        chatRequestRecord.setMessageId(messageId);
        chatRequestRecord.setUserId(userId);
        chatRequestRecord.setRole(role);
        chatRequestRecord.setContent(content);
        subChatRequestRecordMapper.insert(chatRequestRecord);
        return chatRequestRecord;
    }

    public ChatRequestRecord addPrompt(String requestNo, String userId, String prompt){
        List<ChatRequestRecord> requestRecords = getByRequestNo(requestNo);
        if (requestRecords!=null) {
            for (ChatRequestRecord chatRequestRecord : requestRecords) {
                if (Message.Role.SYSTEM.getName().equalsIgnoreCase(chatRequestRecord.getRole())) {
                    return chatRequestRecord;
                }
            }
        }
        return addOne(requestNo, requestNo, userId, Message.Role.SYSTEM.getName(), prompt);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void addAns(OpenAIRequestContext openAIRequestContext){
        if (openAIRequestContext.getResponseMessage().length()>0 && openAIRequestContext.getFinishReason()==null) {
            addOne(openAIRequestContext.getRequestNo(), openAIRequestContext.getMessageId(), openAIRequestContext.getUserId(), Message.Role.USER.getName(), openAIRequestContext.getRequestMessage());
            addOne(openAIRequestContext.getRequestNo(), openAIRequestContext.getMessageId(), openAIRequestContext.getUserId(), openAIRequestContext.getResponseRole().getName(), openAIRequestContext.getResponseMessage().toString());
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void addAns(String requestNo, String messageId, String userId, String request, String response){
        if (! MyStringUtil.isEmpty(request) && ! MyStringUtil.isEmpty(response)) {
            addOne(requestNo, messageId, userId, Message.Role.USER.getName(), request);
            addOne(requestNo, messageId, userId, Message.Role.ASSISTANT.getName(), response);
        }
    }


    public List<ChatRequestRecord> getByRequestNo(String requestNo) {
        ChatRequestRecordExample example = new ChatRequestRecordExample();
        ChatRequestRecordExample.Criteria criteria = example.createCriteria();
        criteria.andRequestNoEqualTo(requestNo);
        List<ChatRequestRecord> chatRequestRecords = subChatRequestRecordMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(chatRequestRecords)) {
            return null;
        }
        chatRequestRecords.sort(Comparator.comparing(ChatRequestRecord::getRequestDate).thenComparing(ChatRequestRecord::getRequestTime));
        return chatRequestRecords;
    }

    public static void main(String[] args) {
        for (int i=0;i<10000;i++){
            System.out.println(getLastId());
        }
    }

}