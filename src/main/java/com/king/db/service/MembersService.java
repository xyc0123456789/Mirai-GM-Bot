package com.king.db.service;


import com.king.component.MyBot;
import com.king.config.CommonConfig;
import com.king.db.pojo.Members;
import com.king.db.pojo.MembersExample;
import com.king.db.subdao.SubMembersMapper;
import com.king.model.GroupId;
import com.king.model.LastDetectResult;
import com.king.model.QQFriendId;
import com.king.model.Response;
import com.king.util.*;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.data.UserProfile;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.king.model.ExtDataJsonKeys.COUNT;
import static com.king.model.ExtDataJsonKeys.KICK_COUNT;

@Slf4j
@Service
public class MembersService {

    @Autowired
    private SubMembersMapper subMembersMapper;

    @Autowired
    private ContactListenListService contactListenListService;

    public Members addMember(Group group, Member member){
        long id = member.getId();
        NormalMember normalMember = group.get(id);
        UserProfile userProfile = member.queryProfile();

        Members newMember = new Members();

        String nameCard = member.getNameCard();//群名片
        String specialTitle = member.getSpecialTitle();//群头衔

        if (normalMember!=null){
            int joinTimestamp = normalMember.getJoinTimestamp();
            Date joinDate = new Date(joinTimestamp* 1000L);

            int lastSpeakTimestamp = normalMember.getLastSpeakTimestamp();
            Date lastSpeakDate = new Date(lastSpeakTimestamp * 1000L);

            if (joinTimestamp==0){
                newMember.setJoinDate(new Date());
            }else {
                newMember.setJoinDate(joinDate);
            }

            if (lastSpeakTimestamp==0){
                newMember.setLastSpeakDate(null);
            }else {
                newMember.setLastSpeakDate(lastSpeakDate);
            }
        }
        int age = userProfile.getAge();
        String email = userProfile.getEmail();
        String nickname = userProfile.getNickname();
        String sex = userProfile.getSex().name();
        int qLevel = userProfile.getQLevel();
        String sign = userProfile.getSign();

        newMember.setGroupId(group.getId());
        newMember.setQqId(id);
        newMember.setAge(age);
        newMember.setEmail(email);
        newMember.setQqLevel(qLevel);
        newMember.setSex(sex);
        newMember.setSign(sign);
        newMember.setNickName(nickname);
        newMember.setEnable(true);

        Members oldMembers = subMembersMapper.selectByPrimaryKey(group.getId(), id);
        if (oldMembers==null){
            newMember.setSpecialTitle(specialTitle);
            newMember.setNameCard(nameCard);
            newMember.setCtime(new Date());
            newMember.setExtData(JsonUtil.json(COUNT,"1"));
            subMembersMapper.insert(newMember);
        }else {
            if (MyStringUtil.isEmpty(oldMembers.getExtData())){
                newMember.setExtData(JsonUtil.json(COUNT,"1"));
            }else {
                Map<String,String> map = JsonUtil.fromJson(oldMembers.getExtData(), Map.class);
                if (map==null){
                    newMember.setExtData(JsonUtil.json(COUNT,"1"));
                }else {
                    String count = map.get(COUNT);
                    map.put(COUNT, String.valueOf(Integer.parseInt(count) + 1));
                    newMember.setExtData(JsonUtil.toJson(map));
                }
            }
            subMembersMapper.updateByPrimaryKeySelective(newMember);
        }
        return subMembersMapper.selectByPrimaryKey(group.getId(), id);
    }

    public Members selectMember(Long groupId,Long memberId){
        return subMembersMapper.selectByPrimaryKey(groupId, memberId);
    }

    public List<Members> getGroupList(Long groupId){
        return subMembersMapper.getByGroup(groupId);
    }

    public Members memberLeave(Long groupId,Long memberId){
        Members members = selectMember(groupId, memberId);
        Date lastDate = members.getUtime();
        members.setEnable(false);
        members.setUtime(new Date());
        subMembersMapper.updateByPrimaryKeySelective(members);
        members.setUtime(lastDate);
        return members;
    }

    public void updateByPrimaryKeySelective(Members members){
        subMembersMapper.updateByPrimaryKeySelective(members);
    }

