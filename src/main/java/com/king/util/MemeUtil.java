package com.king.util;

import com.king.db.pojo.MessageRecord;
import com.king.db.service.MessageRecordService;
import com.king.model.GroupId;
import com.king.model.MessageEventContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.data.UserProfile;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @description: meme
 * @author: xyc0123456789
 * @create: 2023/9/29 17:34
 **/

@Slf4j
@Component
public class MemeUtil {
    private static String baseUrl = "http://127.0.0.1:2233";

    private static MessageRecordService messageRecordService;
    private static final boolean reply = true;
    private static final boolean masterProtectDo = true;

    private static final Map<String, String> funcMap = new HashMap<>();
    private static Map<String, String> keyMap;
    private static Map<String, Object> infoMap;


    static {
        keyMap = JsonUtil.fromJson(StreamUtil.getStringFromResources("meme/keyMap.json"), Map.class);
        infoMap = JsonUtil.fromJson(StreamUtil.getStringFromResources("meme/info.json"), Map.class);
        funcMap.put("^(#)?(meme(s)?|表情包)列表$", "memesList");
        funcMap.put("^#?随机(meme(s)?|表情包)", "randomMemes");
        funcMap.put("^#?(meme(s)?|表情包)帮助", "memesHelp");
        funcMap.put("^#?(meme(s)?|表情包)搜索", "memesSearch");
    }

    @Autowired
    public MemeUtil(MessageRecordService messageRecordService) {
        MemeUtil.messageRecordService = messageRecordService;
    }


    public static void sendMessage(MessageEventContext messageEventContext, String message) {
        sendMessage(messageEventContext, message, null);
    }

    public static void sendMessage(MessageEventContext messageEventContext, String message, List<File> imageList) {
        Contact subject = messageEventContext.getMessageEvent().getSubject();
        MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
        if (reply) {
            messageChainBuilder.append(messageEventContext.getQuoteReply());
        }
        if (!MyStringUtil.isEmpty(message)) {
            messageChainBuilder.append(message);
        }
        if (imageList != null) {
            for (File img : imageList) {
                Image image = Contact.uploadImage(subject, img);
                messageChainBuilder.append(image);
            }
        }
        subject.sendMessage(messageChainBuilder.build());
    }

    public static String matchKey(MessageEventContext messageEventContext) {
        String input = messageEventContext.getContent();
        // 遍历map对象的键集合
        for (String key : funcMap.keySet()) {
            // 创建一个正则模式对象，根据键值
            Pattern pattern = Pattern.compile(key);
            // 判断输入字符串是否匹配该模式
            if (pattern.matcher(input).matches()) {
                // 如果匹配，返回对应的值
                return funcMap.get(key);
            }
        }
        // 如果没有匹配到任何键，返回null
        return null;
    }

    public static void memesList(MessageEventContext messageEventContext) {
        File target = null;
        try {
            target =handlerRequest(messageEventContext,baseUrl+"/memes/render_list", null,null,null);
            if (target!=null) {
                ArrayList<File> imgllll = new ArrayList<>();
                imgllll.add(target);
                sendMessage(messageEventContext, "", imgllll);
            }
        }catch (Exception e){
            log.error("", e);
        }finally {
            FileUtil.deleteFile(target);
        }
    }

    public static void randomMemes() {
        //    let keys = Object.keys(infos).filter(key => infos[key].params.min_images === 1 && infos[key].params.min_texts === 0)
        //    let index = _.random(0, keys.length - 1, false)
        //    e.msg = infos[keys[index]].keywords[0]
        //    return await this.memes(e)
    }


