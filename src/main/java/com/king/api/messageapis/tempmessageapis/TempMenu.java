package com.king.api.messageapis.tempmessageapis;

import com.king.api.messageapis.common.HttpKeyWordApi;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
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

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/9 13:20
 **/
@Slf4j
@Component
public class TempMenu extends AbstractTempGroupMessageApi {
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
                "——#roll\t(#roll)随机一个0-100的数字\\\n" +
                "——#wordcloud\t(#wordcloud)根据记录生成自己的词云\\\n" +
                "——任意文字包含两个emoji，仅限前两个emoji\\\n" +
                "——关键词触发的相关命令，关键词: "+ HttpKeyWordApi.keyWordHandlerMap.keySet()+"\\\n\\\n" +

//                "### 以下为chatGpt相关命令，由于技术问题，有且仅有5个上下文，会发生串台情况，gpt与reset后可以加位数字0-4来指定是哪个bot。\\\n"+
//                "### 如果希望渲染LaTeX，请在原有命令后加上math，例如#gptmath、#gpt2math”\\\n"+
//                "### 默认对话会在编号1-4的bot之间轮询\\\n"+
                "### 以下为chatGpt/new bing相关命令，由于技术问题，有且仅有1个上下文，会发生串台情况\\\n"+
                "### 如果希望gpt渲染LaTeX，请在原有命令后加上math，例如#gptmath”\\\n"+
//                "——#reset\t(#reset/#reset0)重置bot对话上下文\\\n" +
                "——#gpt\t(#gpt 任意文本)发起对话请求，请耐心等待图片返回\\\n\\\n" +
                "——#bing\t(#bing 任意文本)发起new bing对话请求，请耐心等待合并转发返回\\\n" +
//                "——#bingreset\t(#bingreset)重置new bing上下文\\\n\\\n" +

                "### 以下为我画你猜相关命令\\\n"+
                "——我画你猜 猜的词\t(我画你猜 苹果)开启被发起临时会话的群的我画你猜\\\n\\\n" +
//                "——#botstatus\t(#botstatus)查询当前剩余请求情况\\\n" +
//                "——#preset\t(#preset)输入”#preset+角色名“。角色列表："+RolyMap.keySet()+"\\\n\\\n"+
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
        return 80;
    }

    @Override
    public String commandName() {
        return "temp.group.message.menu";
    }
}