    public int kickMemberCountAdd(Long groupId,Long memberId){
        try {
            Members members = selectMember(groupId, memberId);
            Map<String, String> map = JsonUtil.fromJson(members.getExtData(), Map.class);
            String count = map.getOrDefault(KICK_COUNT,"0");
            int oricount = Integer.parseInt(count);
            map.put(KICK_COUNT, String.valueOf(oricount+1));
            members.setExtData(JsonUtil.toJson(map));
            subMembersMapper.updateByPrimaryKeySelective(members);
            return oricount+1;
        }catch (Exception e){
            log.error("kickMemberCountAdd err", e);
            return 0;
        }
    }

    public Members updateMember(NormalMember normalMember, Members members){
        if (members==null){
            return null;
        }
        members.setQqId(normalMember.getId());
        members.setGroupId(normalMember.getGroup().getId());
        subMembersMapper.updateByPrimaryKeySelective(members);
        return members;
    }

    public void detectTest(Date endDate){
        boolean isCurrent = false;
        if (endDate==null){
            isCurrent = true;
            endDate = new Date();
        }
        List<Long> longs = detectMembers(GroupId.GROUP985, endDate);
        if (longs.size()!=0){
            MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
            for (Long id:longs){
                messageChainBuilder.append(new At(id));
            }
            String messageStr = "以上成员已入群一天但未能获得群头衔";
            if (!isCurrent) {
                messageStr = "以上成员若在" + DateFormateUtil.formatYYYYMMDDHHMMSS(endDate) + "前仍未能获得群头衔，将被踢出群聊。相关证明请私信管理（不要发给bot）或者直接发群里，学信网、校园卡、录取通知等均可";
            }
            MessageChain message = messageChainBuilder.append("\n").append(messageStr).build();
            Group group = MyBot.bot.getGroup(GroupId.TEST);
            log.info(message.serializeToMiraiCode());
            if (group!=null){
                group.sendMessage(message);
            }
        }
    }

    public void detect(Long groupId){
        detect(groupId, null);
    }

    public void detect(Long groupId,Date endDate){
        detect(groupId, endDate, 0);
    }

    public void detect(Long groupId,Date endDate, int nums){
        boolean isCurrent = false;
        if (endDate==null){
            isCurrent = true;
            endDate = new Date();
        }

        List<Long> longs = detectMembers(groupId, endDate);
        if (nums>0){
            longs = ListUtil.randomList(longs, nums);
        }
        if (longs.size()!=0){
            CommonConfig.lastDetect.put(groupId, new LastDetectResult(longs));
            Group group = MyBot.bot.getGroup(groupId);
            if (group==null){
                return;
            }
            String messageStr = "以上成员已入群一天但未能获得群头衔";
            if (DateFormateUtil.deltaDateWithMS(endDate, new Date())>8640000000L){//100天
                messageStr = "以上成员仍未找群主验证获得群头衔，请及时修改群名片，找群主验证获取群头衔。相关证明请私信群主或者直接发群里，学信网、校园卡、录取通知等均可";
            }else if (!isCurrent){
                messageStr = "以上成员若在"+DateFormateUtil.formatYYYYMMDDHHMMSS(endDate)+"前仍未找群主验证获得群头衔，将被踢出群聊。相关证明请私信群主或者直接发群里，学信网、校园卡、录取通知等均可";
            }
            NormalMemberUtil.sendMessageForIds(longs, messageStr, group);
        }
    }

    public List<Long> detectMembers(Long groupId){
        return detectMembers(groupId, new Date());
    }
    public List<Long> detectMembers(Long groupId, Date endDate){
        log.info("---检测群名片:"+groupId+DateFormateUtil.formatYYYYMMDDHHMMSS(endDate)+"---");
        List<Long> ans = new ArrayList<>();
        Group group = MyBot.bot.getGroup(groupId);
        if (group==null){
            log.error("group {} not found", groupId);
            log.info("===检测群名片:"+groupId+"===");
            return ans;
        }
        for (NormalMember normalMember: group.getMembers()){
            String specialTitle = normalMember.getSpecialTitle();

            if (MyStringUtil.isEmpty(specialTitle)){
                long joinTime = normalMember.getJoinTimestamp()*1000L;
                if (joinTime==0){
                    Members members = subMembersMapper.selectByPrimaryKey(groupId, normalMember.getId());
                    joinTime = members.getCtime().getTime();
                }
                long ms = endDate.getTime() - joinTime;
                boolean flag = ms > 86400000;//24h
                if (flag){
                    if (QQFriendId.Q_QUN_GUAN_JIA== normalMember.getId()){//Q群管家
                        continue;
                    }
                    if (QQFriendId.QUN_ZHU == normalMember.getId()){
                        continue;
                    }
                    ans.add(normalMember.getId());
                }
            }
        }
        log.info("===检测群名片:"+groupId+"===");
        return ans;
    }