    public static File memes(MessageEventContext messageEventContext) {
        List<File> toSubmitFile = new ArrayList<>();
        try {
            String content = messageEventContext.getContent().trim();
            if (content.startsWith("#")){
                content = content.substring(1);
            }else {
                if (messageEventContext.isGroup()){
                    Group group = (Group) messageEventContext.getMessageEvent().getSubject();
                    if (GroupId.Group985_2 == group.getId()){
                        return null;
                    }
                }
            }
            Optional<String> result = keyMap.keySet().stream().filter(content::startsWith).findFirst();
            String target = null;
            if (result.isPresent()){
                target = result.get();
            }
            if (target==null){
                return null;
            }
            if ("玩".equals(target) && content.startsWith("玩游戏")) {
                target = "玩游戏";
            }
            if ("滚".equals(target) && content.startsWith("滚屏")) {
                target = "滚屏";
            }

            String targetCode = keyMap.get(target);
            log.info("memes:" + targetCode);
            // let target = e.msg.replace(/^#?meme(s)?/, '')
            String text1 = content.replaceAll(target, "");
            if ("详情".equals(text1.trim()) || "帮助".equals(text1.trim())) {
                sendMessage(messageEventContext, detail(targetCode));
                return null;
            }

            String[] parts = text1.split("#", 2);
            String text = parts[0];
            String args = parts.length > 1 ? parts[1] : "";

            MessageEvent messageEvent = messageEventContext.getMessageEvent();
            long sendId = 2482095029L;
            if (messageEvent != null) {
                sendId = messageEventContext.getMessageEvent().getSender().getId();
            }

            // 如果info对象中的params对象中的max_images属性大于0，表示可以有图，来从回复、发送和头像找图
            Contact subject = messageEvent == null?null: messageEvent.getSubject();
            // 定义一个userInfos变量，用于存储用户信息
            List<Map<String, Object>> userInfos = new ArrayList<>();

            ArrayList<At> atList = new ArrayList<>(messageEventContext.getAtList());
            List<String> imgUrls = new ArrayList<>();

            // 从infos对象中根据targetCode键获取对应的信息对象，并赋值给info变量
            Map<String, Object> info = (Map<String, Object>) infoMap.get(targetCode);

            String[] split = text.split("/"); // 用 / 分割字符串
            ArrayList<String> nonNumbers = new ArrayList<>(); // 创建一个空的字符串数组列表，用来存放非数字部分
            int start = 0; // 一个变量，表示开头连续数字的结束位置
            int end = split.length - 1; // 一个变量，表示结尾连续数字的开始位置
            while (start < split.length && isNumber(split[start].trim())) { // 从开头遍历，处理掉开头的数字
                atList.add(new At(Long.parseLong(split[start].trim()))); // 把数字转换为长整型，并添加到 atList 中
                start++; // 更新 start 的值
            }
            while (end >= start && isNumber(split[end].trim())) { // 从末尾遍历，处理掉结尾的数字
                atList.add(new At(Long.parseLong(split[end].trim()))); // 把数字转换为长整型，并添加到 atList 中
                end--; // 更新 end 的值
            }
            for (int i = start; i <= end; i++) { // 遍历中间两头都是非数字的部分
                String s = split[i].trim(); // 去掉空格
                if (!MyStringUtil.isEmpty(s)) { // 如果 s 不是空字符串
                    nonNumbers.add(s); // 把 s 添加到非数字列表中
                }
            }
            split = nonNumbers.toArray(new String[nonNumbers.size()]); // 把非数字列表转换为字符串数组


            // 如果text变量为空，并且info对象中的params对象中的min_texts属性大于0，表示需要文本
            if (split.length==0 && (Integer) ((Map<String, Object>) info.get("params")).get("min_texts") > 0) {
                // 如果e对象的message属性中有任何元素的type属性等于at，表示艾特的用户
                if (messageEventContext.isGroup() && atList.size()>0) {
                    long targetId = atList.get(0).getTarget();
                    Group group = (Group) subject;
                    NormalMember normalMember = group.get(targetId);
                    if (normalMember !=null) {
                        text = normalMember.getNameCard();
                    }else {
                        if (messageEventContext.isGroup()){
                            normalMember = group.get(sendId);
                            text = normalMember.getNameCard();
                        }else {
                            text = messageEvent.getSender().getNick();
                        }
                    }
                } else {
                    // 否则，将e对象的sender属性中的card属性或者nickname属性赋值给text变量
                    if (messageEventContext.isGroup()){
                        Group group = (Group) subject;
                        NormalMember normalMember = group.get(sendId);
                        text = normalMember.getNameCard();
                    }else {
                        text = messageEvent.getSender().getNick();
                    }
                }
                if (MyStringUtil.isEmpty(text)){
                    text = messageEvent.getSender().getNick();
                }
                split = new String[]{text};
            }


            if ((Integer) ((Map<String, Object>) info.get("params")).get("max_images") > 0) {

                // 如果e对象有source属性，优先从回复找图
                QuoteReply quoteReply = messageEvent == null?null: messageEvent.getMessage().get(QuoteReply.Key);
                if (quoteReply != null) {
                    int[] ids = quoteReply.getSource().getIds();
                    MessageRecord messageById = MemeUtil.messageRecordService.getMessageById(subject.getId(), ids[0]);
                    if (!MyStringUtil.isEmpty(messageById.getImageUrlList())) {
                        Map<String, String> imageMap = JsonUtil.fromJson(messageById.getImageUrlList(), Map.class);
                        imgUrls.addAll(imageMap.values());
                    }
                } else if (messageEventContext.getImages().size() > 0) {
                    for (Image image : messageEventContext.getImages()) {
                        String queryUrl = Image.queryUrl(image);
                        imgUrls.add(queryUrl);
                    }
                } else if (atList.size() > 0) {
                    imgUrls = atList.stream().map(At::getTarget).map(qq -> "https://q1.qlogo.cn/g?b=qq&s=0&nk=" + qq).collect(Collectors.toList());
                }

                // 如果imgUrls列表为空或者长度为0，表示都没有，用发送者的头像
                if (imgUrls.size() == 0) {
                    // 将e对象的sender属性中的user_id属性作为参数，拼接成一个头像链接，并添加到imgUrls列表中
                    imgUrls.add("https://q1.qlogo.cn/g?b=qq&s=0&nk=" + sendId);
                }

                // 如果imgUrls列表的长度小于info对象中的params对象中的min_images属性，并且imgUrls列表中不包含发送者头像，表示数量不够，补上发送者头像，且放到最前面
                if (imgUrls.size() < (Integer) ((Map<String, Object>) info.get("params")).get("min_images")) {
                    imgUrls.add(0, "https://q1.qlogo.cn/g?b=qq&s=0&nk=" + sendId);
                }
                for (String url: imgUrls){
                    toSubmitFile.add(FileUtil.downLoadImage(url, "prememe_"+ UUID.randomUUID().toString().replaceAll("-", "")+".jpg", true));
                }
            }

            // 使用split方法将text变量按照斜杠分割成一个字符串数组，并根据info对象中的params对象中的max_texts属性截取前面部分，并赋值给texts变量，表示文本数组
            String[] texts = Arrays.copyOfRange(split, 0, (Integer) ((Map<String, Object>) info.get("params")).get("max_texts"));
            // 如果text变量不为空，并且info对象中的params对象中的max_texts属性等于0，表示不需要文本
            if (split.length > 0 && (Integer) ((Map<String, Object>) info.get("params")).get("max_texts") == 0) {
                log.warn("texts > 0, max_texts==0");
                // 返回false
                return null;
            }
            // 如果texts变量的长度小于info对象中的params对象中的min_texts属性，表示文本数量不够
            if (texts.length < (Integer) ((Map<String, Object>) info.get("params")).get("min_texts")) {
                // 使用reply方法，传入一个字符串参数和true作为参数，表示回复用户
                sendMessage(messageEventContext, "字不够！要至少" + (Integer) ((Map<String, Object>) info.get("params")).get("min_texts") + "个用/隔开！");
                return null;
            }

            List<String> toSubmitText = new ArrayList<>(Arrays.asList(texts));
            // 如果info对象中的params对象中的max_texts属性大于0，并且formData对象中获取texts键对应的值的长度等于0，表示需要文本
            if ((Integer) ((Map<String, Object>) info.get("params")).get("max_texts") > 0 && toSubmitText.size() == 0) {
                // 如果formData对象中获取texts键对应的值的长度小于info对象中的params对象中的max_texts属性，表示文本数量不够
                if (toSubmitText.size() < (Integer) ((Map<String, Object>) info.get("params")).get("max_texts")) {
                    // 如果e对象的message属性中有任何元素的type属性等于at，表示艾特的用户
                    String toAppend = "";
                    try {
                        if (atList.size() > 0) {
                            // 使用formData对象的append方法，传入texts作为第一个参数，传入e对象的message属性中过滤出type属性等于at的元素，并获取第一个元素，并去掉两边的@符号作为第二个参数，表示将文本添加到表单数据中
                            long targetId = atList.get(0).getTarget();
                            toAppend = String.valueOf(targetId);

                            Group group = (Group) subject;
                            NormalMember normalMember = group.get(targetId);
                            if (normalMember !=null) {
                                toAppend = normalMember.getNameCard();
                            }else {
                                if (messageEventContext.isGroup()){
                                    normalMember = group.get(sendId);
                                    toAppend = normalMember.getNameCard();
                                }else {
                                    toAppend = messageEvent.getSender().getNick();
                                }
                            }
                        } else {
                            // 否则，将e对象的sender属性中的card属性或者nickname属性赋值给text变量
                            if (messageEventContext.isGroup()){
                                Group group = (Group) subject;
                                NormalMember normalMember = group.get(sendId);
                                toAppend = normalMember.getNameCard();
                            }else {
                                toAppend = messageEvent.getSender().getNick();
                            }
                        }
                    }catch (Exception e){}

                    toSubmitText.add(toAppend);
                }
            }
            // 如果e对象的message属性中有任何元素的type属性等于at，表示艾特的用户
            if (atList.size() > 0 && messageEventContext.isGroup()) {
                // 遍历userInfos变量中的每个元素
                Group group = (Group) subject;
                for (At at : atList) {
                    // 根据ui元素的qq属性，在mm变量中获取对应的User对象，并赋值给user变量，表示用户信息
                    String name= String.valueOf(at.getTarget()),gender="unknown";
                    try {
                        NormalMember normalMember = group.get(at.getTarget());
                        // 将user对象的sex属性赋值给ui元素的gender属性，表示用户性别
                        UserProfile userProfile = normalMember.queryProfile();
                        // 将user对象的card属性或者nickname属性赋值给ui元素的text属性，表示用户昵称
                        name = normalMember.getNameCard();
                        if (MyStringUtil.isEmpty(name)){
                            name = normalMember.getNick();
                        }
                        gender = userProfile.getSex().name().toLowerCase();
                    }catch (Exception e){}
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("name", name);
                    userInfo.put("gender", gender);
                    userInfos.add(userInfo);
                }
            }
            // 如果userInfos变量为空
            if (userInfos.size() == 0) {
                String nameCard = String.valueOf(sendId);
                String gender = "unknown";
                try {
                    if (messageEventContext.isGroup()){
                        Group group = (Group) subject;
                        NormalMember normalMember = group.get(sendId);
                        nameCard = normalMember.getNameCard();
                        if (MyStringUtil.isEmpty(nameCard)){
                            nameCard = normalMember.getNick();
                        }
                    }else {
                        nameCard = messageEvent.getSender().getNick();
                    }
                    gender = messageEvent.getSender().queryProfile().getSex().name().toLowerCase();
                }catch (Exception e){
                }
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("name", nameCard);
                userInfo.put("gender", gender);
                userInfos.add(userInfo);
            }

            args = handleArgs(targetCode, args, userInfos);
            String url = baseUrl + "/memes/" + targetCode + "/";
            log.info("url:{} ,image:{} ,text:{} ,args:{}", url, imgUrls, toSubmitText, args);
            File targetFile = handlerRequest(messageEventContext, url, toSubmitFile, toSubmitText, args);
            if (targetFile!=null) {
                ArrayList<File> imgllll = new ArrayList<>();
                imgllll.add(targetFile);
                sendMessage(messageEventContext, "", imgllll);
            }
            return targetFile;
        }catch (Exception e){
            log.error("", e);
        }
        return null;
    }

