package com.king.task;


import com.king.api.messageapis.group.MessageRank;
import com.king.db.service.ChatUserInfoService;
import com.king.db.service.MembersService;
import com.king.model.GroupId;
import com.king.model.Response;
import com.king.util.DateFormateUtil;
import com.king.util.MessageAnalysisUtil;
import com.king.util.WordCloudUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.king.config.CommonConfig.MDC_TRACE_ID;

/**
 * 描述：写在cron表达式的标识符位置用于表达特定意义的字符，如 例1 中的：0 0 2 2 * ? *
 * 阿拉伯数字：数值，出现在标识符位置的数字代表对应值，比如第一个2代表两点，第二个2代表二号
 * * ：通配，语义相当于每… 比如第五个位置的 *就表示每月都会执行(相当于1-12)
 * ? ：忽略，语义相当于不管… 比如第六个位置的?就表示不管当前是周几就会执行。至于为什么会有这种用法，我觉得应该是因为它和其他的字符可能会冲突。如果用*的话表示周一到周日都会执行，此时其他语义就不明确了，所以如果用不上星期的话一般给它用一个?表示 not care。
 * / ：间隔，语义相当于每隔… 比如例2中的第三个位置的2/5就表示从2点开始每隔五小时
 * - ：区间，语义相当于第…到…的每… 比如例2中的第二个位置的15-20就表示第15分钟到20分钟之间的每分钟
 * , ：枚举，语义相当于第…和… 比如例2中的第一个位置的15,20,40就表示第15秒、20秒和40秒
 * L ：最后(last)，语义相当于最后一个 比如例2中的第四个位置的L就表示最后一天
 * W ：工作日，字面意思，就是工作日 比如例3中的第四个位置的15W表示15号附近最近的工作日，如果15号刚好是工作日那就15号触发，如果15号是周六那就14号触发，如果15号是周日那就16号触发。前面不带数字就是所有匹配的工作日。
 * # ：周定位，语义相当于每月的第几个周几 比如例4中的第六个位置的2#3就表示第三个周一。
 */
@Component
@Slf4j
public class AllTask {

    @Autowired
    private MembersService membersService;

    @Autowired
    private KickTaskImpl kickTask;

    @Autowired
    private MessageRank messageRank;

    @Autowired
    private SubscriptionTaskImpl subscriptionTask;

    @Autowired
    protected ChatUserInfoService chatUserInfoService;

    @Scheduled(cron ="0 0 3 * * ?")
    public void task(){
        log.info("================定时更新群信息================");
        Response response = membersService.updateGroups();
        log.info("================定时更新结束:[{}]{}================",response.getCode(),response.getMessage());
    }


    @Scheduled(cron ="0 0 4 * * ?")
    public void resetAll(){
        log.info("================定时清除上下文开始================");
        chatUserInfoService.updateWithResetAll();
        log.info("================定时清除上下文完成================");
    }

//    @Scheduled(cron ="0 0 10,18 * * ?")
//    public void detect(){
//        membersService.detect(GroupId.Group985_2, DateFormateUtil.getOffsetDate000000(1));
//    }

//    @Scheduled(cron ="1 0 0 * * ?")
//    public void kick(){
//        kickTask.kick(GroupId.Group985_2);
//    }

//    @Scheduled(cron ="0 0/10 1-5 * * ?")
//    public void muteStart(){
//        MuteAllImpl.muteAll(GroupId.Group985_2, true);
//    }
//
//    @Scheduled(cron ="0 0 7 * * ?")
//    public void muteStop(){
//        MuteAllImpl.muteAll(GroupId.Group985_2, false);
//    }

    @Scheduled(cron ="0 0/15 * * * ?")
    public void deleteFile(){
        DeleteWordCloudFileTaskImpl.deleteMemeFile(20);
    }



//    @Scheduled(cron ="0 0 22 * * ?")
//    public void detectnegativea90(){
//        if (!DateFormateUtil.checkDate()){
//            return;
//        }
//        try {
//            MDC.put(MDC_TRACE_ID, "detectnegativea90");
//            MemberActiveImpl memberActive = new MemberActiveImpl(membersService, GroupId.Group985_2)
//                    .andLastSpeak(90).build();
//            memberActive.sendWarnMessage();
//        }finally {
//            MDC.remove(MDC_TRACE_ID);
//        }
//    }

    @Scheduled(cron ="5 0 0 * * ?")
    public void messageRank(){
//        messageRank.sendRank(GroupId.Group985_2, DateFormateUtil.offDate(-1));
        messageRank.sendRank(GroupId.AIQUN, DateFormateUtil.offDate(-1));
//        WordCloudUtil.sendWordCloud(GroupId.Group985_2, DateFormateUtil.offDate(-1));
        WordCloudUtil.sendWordCloud(GroupId.AIQUN, DateFormateUtil.offDate(-1));
//        MessageAnalysisUtil.analyseGroup(GroupId.Group985_2, null, DateFormateUtil.offDate(-1));
        MessageAnalysisUtil.analyseGroup(GroupId.AIQUN, null, DateFormateUtil.offDate(-1));
    }

    @Scheduled(cron ="0 * * * * ?")
    public void subscription(){
        MDC.put(MDC_TRACE_ID, "subscription");
        try {
            subscriptionTask.doTask(true);
        }finally {
            MDC.remove(MDC_TRACE_ID);
        }
    }

//    @Scheduled(cron ="5 0 0 * * ?")
//    public void aiTokick(){
//        try {
//            MDC.put(MDC_TRACE_ID, "aiTokick");
//            MemberActiveImpl memberActive = new MemberActiveImpl(membersService, GroupId.AIQUN)
//                    .andInMainGroup().andSpecialTitle().andLastSpeak(15).build();
//            memberActive.sendWarnMessage();
//        }finally {
//            MDC.remove(MDC_TRACE_ID);
//        }
//
//    }

}