    public Response updateGroups(){
        List<Members> all = subMembersMapper.getAll();
        HashSet<String> set = new HashSet<>();

        for (Members member:all){
            set.add(member.getGroupId() +String.valueOf(member.getQqId()));
        }
        StringBuilder failMessage = new StringBuilder();
        Map<Long, String> linsteningGroups = contactListenListService.getByType(1);
        Set<String> onlineMembers = new HashSet<>();
        for (Long groupId:linsteningGroups.keySet()){
            log.info("Start update group {}",groupId);
            Group group = MyBot.bot.getGroup(groupId);
            if (group==null){
                continue;
            }
            try {
                for (NormalMember normalMember: group.getMembers()){
                    UserProfile userProfile = normalMember.queryProfile();

                    Members newMember = new Members();

                    String nameCard = normalMember.getNameCard();//群名片
                    String specialTitle = normalMember.getSpecialTitle();//群头衔
                    int age = userProfile.getAge();
                    String email = userProfile.getEmail();
                    String nickname = userProfile.getNickname();
                    String sex = userProfile.getSex().name();
                    int qLevel = userProfile.getQLevel();
                    String sign = userProfile.getSign();

                    int joinTimestamp = normalMember.getJoinTimestamp();
                    Date joinDate = new Date(joinTimestamp* 1000L);

                    int lastSpeakTimestamp = normalMember.getLastSpeakTimestamp();
                    Date lastSpeakDate = new Date(lastSpeakTimestamp * 1000L);

                    if (joinTimestamp==0){
                        newMember.setJoinDate(new Date());
                    }else {
                        newMember.setJoinDate(joinDate);
                    }

                    if (lastSpeakTimestamp==0){
                        newMember.setLastSpeakDate(null);
                    }else {
                        newMember.setLastSpeakDate(lastSpeakDate);
                    }

                    newMember.setGroupId(groupId);
                    newMember.setQqId(normalMember.getId());
                    newMember.setAge(age);
                    newMember.setEmail(email);
                    newMember.setQqLevel(qLevel);
                    newMember.setSex(sex);
                    newMember.setSign(sign);
                    newMember.setNickName(nickname);
                    newMember.setNameCard(nameCard);
                    if (!MyStringUtil.isEmpty(specialTitle)) {
                        newMember.setSpecialTitle(specialTitle);
                    }
                    newMember.setEnable(true);
                    String key = newMember.getGroupId() + String.valueOf(newMember.getQqId());
                    onlineMembers.add(key);
                    Members members = subMembersMapper.selectByPrimaryKey(groupId, normalMember.getId());
                    if (members!=null){
                        subMembersMapper.updateByPrimaryKeySelective(newMember);
                    }else {
                        newMember.setCtime(joinDate);
                        newMember.setExtData(JsonUtil.json(COUNT,"1"));
                        subMembersMapper.insertSelective(newMember);
                    }
                }
            }catch (Exception e){
                log.error(groupId+" 更新失败",e);
                failMessage.append(groupId).append(";");
            }
        }
        //处理已经离群的标识
        all = subMembersMapper.getAll();
        for (Members members:all){
            String key = members.getGroupId() + String.valueOf(members.getQqId());
            if (!onlineMembers.contains(key) && MyBot.bot.getGroup(members.getGroupId())!=null){
                try {
                    if (members.getEnable()){
                        members.setEnable(false);
                        subMembersMapper.updateByPrimaryKeySelective(members);
                    }
                }catch (Exception e){
                    log.error("在线member处理失败", e);
                }
            }
        }

        if (failMessage.length()==0){
            return Response.ok(linsteningGroups.keySet().toString());
        }else {
            return Response.fail(failMessage +"更新失败");
        }
    }

    public List<Members> getAllEnable(Long groupId){
        MembersExample membersExample = new MembersExample();
        MembersExample.Criteria criteria = membersExample.createCriteria();
        criteria.andGroupIdEqualTo(groupId);
        criteria.andEnableEqualTo(true);
        return subMembersMapper.selectByExample(membersExample);
    }

}