    // 定义一个handleArgs方法，输入一个key字符串，一个args字符串，和一个userInfos列表，输出一个argsObj字符串
    public static String handleArgs(String key, String args, List<Map<String, Object>> userInfos) {
        // 如果args变量为空
        if (MyStringUtil.isEmpty(args)) {
            // 将args变量设为空字符串
            args = "";
        }
        // 定义一个argsObj变量，用于存储参数对象
        Map<String, Object> argsObj = new HashMap<>();
        // 使用switch语句，根据key的不同值，给argsObj赋不同的值
        switch (key) {
            case "look_flat": {
                // 将args变量转换为整数，如果为空则默认为2，并赋值给ratio变量
                int ratio = args.isEmpty() ? 2 : Integer.parseInt(args);
                // 将ratio变量作为键值对添加到argsObj中，键为ratio
                argsObj.put("ratio", ratio);
                break;
            }
            case "crawl": {
                // 将args变量转换为整数，如果为空或者无效则随机生成一个1到92之间的整数，并赋值给number变量
                int number = args.isEmpty() || !isNumber(args) ? (int) (Math.random() * 92 + 1) : Integer.parseInt(args);
                // 将number变量作为键值对添加到argsObj中，键为number
                argsObj.put("number", number);
                break;
            }
            case "guichu":
            case "symmetric": {
                // 定义一个directionMap变量，用于存储方向的映射关系
                Map<String, String> directionMap = new HashMap<>();
                directionMap.put("左", "left");
                directionMap.put("右", "right");
                directionMap.put("上", "top");
                directionMap.put("下", "bottom");
                // 根据args变量去掉两边空格后的值，在directionMap中获取对应的值，并赋值给direction变量，如果没有对应的值，则默认为left
                String direction = directionMap.getOrDefault(args.trim(), "left");
                // 将direction变量作为键值对添加到argsObj中，键为direction
                argsObj.put("direction", direction);
                break;
            }
            case "petpet":
            case "jiji_king":
            case "kaleidoscope":
            case "kirby_hammer": {
                // 判断args变量是否以圆开头，并赋值给circle变量
                boolean circle = args.startsWith("圆");
                // 将circle变量作为键值对添加到argsObj中，键为circle
                argsObj.put("circle", circle);
                break;
            }
            case "mourning": {
                boolean circle = args.startsWith("黑");
                argsObj.put("black", circle);
                break;
            }
            case "note_for_leave":
            case "my_friend": {
                // 如果args变量为空
                if (MyStringUtil.isEmpty(args)) {
                    // 将userInfos列表中的第一个元素的text属性去掉两边的@符号，并赋值给args变量
                    args = (String) userInfos.get(0).get("name");
                }
                // 将args变量作为键值对添加到argsObj中，键为name
                argsObj.put("name", args);
                break;
            }
            case "always": {
                // 定义一个modeMap变量，用于存储模式的映射关系
                Map<String, String> modeMap = new HashMap<>();
                modeMap.put("", "normal");
                modeMap.put("循环", "loop");
                modeMap.put("套娃", "circle");
                // 根据args变量在modeMap中获取对应的值，并赋值给mode变量，如果没有对应的值，则默认为normal
                String mode = modeMap.getOrDefault(args, "normal");
                // 将mode变量作为键值对添加到argsObj中，键为mode
                argsObj.put("mode", mode);
                break;
            }
            case "gun":
            case "bubble_tea": {
                // 定义一个directionMap变量，用于存储方向的映射关系
                Map<String, String> directionMap = new HashMap<>();
                directionMap.put("左", "left");
                directionMap.put("右", "right");
                directionMap.put("两边", "both");
                // 根据args变量去掉两边空格后的值，在directionMap中获取对应的值，并赋值给position变量，如果没有对应的值，则默认为right
                String position = directionMap.getOrDefault(args.trim(), "right");
                // 将position变量作为键值对添加到argsObj中，键为position
                argsObj.put("position", position);
                break;
            }
        }
        // 将userInfosObj变量作为键值对添加到argsObj中，键为user_infos
        argsObj.put("user_infos", userInfos);
        // 使用JSON类的stringify方法，将argsObj变量转换为一个JSON字符串，并返回
        return JsonUtil.toJson(argsObj);
    }

