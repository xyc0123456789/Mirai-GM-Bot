package com.king.api.messageapis.friend;

import com.king.api.messageapis.common.HttpKeyWordApi;
import com.king.api.messageapis.group.AbstractGroupMessageApi;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.model.PlatformEnum;
import com.king.util.CommonMarkUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.king.model.RolyPlayPresetText.RolyMap;

@Component
@Slf4j
public class FriendMenu extends AbstractFriendMessageApi {
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        String content = messageEventContext.getContent();
        return "menu".equalsIgnoreCase(content)||"菜单".equals(content)||"#菜单".equals(content)||"#menu".equalsIgnoreCase(content)||"help".equalsIgnoreCase(content);
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        String menu = "menu,#menu,菜单,#菜单,help 均可呼出此菜单\\\n"+
                "###所有命令的#都是英文字母的#，键盘上是shift+3(#)\\\n" +
                "——命令\t（命令示例）说明\\\n" +
                "——关键词触发的相关命令，关键词: "+ HttpKeyWordApi.keyWordHandlerMap.keySet()+"\\\n" +
                "——#add 敏感词\t(#add 敏感词)添加敏感词，实时生效\\\n" +
                "——#remove 敏感词\t(#remove 敏感词)添加敏感词，实时生效\\\n" +
                "——#detect\t(#detectf groupId)检测群名片，#detectf中末尾的f可以为n或者不填，分别对应不限时间，当前时间，当天晚上12点\\\n" +
                "——任意文字包含两个emoji，仅限前两个emoji\\\n" +
                "——#reload\t(#reload)重载权限\\\n\\\n" +
//                "#givetitle\t(#givetitle 123)查询当前群聊qq并尝试给予已知头衔，参数仅限某一个qq_id或者all，仅管理员/群主有效\\\n"+

                "### 以下为chatGpt相关命令，由于技术问题，只能一个一个回答问题，gpt与reset后可以加位数字0-4来指定是哪个bot。\\\n"+
                "### 如果希望渲染LaTeX，请在原有命令后加上math，例如#gptmath、#gpt2math”\\\n"+
                "### 默认对话会在编号1-4的bot之间轮询。触发At可以替代#gpt这个命令，文本开头的数字(指定bot)与math(LaTeX)与#gpt用法一致\\\n"+
                "——#reset数字\t(#reset2)重置本群对话上下文\\\n" +
                "——#resetall\t(#resetall)重置所有人的上下文\\\n" +
                "——#gpt\t(#gpt 任意文本)发起对话请求，请耐心等待图片返回\\\n" +
                "——#botstatus\t(#botstatus)查询当前剩余请求情况\\\n" +
                "——#preset\t(#preset)输入”#preset+角色名“。角色列表："+RolyMap.keySet()+"\\\n\\\n"+

                "### 以下为直播订阅相关命令，当前可指定平台关键字:"+ PlatformEnum.enumMap.keySet() +"\\\n"+
                "——#subadd\t(#subadd 平台关键字 房间名称)订阅直播通知\\\n" +
                "——#subrm\t(#subrm)取消订阅\\\n" +
                "——#sublist\t(#sublist)查看当前所有订阅情况\\\n\\\n" +
                "更多可用内容有待开发测试，敬请期待...";
        File tmpImg = CommonMarkUtil.createMarkdownImg(menu, 1600);
        MessageChain messages;
        if (tmpImg==null){
            messages = new MessageChainBuilder().append("图片生成发生错误").build();
        }else {
            Image image = Contact.uploadImage(messageEvent.getSender(), tmpImg);
            messages = new MessageChainBuilder().append(image).build();
        }
        messageEvent.getSubject().sendMessage(messages);
        if (tmpImg!=null){
            boolean delete = tmpImg.delete();
            log.info("{} delete {}", tmpImg.getAbsolutePath(),delete);
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 10;
    }

    @Override
    public String commandName() {
        return "menu";
    }
}
