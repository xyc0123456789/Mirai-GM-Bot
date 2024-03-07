package com.king.api.messageapis.group;

import com.king.api.messageapis.common.AbstractCommonMessageApi;
import com.king.api.messageapis.common.HttpKeyWordApi;
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
public class GroupMenu extends AbstractGroupMessageApi {
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
                "——命令\t（命令示例）说明\\\n\\\n" +
                "### 通用命令\\\n" +
                "——#导出群成员\t(#导出群成员)导出本群成员名单\\\n" +
                "——#exportmembers\t(#exportmembers)快速导出本群成员名单，数据可能不是实时，注意核对\\\n" +
                "——#roll\t(#roll)随机一个0-100的数字\\\n" +
                "——#wordcloud\t(#wordcloud)根据群聊天记录记录生成词云\\\n" +
                "——#selfwordcloud\t(#selfwordcloud)根据自己的发言记录生成词云，通过临时会话发送结果\\\n" +
                "——#analysegroup\t(#analysegroup)根据群聊天记录生成时段记录分析\\\n" +
                "——#selfanalysegroup\t(#selfanalysegroup)根据群自己的发言记录生成时段记录分析，通过临时会话发送结果\\\n" +
                "——#messagerank\t(#messagerank)根据记录生成当前发言排名\\\n" +
                "——#sptitlecount\t(#sptitlecount)统计当前群聊各头衔人数\\\n" +
                "——任意文字包含两个emoji，仅限前两个emoji\\\n" +
                "——关键词触发的相关命令，关键词: "+ HttpKeyWordApi.keyWordHandlerMap.keySet()+"\\\n\\\n" +

                "### 以下为我画你猜相关命令\\\n"+
                "——我画你猜 猜的词\t(我画你猜 苹果)开启群的我画你猜，可通过私聊bot发送此命令\\\n" +
                "——#whncstop uuid\t(#whncstop uuid)提前结束uuid对应的我画你猜\\\n" +
                "——#whncstatus\t(#whncstatus)查询当前群的我画你猜剩余任务\\\n" +

                "### 仅管理员/群主有效的命令\\\n" +
                "——#kick\t(#kick)将当前群聊当前时间入群一天但未获取头衔的踢出群聊\\\n" +
                "——#kickdetect\t(#kickdetect)将detect命令检测到的当前群聊入群一天但未获取头衔的踢出群聊\\\n" +
                "——#kickforce\t(#kickforce)将当前群聊未获取头衔的踢出群聊\\\n" +
                "——#kicklowlevel\t(#kicklowlevel 99)踢出当前群聊低于某一等级的群员\\\n" +
                "——#kicknegative\t(#kicknegative 未发言天数x)踢出当前群聊x天未发言的\\\n" +
                "——#kicklastdetect\t(#kicklastdetect)踢出当前群聊前一次被检测到的人，包括以下命令所有以#detect开头的命令\\\n" +
                "——#detect\t(#detect)查询当前群聊入群一天但未获取头衔的\\\n" +
                "——#detectr\t(#detectr)查询当前群聊入群一天但未获取头衔的,上限20\\\n" +
                "——#detectforce\t(#detectforce)查询当前群聊未获取头衔的\\\n" +
                "——#detectforcer\t(#detectforcer)查询当前群聊未获取头衔的,上限20\\\n" +
                "——#detectnegative\t(#detectnegative 未发言天数x)查询当前群聊x天未发言的\\\n" +
                "——#detectlowlevel\t(#detectlowlevel 99)检测当前群聊低于某一等级的群员\\\n" +
                "——#detectspecialtitle\t(#detectspecialtitle 头衔关键字 头衔关键字)检测当前群聊具有特定头衔的群员, 关键字使用模糊匹配\\\n" +
                "——#permissionblock\t(#permissionblock qq号 qq号...)用于屏蔽特定成员的所有命令，也可用@来代替qq号\\\n" +
                "——#permissionallow\t(#permissionallow qq号 qq号...)用于恢复特定成员的所有命令，也可用@来代替qq号\\\n" +
                "——#temppermissionblock\t(#temppermissionblock qq号 qq号...)用于屏蔽特定成员临时对话的所有命令，也可用@来代替qq号\\\n" +
                "——#temppermissionallow\t(#temppermissionallow qq号 qq号...)用于恢复特定成员临时对话的所有命令，也可用@来代替qq号\\\n" +
                "——#进群通知\t(#进群通知 通知内容...)修改进群通知内容，不输入内容将取消进群通知信息\\\n\\\n" +
//                "#givetitle\t(#givetitle 123)查询当前群聊qq并尝试给予已知头衔，参数仅限某一个qq_id或者all，仅管理员/群主有效\\\n"+

//                "### 以下为chatGpt相关命令，由于技术问题，只能一个一个回答问题，gpt与reset后可以加位数字0-4来指定是哪个bot。\\\n"+
//                "### 如果希望渲染LaTeX，请在原有命令后加上math，例如#gptmath、#gpt2math”\\\n"+
//                "### 默认对话会在编号1-4的bot之间轮询。触发At可以替代#gpt这个命令，文本开头的数字(指定bot)与math(LaTeX)与#gpt用法一致\\\n"+
                "### 以下为chatGpt/new bing相关命令\\\n"+
                "### 如果希望gpt渲染LaTeX，请在原有命令后加上math，例如#gptmath”\\\n"+
                "### 触发At或者引用可以替代#gpt这四个字，如需渲染请以math开头\\\n"+
                "### #gpt，at或者回复触发的是各自qq号对应的上下文\\\n"+
//                "——#reset\t(#reset/#reset0)重置bot对话上下文\\\n" +
                "——#gpt\t(#gpt 任意文本)发起对话请求，请耐心等待图片返回\\\n" +
                "——#reset\t(#reset prompt)重置发起者qq号对应的上下文对话请求,如果带了文本，会作为prompt\\\n\\\n" +

                "——.+\\.pdf\\s+.+\t(任意pdf.pdf 查询语句)检索pdf返回所需要的信息，不具备上下文，pdf的token不能超出16k\\\n" +
//                "——#bing\t(#bing 任意文本)发起new bing对话请求，请耐心等待合并转发返回\\\n" +
//                "——#bingreset\t(#bingreset)重置new bing上下文\\\n\\\n" +
//                "——#botstatus\t(#botstatus)查询当前剩余请求情况\\\n" +
//                "——#preset\t(#preset)输入”#preset+角色名“。角色列表："+RolyMap.keySet()+"\\\n\\\n"+

                "### 以下为直播订阅相关命令，当前可指定平台关键字:"+ PlatformEnum.enumMap.keySet() +"\\\n"+
                "### 任意房间当无人订阅时将不会发送开播通知\\\n"+
                "——#subadd\t(#subadd 平台关键字 房间号)订阅直播通知\\\n" +
                "——#subrm\t(#subrm 平台关键字 房间号)取消订阅\\\n" +
                "——#sublist\t(#sublist)查看当前所有订阅情况\\\n\\\n" +

                "### meme合成相关命令\\\n"+
                "——memes列表：查看支持的memes列表\\\n" +
                "——{表情名称}+可能的参数：memes列表中的表情名称，根据提供的文字或图片制作表情包\\\n" +
//                "【随机meme】：随机制作一些表情包\n" +
                "——meme搜索+关键词：搜索表情包关键词\\\n" +
                "——{表情名称}+详情：查看该表情所支持的参数\\\n"+
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