    // 定义一个isInteger方法，输入一个s字符串，输出一个boolean值，表示s是否是一个整数
    public static boolean isNumber(String s) {
        // 使用try-catch语句，尝试将s转换为整数，如果成功则返回true，如果失败则返回false
        try {
            Long.parseLong(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private static String detail(String code) {
        // 从infos对象中根据code键获取对应的信息对象，并赋值给d变量
        Map<String, Object> d = (Map<String, Object>) infoMap.get(code);
        // 从d对象中根据keywords键获取对应的列表对象，并用join方法将其转换为以顿号分隔的字符串，并赋值给keywords变量
        String keywords = String.join("、", (List<String>) d.get("keywords"));
        // 从d对象中根据params键获取对应的参数对象，并赋值给p变量
        Map<String, Object> p = (Map<String, Object>) d.get("params");
        // 使用字符串拼接的方式，将d和p对象中的各个属性值拼接成一个ins字符串，注意使用换行符和反引号来保持格式
        String ins = "【代码】" + d.get("key") + "\n" +
                "【名称】" + keywords + "\n" +
                "【最大图片数量】" + p.get("max_images") + "\n" +
                "【最小图片数量】" + p.get("min_images") + "\n" +
                "【最大文本数量】" + p.get("max_texts") + "\n" +
                "【最小文本数量】" + p.get("min_texts") + "\n" +
                "【默认文本】" + String.join("/", (List<String>) p.get("default_texts")) + "\n";
        // 从p对象中根据args键获取对应的列表对象，并判断其长度是否大于0，如果是，则表示有支持参数
        if (((List<String>) p.getOrDefault("args", new ArrayList<>())).size() > 0) {
            // 定义一个supportArgs字符串，用于存储支持参数的说明
            String supportArgs = "";
            // 使用switch语句，根据code的不同值，给supportArgs赋不同的值
            switch (code) {
                case "look_flat": {
                    supportArgs = "看扁率，数字.如#3";
                    break;
                }
                case "crawl": {
                    supportArgs = "爬的图片编号，1-92。如#33";
                    break;
                }
                case "guichu":
                case "symmetric": {
                    supportArgs = "方向，上下左右。如#下";
                    break;
                }
                case "mourning": {
                    supportArgs = "黑白, 如 #黑";
                    break;
                }
                case "petpet":
                case "jiji_king":
                case "kaleidoscope":
                case "kirby_hammer": {
                    supportArgs = "是否圆形头像，输入圆即可。如#圆";
                    break;
                }
                case "always": {
                    supportArgs = "一直图像的渲染模式，循环、套娃、默认。不填参数即默认。如一直#循环";
                    break;
                }
                case "gun":
                case "bubble_tea": {
                    supportArgs = "方向，左、右、两边。如#两边";
                    break;
                }
            }
            // 将supportArgs拼接到ins字符串后面，并换行
            ins += "【支持参数】" + supportArgs + "\n";
        }
        // 返回ins字符串
        return ins;
    }


    // 定义一个memesHelp方法，输入一个e对象，输出一个回复
    public void memesHelp(MessageEventContext messageEventContext) {
        String helpString = "【memes列表】：查看支持的memes列表\n" +
                "【{表情名称}】：memes列表中的表情名称，根据提供的文字或图片制作表情包\n" +
//                "【随机meme】：随机制作一些表情包\n" +
                "【meme搜索+关键词】：搜索表情包关键词\n" +
                "【{表情名称}+详情】：查看该表情所支持的参数";
        sendMessage(messageEventContext, helpString, null);
    }

    // 定义一个异步的memesSearch方法，输入一个e对象，输出一个回复
    public static void memesSearch(MessageEventContext messageEventContext) {
        String content = messageEventContext.getContent();
        // 从e对象中获取msg属性，并去掉开头的#?(meme(s)?|表情包)搜索和两边的空格，赋值给search变量
        String search = content.replaceAll("^#?(meme(s)?|表情包)搜索", "").trim();
        // 如果search变量为空
        if (MyStringUtil.isEmpty(search)) {
            // 调用e对象的reply方法，传入一个字符串参数
            sendMessage(messageEventContext, "你要搜什么？");
            // 返回true
            return;
        }
        // 定义一个hits列表，用于存储匹配到的键
        List<String> hits = new ArrayList<>();
        // 遍历keyMap对象的键集合
        for (String key : keyMap.keySet()) {
            // 如果键中包含search变量
            if (key.contains(search)) {
                // 将键添加到hits列表中
                hits.add(key);
            }
        }
        // 定义一个result字符串，用于存储搜索结果
        StringBuilder result = new StringBuilder("搜索结果");
        // 如果hits列表不为空
        if (!hits.isEmpty()) {
            // 遍历hits列表的索引和元素
            for (int i = 0; i < hits.size(); i++) {
                String hit = hits.get(i);
                // 将索引和元素拼接到result字符串中，并换行
                result.append("\n").append(i + 1).append(". ").append(hit);
            }
        } else {
            // 否则，将无拼接到result字符串中，并换行
            result.append("\n无");
        }
        // 调用e对象的reply方法，传入result字符串和isGroup属性
        sendMessage(messageEventContext, result.toString());
    }


    public static File handlerRequest(MessageEventContext messageEventContext, String url, List<File> fileList, List<String> texts, String args) throws FileNotFoundException {
        // 创建一个HttpPost类的实例
        HttpPost httpPost = new HttpPost(url);
        // 创建一个MultipartEntityBuilder类的实例
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        boolean need = false;
        // 添加文件部分
        if (!CollectionUtils.isEmpty(fileList)) {
            for (File f : fileList) {
                if (f!=null) {
                    need = true;
                    builder.addBinaryBody("images", new FileInputStream(f), ContentType.IMAGE_JPEG, f.getName());
                }
            }
        }
        ContentType contentType = ContentType.create("text/plain", "UTF-8");
        if (!CollectionUtils.isEmpty(texts)) {
            for (String text : texts) {
                if (!MyStringUtil.isEmpty(text)) {
                    need = true;
                    builder.addTextBody("texts", text, contentType);
                }
            }
        }

        if (!MyStringUtil.isEmpty(args)) {
            need = true;
            builder.addTextBody("args", args, contentType);
        }

        // 获取请求实体
        HttpEntity multipart = builder.build();
        // 将请求实体设置到请求对象中
        if (need) {
            httpPost.setEntity(multipart);
        }
        // 发送请求，并获取响应
        try {
            AtomicReference<File> ans = new AtomicReference<>();
            CloseableHttpClient client = HttpClientBuilder.create().build();
            return client.execute(httpPost, response -> {
                try {
                    HttpEntity entity = response.getEntity();
                    Header[] headers = response.getHeaders("Content-Type");
                    String ext = "jpg";
                    if (response.getStatusLine().getStatusCode() > 299) {
                        String error = response.getStatusLine().getStatusCode() + "\n" + StreamUtil.inputStreamToOutputStream(response.getEntity().getContent()).toString(StandardCharsets.UTF_8.name());
                        log.warn(error);
                        sendMessage(messageEventContext, error);
                        return null;
                    } else if (ContentType.IMAGE_JPEG.toString().equals(headers[0].getValue())) {
                        ext = "jpg";
                    } else if (ContentType.IMAGE_GIF.toString().equals(headers[0].getValue())) {
                        ext = "gif";
                    } else if (ContentType.IMAGE_PNG.toString().equals(headers[0].getValue())) {
                        ext = "png";
                    }
                    ans.set(StreamUtil.writeToFile("meme_" + UUID.randomUUID().toString().replaceAll("-", "") + "." + ext, entity.getContent()));
                    log.info(ans.get().getAbsolutePath());
                    return ans.get();
                }catch (Exception e){
                    log.error("", e);
                }
                return null;
            });
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }


    public static void main(String[] args) throws FileNotFoundException {
//        // 创建一个HttpPost类的实例
//        HttpPost httpPost = new HttpPost("http://124.221.233.84:2233/memes/5000choyen/");
//        // 创建一个MultipartEntityBuilder类的实例
//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        // 添加文件部分
//        ContentType contentType = ContentType.create("text/plain", "UTF-8");
//        builder.addTextBody("texts", "你好", contentType);
//        builder.addTextBody("texts", "你好.....", contentType);
//        // 获取请求实体
//        HttpEntity multipart = builder.build();
//        // 将请求实体设置到请求对象中
//        httpPost.setEntity(multipart);
//        // 发送请求，并获取响应
//        try {
//            AtomicReference<File> ans = new AtomicReference<>();
//            CloseableHttpClient client = HttpClientBuilder.create().build();
//            File t = client.execute(httpPost, response -> {
//                HttpEntity entity = response.getEntity();
//                Header[] headers = response.getHeaders("Content-Type");
//                String ext = "jpg";
//
//
//                if (response.getStatusLine().getStatusCode() > 299) {
//                    String error = StreamUtil.inputStreamToOutputStream(response.getEntity().getContent()).toString(StandardCharsets.UTF_8.name());
//                    return null;
//                } else if (ContentType.IMAGE_JPEG.toString().equals(headers[0].getValue())) {
//                    ext = "jpg";
//                } else if (ContentType.IMAGE_GIF.toString().equals(headers[0].getValue())) {
//                    ext = "gif";
//                } else if (ContentType.IMAGE_PNG.toString().equals(headers[0].getValue())) {
//                    ext = "png";
//                }
//                ans.set(StreamUtil.writeToFile("meme_" + UUID.randomUUID().toString().replaceAll("-", "") + "." + ext, entity.getContent()));
//                // 处理响应
//                return ans.get();
//            });
//
//            System.out.println(t.getAbsolutePath());
//        } catch (Exception e) {
//            log.error("", e);
//        }
//        handlerRequest(null,"http://124.221.233.84:2233/memes/render_list", null,null,null);

        MemeUtil.baseUrl = "http://124.221.233.84:2233";
        MessageEventContext context = new MessageEventContext();
        context.setContent("击剑 1339500240/a/1084701532/a/1339500240");

        context.setAtList(new ArrayList<>());
        context.setImages(new ArrayList<>());
        memes(context);

//        String[] parts = "他想看看".split("#", 2);
//        String text = parts[0];
//        String argss = parts.length > 1 ? parts[1] : "";
//        log.info("args:{}", argss);
//        System.out.println(argss);
//        if (MyStringUtil.isEmpty(argss)) {
//            // 将userInfos列表中的第一个元素的text属性去掉两边的@符号，并赋值给args变量
//            argss = "sssss";
//        }
//        System.out.println(argss);
    }

}
