package com.king.task;

import com.king.component.MyBot;
import com.king.db.pojo.Members;
import com.king.db.service.MembersService;
import com.king.model.GroupId;
import com.king.util.DateFormateUtil;
import com.king.util.MyStringUtil;
import com.king.util.NormalMemberUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @description: 活跃度检测
 * @author: xyc0123456789
 * @create: 2023/3/29 22:20
 **/
@Slf4j
public class MemberActiveImpl {


    private final MembersService membersService;

    private final Long groupId;

    private final Group curGroup;

    private boolean inMainGroup = false;

    private boolean hasSpecialTitle = false;

    private int lastSpeak = -1;

    private final Set<NormalMember> toKick = new HashSet<>();

    private final StringBuilder warnMessage = new StringBuilder();


    private boolean haveBeenBuild = false;

    public MemberActiveImpl(MembersService membersService, Long groupId) {
        this.membersService = membersService;
        this.groupId = groupId;
        this.curGroup = MyBot.bot.getGroup(groupId);
    }

    public MemberActiveImpl andInMainGroup(){
        this.inMainGroup = true;
        return this;
    }

    public MemberActiveImpl andSpecialTitle(){
        this.hasSpecialTitle = true;
        return this;
    }

    public MemberActiveImpl andLastSpeak(int day){
        this.lastSpeak = day;
        return this;
    }

    private String getSpecialTitle(long qq_id){
        Members members = membersService.selectMember(GroupId.GROUP985, qq_id);
        if (members==null){
            members = membersService.selectMember(GroupId.Group985_2, qq_id);
            if (members==null) {
                return "";
            }
        }
        return members.getSpecialTitle();
    }


    public MemberActiveImpl build(){
        haveBeenBuild =true;
        warnMessage.append("未能满足以下任一条件: ");
        if (inMainGroup){
            warnMessage.append("在大群; ");
        }
        if (hasSpecialTitle){
            warnMessage.append("大群或者本群有头衔或者历史有头衔; ");
        }
        if (lastSpeak>0){
            warnMessage.append(lastSpeak).append("天内存在发言; ");
        }

        Group group985_2 = MyBot.bot.getGroup(GroupId.Group985_2);
        long minTime = 0;
        if (lastSpeak>0) {
            minTime = DateFormateUtil.offDate(-lastSpeak).getTime();
        }
        for (NormalMember normalMember:this.curGroup.getMembers()){
            boolean flag = true;
            if (inMainGroup){
                if (group985_2!=null&&group985_2.get(normalMember.getId())!=null){
//                    log.error("inMainGroup:{}",NormalMemberUtil.getNormalMemberStr(groupId,normalMember.getId()));
                    flag = false;
                }
            }

            if (hasSpecialTitle){
                if (MyStringUtil.isEmpty(normalMember.getSpecialTitle())){
                    String specialTitle = getSpecialTitle(normalMember.getId());
                    if (MyStringUtil.isEmpty(specialTitle)) {
//                        log.error("specialTitle:{}",NormalMemberUtil.getNormalMemberStr(groupId,normalMember.getId()));
                    }else {
                        flag = false;
                    }
                }else {
                    flag = false;
                }
            }

            if (lastSpeak>0){
                int normalMemberLastSpeakTimestamp = normalMember.getLastSpeakTimestamp();
                if(normalMemberLastSpeakTimestamp == 0) {
                    normalMemberLastSpeakTimestamp = normalMember.getJoinTimestamp();
                }
                long lastSpeakTimestamp = normalMemberLastSpeakTimestamp *1000L;
                if (lastSpeakTimestamp>minTime){
//                    log.error("lastSpeakTimestamp{} {}:{}",lastSpeakTimestamp,minTime,NormalMemberUtil.getNormalMemberStr(groupId,normalMember.getId()));
                    flag = false;
                }
            }
            if (flag){
                toKick.add(normalMember);
            }
        }
        return this;
    }

    public Set<NormalMember> toKickNormalMembers(){
        if (!haveBeenBuild){
            log.error("没有build就被调用了");
        }
        return toKick;
    }

    public Set<Long> toKickNormalMemberIds(){
        if (!haveBeenBuild){
            log.error("没有build就被调用了");
        }
        Set<Long> longSet = new HashSet<>();
        for (NormalMember normalMember:toKick){
            longSet.add(normalMember.getId());
        }
        return longSet;
    }

//    public void kick(){
//        if (!haveBeenBuild){
//            log.error("没有build就被调用了");
//            return;
//        }
//        for (NormalMember normalMember:toKick){
//            normalMember.kick(warnMessage.toString());
//        }
//    }

    public void sendWarnMessage(){
        sendWarnMessage(null, true);
    }

    public void sendWarnMessage(QuoteReply quoteReply, boolean isAt){
        if (!haveBeenBuild){
            log.error("没有build就被调用了");
            return;
        }
        MessageChainBuilder messages = new MessageChainBuilder();
        if (quoteReply!=null){
            messages.append(quoteReply);
        }
        if (!CollectionUtils.isEmpty(toKick)) {
            int count =0,total=toKick.size(),page=0,pageSize=50,totalPages=total/pageSize+(total%pageSize==0?0:1);
            for (NormalMember normalMember : toKick) {
                count++;
                if (isAt){
                    messages.append(new At(normalMember.getId())).append(",");
                }else {
                    messages.append(NormalMemberUtil.getNormalMemberStr(groupId, normalMember.getId())).append("; ");
                }
                if (count%pageSize==0||count==total){
                    page++;
                    messages.append("\npage: "+page+"/"+totalPages+" current: "+count+"/"+total+"\n")
                            .append(warnMessage.toString());
                    log.info(messages.build().serializeToMiraiCode());
                    curGroup.sendMessage(messages.build());
                    try {
                        Random random = new Random();
                        Thread.sleep(1000 + random.nextInt(2000) + random.nextInt(500));} catch (InterruptedException ignore) {}
                    messages = new MessageChainBuilder();
                    if (quoteReply!=null){
                        messages.append(quoteReply);
                    }
                }
            }
        }else {
            curGroup.sendMessage(messages.append("OK").build());
        }
        log.info("sendWarnMessage finished");
    }

    public StringBuilder getWarnMessage() {
        return warnMessage;
    }
}
