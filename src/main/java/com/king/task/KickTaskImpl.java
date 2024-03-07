package com.king.task;

import com.king.component.MyBot;
import com.king.db.service.MembersService;
import com.king.model.KickRequest;
import com.king.model.MessageEventContext;
import com.king.util.NormalMemberUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.*;

@Slf4j
@Component
public class KickTaskImpl {

    @Autowired
    private MembersService membersService;


    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(50);


    public void kick(Long groupId) {
        kick(groupId, new Date(), 10);
    }

    public void kick(Long groupId, Date endDate) {
        kick(groupId, endDate, 10);
    }

    public void kick(Long groupId, Date endDate, int countTop) {
        Group group = MyBot.bot.getGroup(groupId);
        if (group == null) {
            return;
        }
        List<Long> longList = membersService.detectMembers(groupId, endDate);
        log.info("detect members:{}", longList);
        if (longList.size() > 0) {
            int total = longList.size();
            Set<Long> ids = new HashSet<>(longList);
            KickRequest kickRequest = new KickRequest(groupId, ids, "没能获取群头衔");
            String result;
            try {
                ScheduledFuture<String> stringScheduledFuture = normalKick(kickRequest);
                result = stringScheduledFuture.get();
                log.info("kick result:{}", result);

            }catch (Exception e){
                log.error("kick err", e);
                result = "error: "+e.getMessage();
            }
            StringBuilder messages = new StringBuilder();
            messages.append("total: ").append(total).append("\nkick result:").append(result).append("\n")
                    .append(kickRequest.getKickSuccessIds().size()).append(" success").append(":")
                    .append(NormalMemberUtil.getGroupMemberInfo(kickRequest.getGroupId(), kickRequest.getKickSuccessIds(), membersService)).append("\n")
                    .append(kickRequest.getKickFailIds().size()).append(" fail").append(":")
                    .append(NormalMemberUtil.getGroupMemberInfo(kickRequest.getGroupId(), kickRequest.getKickFailIds(), membersService));
            group.sendMessage(messages.toString());

            for (Long id:ids){
                try {
                    NormalMember normalMember = group.get(id);
                    int kickMemberCountAdd = membersService.kickMemberCountAdd(groupId, id);
                    log.info(NormalMemberUtil.getNormalMemberStr(normalMember) + " kickMemberCountAdd:{}", kickMemberCountAdd);
                }catch (Exception e){
                    log.error("", e);
                }
            }
        }else {
            group.sendMessage("No one need to be kicked out!");
        }

    }

    public void kickAndSendResult(KickRequest kickRequest, Contact contact) throws ExecutionException, InterruptedException {
        int total = kickRequest.getToKickIds().size();
        ScheduledFuture<String> ans = normalKick(kickRequest);
        String sansStr = ans.get();

        String successGroupMemberInfo = NormalMemberUtil.getGroupMemberInfo(kickRequest.getGroupId(), kickRequest.getKickSuccessIds(), membersService);
        if (successGroupMemberInfo.length()>1500){
            successGroupMemberInfo = successGroupMemberInfo.substring(0,1500);
        }
        String failGroupMemberInfo = NormalMemberUtil.getGroupMemberInfo(kickRequest.getGroupId(), kickRequest.getKickFailIds(), membersService);
        if (failGroupMemberInfo.length()>1500){
            failGroupMemberInfo = failGroupMemberInfo.substring(0, 1500);
        }

        MessageChain messages = new MessageChainBuilder().append("total ").append(String.valueOf(total))
                .append("; kick result:").append(sansStr).append("; success ").append(String.valueOf(kickRequest.getKickSuccessIds().size())).append("; fail ").append(String.valueOf(kickRequest.getKickFailIds().size())).append("\n")
                .append("success ").append(String.valueOf(kickRequest.getKickSuccessIds().size())).append(" : ").append(successGroupMemberInfo).append("\n")
                .append("fail ").append(String.valueOf(kickRequest.getKickFailIds().size())).append(" : ").append(failGroupMemberInfo).build();
        contact.sendMessage(messages);
    }

    public ScheduledFuture<String> normalKick(KickRequest kickRequest) throws ExecutionException, InterruptedException {
        Group group = MyBot.bot.getGroup(kickRequest.getGroupId());
        String error = null;
        if (group == null) {
            error = "group not found";
        }
        if (CollectionUtils.isEmpty(kickRequest.getToKickIds())) {
            error = "kick finished";
        }
        if (group!=null&&group.getBotPermission().getLevel() == 0) {
            error = "kick error : permission failed";
        }

        if (error!=null) {
            String finalError = error;
            return scheduledExecutorService.schedule(() -> finalError, 0, TimeUnit.SECONDS);
        }

        log.info("=========start kick=========");
        log.info("kick todo :{}", kickRequest.getToKickIds());
        int count = 0;
        ArrayList<Long> toRemove = new ArrayList<>();
        boolean tooFast = false;
        for (Long id : kickRequest.getToKickIds()) {
            count++;
            try {
                if (tooFast){
                    kickRequest.getKickFailIds().add(id);
                }else {
                    NormalMember normalMember = group.get(id);
                    if (normalMember != null) {
                        normalMember.kick(kickRequest.getMessage(), kickRequest.isBlock());
                        kickRequest.getKickSuccessIds().add(id);
                    } else {
                        kickRequest.getKickFailIds().add(id);
                    }
                }
            } catch (Exception e) {
                log.info(id + " kick err:" + e.getMessage());
                if ("Operation too fast".equals(e.getMessage())){
                    tooFast = true;
                }
                kickRequest.getKickFailIds().add(id);
            } finally {
                toRemove.add(id);
            }
            try {Thread.sleep(500);} catch (InterruptedException ignore) {}
            if (kickRequest.getCountTop() > 0 && count >= kickRequest.getCountTop()) {
                break;
            }
        }
        toRemove.forEach(kickRequest.getToKickIds()::remove);

        if (CollectionUtils.isEmpty(kickRequest.getToKickIds())) {
            return scheduledExecutorService.schedule(() -> "kick finished", 0, TimeUnit.SECONDS);
        }

        log.info("=========scheduledExecutorService.schedule after {} seconds=========", 5);
        return scheduledExecutorService.schedule(() -> {
            try {
                return this.normalKick(kickRequest).get();
            }catch (Exception e){
                log.error("", e);
            }
            return "error";
        }, 5, TimeUnit.SECONDS);
    }


    public static int random(int start, int end) {
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }
        return (int) (Math.random() * (end - start + 1) + start);
    }

    public static void fun(Set<Long> longList, ScheduledExecutorService scheduledExecutorService, Set<Long> skip) {
        if (longList.size() > 0) {
            int count = 0;
            for (Long id : longList) {
                if (skip.contains(id)) {
                    continue;
                }

                System.out.println(id);
                count++;
                skip.add(id);
                if (count >= 9) {
                    scheduledExecutorService.schedule(() -> {
                        fun(longList, scheduledExecutorService, skip);
                    }, 2, TimeUnit.SECONDS);
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
//        Set<Long> longList = new HashSet<>();
//        for (int i=0;i<30;i++){
//            longList.add((long) i);
//        }
//        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(20);
//        fun(longList, scheduledExecutorService, new HashSet<>());

        while (true) {
            System.out.println(random(0, 100));
        }

    }

}
