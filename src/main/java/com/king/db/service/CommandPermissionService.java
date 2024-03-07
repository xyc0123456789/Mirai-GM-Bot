package com.king.db.service;


import com.king.api.memberapis.MemberApi;
import com.king.api.memberapis.MemberApiGroup;
import com.king.api.messageapis.*;
import com.king.component.MyBot;
import com.king.db.pojo.CommandPermission;
import com.king.db.pojo.GroupSpecialList;
import com.king.db.pojo.TempGroupSpecialList;
import com.king.db.subdao.SubCommandPermissionMapper;
import com.king.event.friend.MyFriendEventMessageHandler;
import com.king.event.group.MyGroupMemberEventHandler;
import com.king.event.group.MyGroupMessageEventHandler;
import com.king.event.group.MyGroupTempMessageEventHandler;
import com.king.model.MemberEventContext;
import com.king.model.MessageEventContext;
import com.king.model.MessageRecallEventContext;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.event.events.MessageRecallEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class CommandPermissionService {

    @Autowired
    private SubCommandPermissionMapper subCommandPermissionMapper;

    @Autowired
    private MyGroupMessageEventHandler myGroupMessageEventHandler;

    @Autowired
    private MyGroupMemberEventHandler myGroupMemberEventHandler;

    @Autowired
    private MyFriendEventMessageHandler myFriendEventMessageHandler;

    @Autowired
    private MyGroupTempMessageEventHandler myGroupTempMessageEventHandler;


    @Autowired
    private FriendMessageApiGroup friendMessageApiGroup;

    @Autowired
    private GroupMessageApiGroup groupMessageApiGroup;

    @Autowired
    private MemberApiGroup memberApiGroup;

    @Autowired
    private TempGroupMessageApiGroup tempGroupMessageApiGroup;

    @Autowired
    private GroupSpecialListService groupSpecialListService;

    @Autowired
    private TempGroupSpecialListService tempGroupSpecialListService;


    public static final Map<String, Boolean> GroupMemberPermissionMap = new ConcurrentHashMap<>();

    public static final Map<String, Boolean> GroupMessagePermissionMap = new ConcurrentHashMap<>();

    public static final Map<String, Boolean> FriendMessagePermissionMap = new ConcurrentHashMap<>();

    public static final Map<String, Boolean> TempGroupMessagePermissionMap = new ConcurrentHashMap<>();

    public void reloadPermission() {
        reloadPermission(true);
    }
    public void reloadPermission(boolean logShow) {
        initPermissionMapWithGroupMember();
        initPermissionMapWithGroupMessage();
        initPermissionMapWithFriendMessage();
        initPermissionMapWithTempGroupMessage();

        //生成默认配置，所有个人默认+所有群默认 通过添加默认为0的contactListenList
        initSubContactPermissionMapWithGroupMessage();
        initSubContactPermissionMapWithTempGroupMessage();
        if (logShow) {
            printMap(FriendMessagePermissionMap);
            printMap(GroupMessagePermissionMap);
            printMap(GroupMemberPermissionMap);
            printMap(TempGroupMessagePermissionMap);
        }
        log.info("==========reloadPermission finish==========");
    }

    public static void printMap(Map<String, Boolean> map) {
        for (String key : map.keySet()) {
            log.info(key + ": " + map.get(key));
        }
    }

    /**
     * 初始化，本地加载信息
     */
    public void initPermissionMapWithGroupMessage() {
        GroupMessagePermissionMap.clear();
        for (Long groupId : myGroupMessageEventHandler.filterHashMap.keySet()) {
            if (groupId!=0&&MyBot.bot.getGroup(groupId) == null) {
                continue;
            }
            for (GroupMessageApi groupMessageApi : groupMessageApiGroup.groupMessageApis) {
                CommandPermission commandPermission = getGroupCommonPermissionOrDefault(groupId, groupMessageApi.commandName(), groupMessageApi.defaultStatus());
                GroupMessagePermissionMap.put(getKey(commandPermission), commandPermission.getIsOpen());
            }
        }
    }

    /**
     * 群员事件权限
     */
    public void initPermissionMapWithGroupMember() {
        GroupMemberPermissionMap.clear();
        for (Long groupId : myGroupMemberEventHandler.filterHashMap.keySet()) {
            if (groupId!=0&&MyBot.bot.getGroup(groupId) == null) {
                continue;
            }
            for (MemberApi memberApi : memberApiGroup.memberApiList) {
                CommandPermission commandPermission = getGroupCommonPermissionOrDefault(groupId, memberApi.commandName(), memberApi.defaultStatus());
                GroupMemberPermissionMap.put(getKey(commandPermission), commandPermission.getIsOpen());
            }
        }
    }

    /**
     * 好友消息权限
     */
    public void initPermissionMapWithFriendMessage() {
        FriendMessagePermissionMap.clear();
        for (Long friendId : myFriendEventMessageHandler.filterHashMap.keySet()) {
            if (friendId!=0&&MyBot.bot.getFriend(friendId) == null) {
                continue;
            }
            for (FriendMessageApi friendMessageApi : friendMessageApiGroup.friendMessageApis) {
                CommandPermission commandPermission = getFriendCommonPermissionOrDefault(friendId, friendMessageApi.commandName(), friendMessageApi.defaultStatus());
                FriendMessagePermissionMap.put(getKey(commandPermission), commandPermission.getIsOpen());
            }
        }
    }

    /**
     * 个人详细配置
     */
    public void initSubContactPermissionMapWithGroupMessage() {
        List<GroupSpecialList> groupSpecialLists = groupSpecialListService.findAll();
        for (GroupSpecialList groupSpecialList : groupSpecialLists) {
            Long groupId = groupSpecialList.getGroupId();
            if (MyBot.bot.getGroup(groupId) == null) {
                continue;
            }
            Long subContactId = groupSpecialList.getContactId();
            Long permissionLevel = groupSpecialList.getPermissionLevel();
            for (GroupMessageApi groupMessageApi : groupMessageApiGroup.groupMessageApis) {
                boolean defaultStatus;
                if (permissionLevel%10 == 0) {
                    defaultStatus = false;
                } else if (permissionLevel%10 == 1) {
                    defaultStatus = groupMessageApi.defaultStatus();
                } else {
                    defaultStatus = true;
                }
                CommandPermission commandPermission = getGroupPersonPermissionOrDefault(groupId, subContactId, groupMessageApi.commandName(), defaultStatus);
                GroupMessagePermissionMap.put(getKey(commandPermission), commandPermission.getIsOpen());
            }
        }
    }


    public void initPermissionMapWithTempGroupMessage() {
        TempGroupMessagePermissionMap.clear();
        for (Long groupId : myGroupTempMessageEventHandler.filterHashMap.keySet()) {
            if (groupId!=0&&MyBot.bot.getGroup(groupId) == null) {
                continue;
            }
            for (TempGroupMessageApi tempGroupMessageApi : tempGroupMessageApiGroup.tempGroupMessageApis) {
                CommandPermission commandPermission = getGroupCommonPermissionOrDefault(groupId, tempGroupMessageApi.commandName(), tempGroupMessageApi.defaultStatus());
                TempGroupMessagePermissionMap.put(getKey(commandPermission), commandPermission.getIsOpen());
            }
        }
    }

    /**
     * 个人详细配置
     */
    public void initSubContactPermissionMapWithTempGroupMessage() {
        List<TempGroupSpecialList> tempGroupSpecialLists = tempGroupSpecialListService.findAll();
        for (TempGroupSpecialList tempGroupSpecialList : tempGroupSpecialLists) {
            Long groupId = tempGroupSpecialList.getGroupId();
            if (MyBot.bot.getGroup(groupId) == null) {
                continue;
            }
            Long subContactId = tempGroupSpecialList.getContactId();
            Long permissionLevel = tempGroupSpecialList.getPermissionLevel();
            for (TempGroupMessageApi tempGroupMessageApi : tempGroupMessageApiGroup.tempGroupMessageApis) {
                boolean defaultStatus;
                if (permissionLevel%10 == 0) {
                    defaultStatus = false;
                } else if (permissionLevel%10 == 1) {
                    defaultStatus = tempGroupMessageApi.defaultStatus();
                } else {
                    defaultStatus = true;
                }
                CommandPermission commandPermission = getGroupPersonPermissionOrDefault(groupId, subContactId, tempGroupMessageApi.commandName(), defaultStatus);
                TempGroupMessagePermissionMap.put(getKey(commandPermission), commandPermission.getIsOpen());
            }
        }
    }

    private CommandPermission getGroupCommonPermissionOrDefault(Long contactId, String commandName, boolean defaultStatus) {
        return getCommonPermissionOrDefault(true, contactId, null, commandName, defaultStatus);
    }

    private CommandPermission getGroupPersonPermissionOrDefault(Long contactId, Long subContactId, String commandName, boolean defaultStatus) {
        return getCommonPermissionOrDefault(true, contactId, subContactId, commandName, defaultStatus);
    }

    private CommandPermission getFriendCommonPermissionOrDefault(Long contactId, String commandName, boolean defaultStatus) {
        return getCommonPermissionOrDefault(false, contactId, null, commandName, defaultStatus);
    }

    private CommandPermission getCommonPermissionOrDefault(boolean isGroup, Long contactId, Long subContactId, String commandName, boolean defaultStatus) {
        int contactType = isGroup ? 1 : 0;
        if (subContactId == null) {
            subContactId = 0L;
        }
        CommandPermission commandPermission = subCommandPermissionMapper.selectByPrimaryKey(contactType, contactId, subContactId, commandName);
        if (commandPermission == null) {
            commandPermission = new CommandPermission();
            commandPermission.setContactType(contactType);
            commandPermission.setContactId(contactId);
            commandPermission.setSubContactId(subContactId);
            commandPermission.setCommandName(commandName);
            commandPermission.setIsOpen(defaultStatus);
            insert(commandPermission);
        }
        return commandPermission;
    }

    public static String getKey(CommandPermission commandPermission) {
        return commandPermission.getContactType() + "-" + commandPermission.getContactId() + "-" + commandPermission.getSubContactId() + "-" + commandPermission.getCommandName();
    }

    public static String getKeyFromMessageDefault(String commandName, MessageEventContext messageEventContext) {
        int contactType = 0;
        Long subContactId = 0L,contactId;
        if (messageEventContext.isGroup()) {
            contactType = 1;
        }
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        if (messageEvent instanceof GroupTempMessageEvent){
            GroupTempMessageEvent groupTempMessageEvent = (GroupTempMessageEvent) messageEvent;
            contactId = groupTempMessageEvent.getGroup().getId();
        }else {
            contactId = messageEvent.getSubject().getId();
        }
        String key = contactType + "-" + contactId + "-" + subContactId + "-" + commandName;
//        log.info(key);
        return key;
    }

    public static String getKeyFromMessageForPerson(String commandName, MessageEventContext messageEventContext) {
        int contactType = 0;
        long contactId;
        if (messageEventContext.isGroup()) {
            contactType = 1;
        }
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        if (messageEvent instanceof GroupTempMessageEvent){
            GroupTempMessageEvent groupTempMessageEvent = (GroupTempMessageEvent) messageEvent;
            contactId = groupTempMessageEvent.getGroup().getId();
        }else {
            contactId = messageEvent.getSubject().getId();
        }
        String key = contactType + "-" + contactId + "-" + messageEvent.getSender().getId() + "-" + commandName;
//        log.info(key);
        return key;
    }

    public static String getKeyFromMessageDefault(String commandName, MemberEventContext memberEventContext) {
        int contactType = 0;
        Long subContactId = 0L;
        contactType = 1;
        String key = contactType + "-" + memberEventContext.getGroupMemberEvent().getGroup().getId() + "-" + subContactId + "-" + commandName;
//        log.info(key);
        return key;
    }

    public static String getKeyFromMessageRecallDefault(String commandName, MessageRecallEventContext messageRecallEventContext, boolean subContact) {
        int contactType = 0;
        Long subContactId = 0L;
        contactType = 1;
        MessageRecallEvent.GroupRecall messageRecallEvent = (MessageRecallEvent.GroupRecall) messageRecallEventContext.getMessageRecallEvent();
        long groupId = messageRecallEvent.getGroup().getId();
        if (subContact){
            Member operator = messageRecallEvent.getOperator();
            if (operator==null){
                subContactId = messageRecallEvent.getBot().getId();
            }else {
                subContactId = operator.getId();
            }
        }

        String key = contactType + "-" + groupId + "-" + subContactId + "-" + commandName;
//        log.info(key);
        return key;
    }


    public CommandPermission insert(CommandPermission commandPermission) {
        commandPermission.setEnable(true);
        commandPermission.setCtime(new Date());
        subCommandPermissionMapper.insertSelective(commandPermission);
        return commandPermission;
    }


    public List<CommandPermission> getList(Integer contactType, Long contactId, Long subContactId){
        return subCommandPermissionMapper.selectByOnePerson(contactType, contactId, subContactId);
    }



    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void update(List<CommandPermission> commandPermissionList){
        for (CommandPermission commandPermission: commandPermissionList){
            subCommandPermissionMapper.updateByPrimaryKey(commandPermission);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void add(List<CommandPermission> commandPermissionList){
        for (CommandPermission commandPermission: commandPermissionList){
            subCommandPermissionMapper.insert(commandPermission);
        }
    }

}